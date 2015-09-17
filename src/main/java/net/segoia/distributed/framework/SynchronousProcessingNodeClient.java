/**
 * distributed-processor - A distributed task processing framework
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.distributed.framework;

import java.util.HashMap;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;

public class SynchronousProcessingNodeClient implements ProcessingResponseReceiver {
    protected ProcessingNode processingNode;
    private Map<Long, Task> requests = new HashMap<Long, Task>();
    private Map<Long, TaskProcessingResponse> responses = new HashMap<Long, TaskProcessingResponse>();

    /**
     * Synchronously processes a task
     * 
     * @param task
     * @return
     * @throws ContextAwareException
     */
    public TaskProcessingResponse processTask(Task task) throws ContextAwareException {
	SubmitTaskResponse str = processingNode.submitTask(task, this);
	long taskId = task.getTaskId();
	if (str.isSuccessfull()) {
	    synchronized (task) {
		requests.put(taskId, task);
		try {
		    /* wait until the response is received from the processing node */
		    task.wait();
		    TaskProcessingResponse response = responses.remove(taskId);
		    return response;
		} catch (InterruptedException e) {
		    throw new ContextAwareException("INTERRUPTED_EXCEPTION", e);
		}
	    }
	} else {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put("taskId", taskId);
	    throw new ContextAwareException("TASK_SUBMIT_ERROR", str.getError());
	}
    }

    public void receiveProcessingResponse(TaskProcessingResponse processingResponse) throws Exception {
	long taskId = processingResponse.getTaskId();
	responses.put(taskId, processingResponse);
	Task inputTask = null;
	do { 
	/* it may happen that the response arrives before the task is added to pending
	request, so we wait for it */
	    inputTask = requests.remove(taskId);
	} while (inputTask == null);

	synchronized (inputTask) {
	    inputTask.notifyAll();
	}
    }

    /**
     * @return the processingNode
     */
    public ProcessingNode getProcessingNode() {
	return processingNode;
    }

    /**
     * @param processingNode
     *            the processingNode to set
     */
    public void setProcessingNode(ProcessingNode processingNode) {
	this.processingNode = processingNode;
    }
}
