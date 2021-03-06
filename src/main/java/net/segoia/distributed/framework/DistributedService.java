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

import net.segoia.commons.exceptions.ContextAwareException;

public interface DistributedService {
	
	TaskProcessingResponse processTask(Task task) throws Exception;
	
	DistributedServiceDescription getServiceDescription();
	
	/**
	 * Submits a task to be processed
	 * @param task
	 * @return the response weather the task was successfully submitted or not
	 */
	SubmitTaskResponse submitTask(Task task);
	
	/**
	 * Start this service
	 */
	void start() throws ContextAwareException;
	/**
	 * Stops this service
	 */
	void shutdown();

}
