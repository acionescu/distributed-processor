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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import net.segoia.distributed.framework.AbstractDistributedService;
import net.segoia.distributed.framework.DistributedService;
import net.segoia.distributed.framework.DistributedServiceDescription;
import net.segoia.distributed.framework.ProcessingNode;
import net.segoia.exceptions.ObjectCreationException;
import net.segoia.factory.ObjectFactory;

public class DefaultDistributedServiceFactory implements
	ObjectFactory<DistributedServiceConfiguration, DistributedService> {

    public DistributedService createObject(DistributedServiceConfiguration serviceConfiguration)
	    throws ObjectCreationException {
	AbstractDistributedService distributedService = null;

	List<String> errors = validateServiceConfiguration(serviceConfiguration);

	if (errors.size() > 0) {
	    throw new IllegalArgumentException("DistributedServiceConfiguration validation errors: " + errors);
	}
	ClassLoader resourcesLoader = serviceConfiguration.getResourcesLoader();
	if (serviceConfiguration.getInstance() != null) {
	    distributedService = (AbstractDistributedService) serviceConfiguration.getInstance();
	    distributedService.setProcessingNode(serviceConfiguration.getProcessingNode());
	    distributedService.setServiceDescription(serviceConfiguration.getServiceDescription());
	} else {
	    String dsClassName = serviceConfiguration.getClassName();
	    
	    Class<?> dsClass = null;
	    /* try to get the class */
	    try {
		dsClass = Class.forName(dsClassName);
	    } catch (ClassNotFoundException e) {
		/* if the class was not found try to load it from the specified repository */
		String repositoryUrl = serviceConfiguration.getClassPathUrl();
		try {
		    URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL(repositoryUrl) },serviceConfiguration.getResourcesLoader());
		    resourcesLoader = urlClassLoader;
		    try {
			dsClass = urlClassLoader.loadClass(dsClassName);
		    } catch (ClassNotFoundException e1) {
			throw new ObjectCreationException("Could not load the class " + dsClassName + " from "
				+ repositoryUrl, e1);
		    }
		} catch (MalformedURLException e1) {
		    throw new ObjectCreationException("Error loading the class from " + repositoryUrl, e1);
		}
	    }

	    DistributedServiceDescription dsd = new DistributedServiceDescription(serviceConfiguration.getName(),
		    serviceConfiguration.getDescription());

	    try {
		distributedService = (AbstractDistributedService) dsClass.getConstructor(
			new Class[] { ProcessingNode.class, DistributedServiceDescription.class }).newInstance(
			serviceConfiguration.getProcessingNode(), dsd);
	    } catch (Exception e) {
		throw new ObjectCreationException("Could not create an instace of class " + dsClassName, e);
	    }
	}
	distributedService.setResourcesLoader(resourcesLoader);
	distributedService.setRuntimeServiceConfig(serviceConfiguration.getRuntimeServiceConfig());
	return distributedService;
    }

    private List<String> validateServiceConfiguration(DistributedServiceConfiguration dsc) {
	List<String> errors = new ArrayList<String>();

	if (dsc == null) {
	    errors.add("DistributedServiceConfiguration object is null");
	} else {
	    if (dsc.getClassName() == null && dsc.getInstance() == null) {
		errors.add("className field on DistributedServiceConfiguration is null");
	    }
	    if (dsc.getName() == null) {
		errors.add("name field on DistributedServiceConfiguration is null");
	    }
	    if (dsc.getDescription() == null) {
		errors.add("description field on DistributedServiceConfiguration is null");
	    }
	}

	return errors;
    }

}
