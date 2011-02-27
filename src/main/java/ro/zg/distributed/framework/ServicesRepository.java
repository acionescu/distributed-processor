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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * A repository with all the distributed services
 * @author adi
 *
 */
public class ServicesRepository {
	private Map<DistributedServiceDescription,DistributedService> services = new Hashtable<DistributedServiceDescription,DistributedService>();
	
	public void addService( DistributedService service){
		services.put(service.getServiceDescription(), service);
	}
	
	public DistributedServiceDescription[] getServicesDescriptions(){
		List<DistributedServiceDescription> desc = new ArrayList<DistributedServiceDescription>();
		for( DistributedService service : services.values()){
			desc.add(service.getServiceDescription());
		}
		return desc.toArray(new DistributedServiceDescription[0]);
	}
	
	public DistributedService getServiceByDescription(DistributedServiceDescription desc){
		return services.get(desc);
	}

}
