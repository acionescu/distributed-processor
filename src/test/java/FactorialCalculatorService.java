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
import java.io.BufferedReader;
import java.io.InputStreamReader;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.distributed.framework.AbstractDistributedService;
import ro.zg.distributed.framework.DistributedServiceDescription;
import ro.zg.distributed.framework.ProcessingNode;
import ro.zg.distributed.framework.SimpleTask;
import ro.zg.distributed.framework.Task;
import ro.zg.distributed.framework.TaskProcessingResponse;
import ro.zg.log.Log;


public class FactorialCalculatorService extends AbstractDistributedService{
	
	public FactorialCalculatorService(ProcessingNode node, DistributedServiceDescription desc){
		super(node,desc);
	}

	public TaskProcessingResponse processTask(Task task) {
		long n = (Long)task.getContent();
		System.out.println("Calculez "+n+"!");
		long result = 1;
		for( long i=n;i>0;i--){
			result *= i;
		}
		return new TaskProcessingResponse(task.getTaskId(),result);
	}

	public void receiveProcessingResponse(
			TaskProcessingResponse processingResponse) {
		System.out.println("Raspunsul este: "+processingResponse.getResult());
		
	}
	
	public static void main(String[] args) throws Exception{
		DistributedServiceDescription factCalcDesc = new DistributedServiceDescription("FactorialCalculator","Fact calc");
		ProcessingNode node = new ProcessingNode();
		node.connect();
		
		FactorialCalculatorService factCalcServ = new FactorialCalculatorService(node,factCalcDesc);
		node.addService(factCalcServ);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		System.out.println("Introdu n si eu calculez n!");
		do{
			System.out.print("n=");
			line = reader.readLine();
			try{
				long n = Long.parseLong(line);
				factCalcServ.submitTask(new SimpleTask(factCalcDesc,n));
			}
			catch(Exception e){
				System.out.println("Tre sa introduci un numar intreg!");
			}
			System.out.println();
		}
		while(!"exit".equals(line));
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
