/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.distributed.framework;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BroadcastTaskProcessingResponse extends TaskProcessingResponse {
    private int expectedResponses;
    private Set<TaskProcessingResponse> responses = new HashSet<TaskProcessingResponse>();

    public BroadcastTaskProcessingResponse(Long taskId, int expectedResponses) {
	super(taskId);
	this.expectedResponses = expectedResponses;
    }

    public void addNewResponse(TaskProcessingResponse response) {
	responses.add(response);
	if(allResponsesReceived()) {
	    computeResult();
	}
    }

    private void computeResult() {
	Map<String, Serializable> result = new HashMap<String, Serializable>();
	Map<String,Exception> exceptions = new HashMap<String, Exception>();
	for (TaskProcessingResponse r : responses) {
	    if (r.isSucessful()) {
		result.put(r.getProcessingNodeAddress().toString(), r.getResult());
	    }
	    else {
		exceptions.put(r.getProcessingNodeAddress().toString(), r.getException());
	    }
	}
	if(result.size() > 0) {
	    this.result=(Serializable)result;
	}
	if(exceptions.size() > 0) {
	    this.exception=new BroadcastProcessingException(exceptions);
	}
    }

    public boolean allResponsesReceived() {
	return expectedResponses == responses.size();
    }

    /**
     * @return the expectedResponses
     */
    public int getExpectedResponses() {
	return expectedResponses;
    }

    /**
     * @return the responses
     */
    public Set<TaskProcessingResponse> getResponses() {
	return responses;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -1818241377608179748L;

}
