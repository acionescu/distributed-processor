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
package ro.zg.distributed.framework.cfg;

import ro.zg.distributed.framework.DistributedService;
import ro.zg.distributed.framework.DistributedServiceDescription;
import ro.zg.distributed.framework.ProcessingNode;

/**
 * Holds the configuration of a distributed service
 * @author adi
 *
 */
public class DistributedServiceConfiguration {
    /** the name of the service */
    private String name;
    /** the description of the service*/
    private String description;
    /** the name of the class that implements the service */
    private String className;
    /** the url where the needed dependencies can be found */
    private String classPathUrl;
    private RuntimeServiceConfiguration runtimeServiceConfig;
    private ClassLoader resourcesLoader;
    
    private DistributedService instance;
    /** 
     * specifies if this service is mandatory or not
     * if it is mandatory it will be started if no other instance is found to be running on peer nodes 
     * or if the node where it runs fails
     */
    private boolean mandatory;
    /** the ProcessingNode instance which will hold this service */
    private ProcessingNode processingNode;
    
    private int maxNumberOfInstancesPerGroup = 1;
    
    public String getName() {
        return name;
    }
    public String getClassName() {
        return className;
    }
    public String getClassPathUrl() {
        return classPathUrl;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setClassPathUrl(String classPathUrl) {
        this.classPathUrl = classPathUrl;
    }
    
    /**
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }
    /**
     * @param mandatory the mandatory to set
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ProcessingNode getProcessingNode() {
        return processingNode;
    }
    public void setProcessingNode(ProcessingNode processingNode) {
        this.processingNode = processingNode;
    }
    /**
     * @return the maxNumberOfInstancesPerGroup
     */
    public int getMaxNumberOfInstancesPerGroup() {
        return maxNumberOfInstancesPerGroup;
    }
    /**
     * @param maxNumberOfInstancesPerGroup the maxNumberOfInstancesPerGroup to set
     */
    public void setMaxNumberOfInstancesPerGroup(int maxNumberOfInstancesPerGroup) {
        this.maxNumberOfInstancesPerGroup = maxNumberOfInstancesPerGroup;
    }
 
    public DistributedServiceDescription getServiceDescription(){
	return new DistributedServiceDescription(name,null);
    }
    /**
     * @return the instance
     */
    public DistributedService getInstance() {
        return instance;
    }
    /**
     * @param instance the instance to set
     */
    public void setInstance(DistributedService instance) {
        this.instance = instance;
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
