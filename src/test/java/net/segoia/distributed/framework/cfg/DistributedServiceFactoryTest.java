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

import net.segoia.distributed.framework.DistributedService;
import net.segoia.distributed.framework.ProcessingNode;
import net.segoia.distributed.framework.cfg.DefaultDistributedServiceFactory;
import net.segoia.distributed.framework.cfg.DistributedServiceConfiguration;
import net.segoia.factory.ObjectFactory;
import junit.framework.TestCase;

public class DistributedServiceFactoryTest extends TestCase{

    public void testValidConfiguration() throws Exception{
//	ProcessingNode node = new ProcessingNode();
//	DistributedServiceConfiguration dsc = new DistributedServiceConfiguration();
//	dsc.setClassName("DummyDistributedService");
//	dsc.setName("DummyService");
//	dsc.setDescription("This is a dummy test service");
//	dsc.setProcessingNode(node);
//	
//	ObjectFactory<DistributedServiceConfiguration, DistributedService> dsf = new DefaultDistributedServiceFactory();
//	
//	DistributedService ds = dsf.createObject(dsc);
//	assertNotNull(ds);
    }
    
    public void testRemoteClasspathConfiguration() throws Exception{
//	ProcessingNode node = new ProcessingNode();
//	DistributedServiceConfiguration dsc = new DistributedServiceConfiguration();
//	dsc.setClassName("net.segoia.wcr.distributed.DistributedWebProcessor");
//	dsc.setName("DummyService");
//	dsc.setDescription("This is a dummy test service");
//	dsc.setProcessingNode(node);
//	dsc.setClassPathUrl("ftp://ftpuser:ftprepo@192.168.0.100/wcr.jar");
//	ObjectFactory<DistributedServiceConfiguration, DistributedService> dsf = new DefaultDistributedServiceFactory();
//	
//	DistributedService ds = dsf.createObject(dsc);
//	assertNotNull(ds);
	
	
    }
}
