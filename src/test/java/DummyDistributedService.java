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
import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.distributed.framework.AbstractDistributedService;
import net.segoia.distributed.framework.DistributedServiceDescription;
import net.segoia.distributed.framework.ProcessingNode;
import net.segoia.distributed.framework.SubmitTaskResponse;
import net.segoia.distributed.framework.Task;
import net.segoia.distributed.framework.TaskProcessingResponse;


public class DummyDistributedService extends AbstractDistributedService{
	
	public DummyDistributedService(ProcessingNode node,DistributedServiceDescription desc){
		super(node,desc);
	}

	public void receiveProcessingResponse(
			TaskProcessingResponse processingResponse) {
		System.out.println("Am primit raspunsu pentru "+processingResponse.getTaskId());
		System.out.println("Raspunsu este "+processingResponse.getResult());
		
	}

	public TaskProcessingResponse processTask(Task task) {
		System.out.println("Am primit task-ul cu id-ul "+task.getTaskId()+" si contentu= "+task.getContent());
		return new TaskProcessingResponse(task.getTaskId(),"Am procesat task-ul "+task.getTaskId());
	}

	
	public SubmitTaskResponse submitTask(Task task){
		return super.submitTask(task);
	}

	public void shutdown() {
	    // TODO Auto-generated method stub
	    
	}

	public void start() {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	protected void configureProperty(String propName, Object value) throws ContextAwareException {
	    // TODO Auto-generated method stub
	    
	}

}
