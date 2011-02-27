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

public class SimpleTask implements Task{
	/**
	 * 
	 */
	private static final long serialVersionUID = -628091888903709126L;
	private DistributedServiceDescription targetService;
	private Serializable content;
	private Long taskId;
	private String methodName;
	private boolean broadcastTask=false;
	
	public SimpleTask(DistributedServiceDescription target, Serializable content){
		targetService = target;
		this.content = content;
	}
	
	public SimpleTask(DistributedServiceDescription target, Serializable content,boolean isBroadcast){
	    this(target,content);
	    this.broadcastTask = isBroadcast;
	}

	public DistributedServiceDescription getTargetService() {
		return targetService;
	}

	public Serializable getContent() {
		return content;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
	    return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
	    this.methodName = methodName;
	}
	

	/**
	 * @return the broadcastTask
	 */
	public boolean isBroadcastTask() {
	    return broadcastTask;
	}

	/**
	 * @param broadcastTask the broadcastTask to set
	 */
	public void setBroadcastTask(boolean broadcastTask) {
	    this.broadcastTask = broadcastTask;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + (broadcastTask ? 1231 : 1237);
	    result = prime * result + ((content == null) ? 0 : content.hashCode());
	    result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
	    result = prime * result + ((targetService == null) ? 0 : targetService.hashCode());
	    result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
	    return result;
	}

	/* (non-Javadoc)
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
	    SimpleTask other = (SimpleTask) obj;
	    if (broadcastTask != other.broadcastTask)
		return false;
	    if (content == null) {
		if (other.content != null)
		    return false;
	    } else if (!content.equals(other.content))
		return false;
	    if (methodName == null) {
		if (other.methodName != null)
		    return false;
	    } else if (!methodName.equals(other.methodName))
		return false;
	    if (targetService == null) {
		if (other.targetService != null)
		    return false;
	    } else if (!targetService.equals(other.targetService))
		return false;
	    if (taskId == null) {
		if (other.taskId != null)
		    return false;
	    } else if (!taskId.equals(other.taskId))
		return false;
	    return true;
	}

	
}
