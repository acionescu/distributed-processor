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

import java.util.List;

public class ProcessingNodeConfiguration {
    /** name of the group to which this node belongs */
    private String groupName;
    /** default bind address */
    private String defaultBindAddress;
    /** the list of configurations for the services exposed by this node */
    private List<DistributedServiceConfiguration> servicesConfiguration;
    
    private ClassLoader resourcesLoader;
    
    public String getGroupName() {
        return groupName;
    }
    public List<DistributedServiceConfiguration> getServicesConfiguration() {
        return servicesConfiguration;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void setServicesConfiguration(List<DistributedServiceConfiguration> servicesConfiguration) {
        this.servicesConfiguration = servicesConfiguration;
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
     * @return the defaultBindAddress
     */
    public String getDefaultBindAddress() {
        return defaultBindAddress;
    }
    /**
     * @param defaultBindAddress the defaultBindAddress to set
     */
    public void setDefaultBindAddress(String defaultBindAddress) {
        this.defaultBindAddress = defaultBindAddress;
    }
    
}
