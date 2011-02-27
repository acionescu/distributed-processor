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
import junit.framework.TestCase;
import ro.zg.distributed.framework.DistributedServiceDescription;
import ro.zg.distributed.framework.ProcessingNode;
import ro.zg.distributed.framework.SimpleTask;
import ro.zg.distributed.framework.SubmitTaskResponse;


public class ProcessingNodeTest extends TestCase{
	public void testProcessingNode() throws Exception{
		//System.out.println(InetAddress.getAllByName("adi-benq")[0]);
		System.setProperty("jgroups.bind_addr", "192.168.0.100");
		ProcessingNode node = new ProcessingNode();
		
		//node.addService("TestService", "DummyDistributedService");
		DistributedServiceDescription desc = new DistributedServiceDescription("TestService1","A service");
		DummyDistributedService serv1 = new DummyDistributedService(node,desc);
		node.connect();
		node.addService(serv1);	
		
		ProcessingNode node2 = new ProcessingNode();
		node2.connect();
		DistributedServiceDescription desc2 = new DistributedServiceDescription("TestService 2","Another service");
		DummyDistributedService serv2 = new DummyDistributedService(node2,desc2);
		node2.addService(serv2);	
		
		DistributedServiceDescription factCalcDesc = new DistributedServiceDescription("FactorialCalculator","Fact calc");
		FactorialCalculatorService factCalcServ = new FactorialCalculatorService(node2,factCalcDesc);
		node2.addService(factCalcServ);
		
		
		Thread.sleep(3000);
		SubmitTaskResponse resp1 = serv1.submitTask(new SimpleTask(desc2,"primul task"));
		SubmitTaskResponse resp2 = serv1.submitTask(new SimpleTask(desc2,"tasku doi"));
		SubmitTaskResponse resp3 = serv1.submitTask(new SimpleTask(desc,"tasku trei"));
		SubmitTaskResponse resp4 = serv2.submitTask(new SimpleTask(desc,"tasku patru"));
		SubmitTaskResponse resp5 = serv2.submitTask(new SimpleTask(factCalcDesc,4L));
		
		assertTrue(resp1.isSuccessfull());
		assertTrue(resp2.isSuccessfull());
		assertTrue(resp3.isSuccessfull());
		assertTrue(resp4.isSuccessfull());
		assertTrue(resp5.isSuccessfull());
		
		Thread.sleep(5000);
		
	}
}
