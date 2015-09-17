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
package net.segoia.distributed.framework.cfg;

import java.util.ArrayList;
import java.util.List;

import net.segoia.distributed.framework.DistributedService;
import net.segoia.distributed.framework.ProcessingNode;
import net.segoia.distributed.framework.cfg.DefaultDistributedServiceFactory;
import net.segoia.distributed.framework.cfg.DefaultProcessingNodeFactory;
import net.segoia.distributed.framework.cfg.DistributedServiceConfiguration;
import net.segoia.distributed.framework.cfg.ProcessingNodeConfiguration;
import net.segoia.factory.ObjectFactory;
import junit.framework.TestCase;

public class ProcessingNodeFactoryTest extends TestCase{
    
//    public void testSimbleProcessingNode() throws Exception{
//	System.setProperty("jgroups.bind_addr", "192.168.0.100");
//	ProcessingNodeConfiguration pnc = new ProcessingNodeConfiguration();
//	pnc.setGroupName("testGroup");
//	
//	ObjectFactory<ProcessingNodeConfiguration, ProcessingNode> pnf = new DefaultProcessingNodeFactory();
//	ProcessingNode pn = pnf.createObject(pnc);
//	assertNotNull(pn);
//    }
    
    public void testProcessingNodeWithServices() throws Exception{
	System.setProperty("jgroups.bind_addr", "localhost");
	ProcessingNodeConfiguration pnc = new ProcessingNodeConfiguration();
	pnc.setGroupName("testGroup");
	
	List<DistributedServiceConfiguration> dscList = new ArrayList<DistributedServiceConfiguration>();
	DistributedServiceConfiguration dsc = new DistributedServiceConfiguration();
	dsc.setClassName("DummyDistributedService");
	dsc.setName("DummyService");
	dsc.setDescription("This is a dummy test service");
	
	dscList.add(dsc);
	pnc.setServicesConfiguration(dscList);
	
	ObjectFactory<DistributedServiceConfiguration, DistributedService> dsf = new DefaultDistributedServiceFactory();
	
	DefaultProcessingNodeFactory pnf = new DefaultProcessingNodeFactory();
	pnf.setDistributedServiceFactory(dsf);
	ProcessingNode pn = pnf.createObject(pnc);
	assertNotNull(pn);
	
    }

}
