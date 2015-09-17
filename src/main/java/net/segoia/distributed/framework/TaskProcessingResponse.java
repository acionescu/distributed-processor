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

import java.io.Serializable;

import org.jgroups.Address;

/**
 * The response that contains the information obtained after a task was processed
 * 
 * @author adi
 * 
 */
public class TaskProcessingResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3088811983234127035L;
    private Long taskId;
    protected Serializable result;
    protected Exception exception;
    private Address processingNodeAddress;

    public TaskProcessingResponse(Long taskId, Serializable result) {
	this.taskId = taskId;
	this.result = result;
    }

    public TaskProcessingResponse(Long taskId, Serializable result, Address nodeAddress) {
	this(taskId, result);
	this.processingNodeAddress = nodeAddress;
    }

    public TaskProcessingResponse(Long taskId, Serializable result, Exception ex) {
	this.taskId = taskId;
	this.result = result;
	this.exception = ex;
    }

    public TaskProcessingResponse(Long taskId, Serializable result, Exception ex, Address nodeAddress) {
	this(taskId, result, ex);
	this.processingNodeAddress = nodeAddress;
    }

    public TaskProcessingResponse(Long taskId) {
	this.taskId = taskId;
    }
    
    public boolean isSucessful() {
	return (exception == null);
    }

    public Long getTaskId() {
	return taskId;
    }

    public Serializable getResult() {
	return result;
    }

    public Exception getException() {
	return exception;
    }

    /**
     * @return the processingNodeAddress
     */
    public Address getProcessingNodeAddress() {
	return processingNodeAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((processingNodeAddress == null) ? 0 : processingNodeAddress.hashCode());
	result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	TaskProcessingResponse other = (TaskProcessingResponse) obj;
	if (processingNodeAddress == null) {
	    if (other.processingNodeAddress != null)
		return false;
	} else if (!processingNodeAddress.equals(other.processingNodeAddress))
	    return false;
	if (taskId == null) {
	    if (other.taskId != null)
		return false;
	} else if (!taskId.equals(other.taskId))
	    return false;
	return true;
    }

}
