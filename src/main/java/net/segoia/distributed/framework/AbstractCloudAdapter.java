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
/**
 * Abstract cloud adapter
 * @author adi
 *
 */
public abstract class AbstractCloudAdapter implements CloudAdapter{
	private ProcessingNode processingNode;

	public AbstractCloudAdapter(ProcessingNode processingNode){
		this.processingNode = processingNode;
	}
	
	public void advertise(DistributedServiceDescription serviceDescription) {
		// TODO Auto-generated method stub
		
	}

	public void advertise(DistributedServiceDescription[] servicesDescription) {
		// TODO Auto-generated method stub
		
	}

}
