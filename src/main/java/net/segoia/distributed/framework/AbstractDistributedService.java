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

import java.util.List;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.distributed.framework.cfg.RuntimeServiceConfiguration;
import net.segoia.distributed.framework.cfg.ServicePropertyConfig;

public abstract class AbstractDistributedService implements DistributedService, ProcessingResponseReceiver {
    private DistributedServiceDescription serviceDescription;
    private ProcessingNode processingNode;
    private ClassLoader resourcesLoader;
    private RuntimeServiceConfiguration runtimeServiceConfig;
    
    private SynchronousProcessingNodeClient client;
    
    public AbstractDistributedService(){
	
    }
    
    public AbstractDistributedService(ProcessingNode processingNode, DistributedServiceDescription desc) {
	this.processingNode = processingNode;
	serviceDescription = desc;
    }

    public DistributedServiceDescription getServiceDescription() {
	return serviceDescription;
    }

    public SubmitTaskResponse submitTask(Task task) {
	return processingNode.submitTask(task, this);
    }

    public void receiveProcessingResponse(TaskProcessingResponse processingResponse) {
	/* override if needed */
    }

    /**
     * Override if needed
     */
    public void shutdown() {
	

    }

    /**
     * Override if needed
     * @throws ContextAwareException 
     */
    public void start() throws ContextAwareException {
	initClient();
	configure();
    }
    
    private void initClient(){
	client = new SynchronousProcessingNodeClient();
	client.setProcessingNode(processingNode);
    }
    
    private void configure() throws ContextAwareException{
	if(runtimeServiceConfig == null){
	    return;
	}
	List<ServicePropertyConfig> propsConfig = runtimeServiceConfig.getPropertiesConfig();
	if(propsConfig == null){
	    return;
	}
	for(ServicePropertyConfig spc : propsConfig){
	    TaskProcessingResponse resp = client.processTask(createTaskFromPropConfig(spc));
	    configureProperty(spc.getPropertyName(), resp.getResult());
	}
    }
    
    protected abstract void configureProperty(String propName, Object value) throws ContextAwareException;
    
    private Task createTaskFromPropConfig(ServicePropertyConfig spc){
	DistributedServiceDescription desc = new DistributedServiceDescription(spc.getSourceServiceName(),null);
	Object[] args = new Object[0];
	if(spc.getArguments() != null){
	    args = spc.getArguments().toArray();
	}
	SimpleTask t = new SimpleTask(desc,args);
	t.setMethodName(spc.getMethodName());
	return t;
    }

    public ProcessingNode getProcessingNode() {
	return processingNode;
    }

    public void setServiceDescription(DistributedServiceDescription serviceDescription) {
	this.serviceDescription = serviceDescription;
    }

    public void setProcessingNode(ProcessingNode processingNode) {
	this.processingNode = processingNode;
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
        return resourcesLoader;
    }

    /**
     * @param resourcesLoader the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }

    /**
     * @return the runtimeServiceConfig
     */
    public RuntimeServiceConfiguration getRuntimeServiceConfig() {
        return runtimeServiceConfig;
    }

    /**
     * @param runtimeServiceConfig the runtimeServiceConfig to set
     */
    public void setRuntimeServiceConfig(RuntimeServiceConfiguration runtimeServiceConfig) {
        this.runtimeServiceConfig = runtimeServiceConfig;
    }

}
