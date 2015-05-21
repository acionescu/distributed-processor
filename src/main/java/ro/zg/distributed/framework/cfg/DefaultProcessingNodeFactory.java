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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.jgroups.ChannelException;

import ro.zg.distributed.framework.DistributedService;
import ro.zg.distributed.framework.DistributedServiceDescription;
import ro.zg.distributed.framework.ProcessingNode;
import ro.zg.exceptions.ObjectCreationException;
import ro.zg.factory.ObjectFactory;
import ro.zg.log.Log;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class DefaultProcessingNodeFactory implements ObjectFactory<ProcessingNodeConfiguration, ProcessingNode> {
    private static Logger logger = MasterLogManager.getLogger(DefaultProcessingNodeFactory.class.getName());
    private ObjectFactory<DistributedServiceConfiguration, DistributedService> distributedServiceFactory = new DefaultDistributedServiceFactory();

    public ProcessingNode createObject(ProcessingNodeConfiguration template) throws ObjectCreationException {
	List<String> errors = validate(template);
	ProcessingNode processingNode = null;

	if (errors.size() > 0) {
	    throw new IllegalArgumentException("ProcessingNodeConfiguration validation errors: " + errors);
	}

	processingNode = new ProcessingNode(template.getGroupName());
	if (template.getDefaultBindAddress() != null) {
	    System.setProperty("jgroups.bind_addr", template.getDefaultBindAddress());
	}
	try {
	    processingNode.connect();
	} catch (ChannelException e) {
	    throw new ObjectCreationException("Could not create a processing node for group '"
		    + template.getGroupName() + "'", e);
	}
	if (template.getServicesConfiguration() != null) {
	    for (DistributedServiceConfiguration dsc : template.getServicesConfiguration()) {

		boolean startConditionSatisfied;
		try {
		    startConditionSatisfied = isServiceStartConditionSatisfied(dsc,processingNode);
		} catch (Exception e) {
		    throw new ObjectCreationException("Could not create a processing node for group '"
			    + template.getGroupName() + "' because could not check start condition for service "+dsc.getServiceDescription(), e);
		}
		if (startConditionSatisfied) {
		    dsc.setResourcesLoader(template.getResourcesLoader());
		    dsc.setProcessingNode(processingNode);
		    processingNode.addService(distributedServiceFactory.createObject(dsc));
		}
	    }
	}

	return processingNode;
    }

    private boolean isServiceStartConditionSatisfied(DistributedServiceConfiguration dsc, ProcessingNode processingNode)
	    throws Exception {
	DistributedServiceDescription desc = dsc.getServiceDescription();
	/*
	 * create a new instance for this service only if the maximum allowed instances is not passed
	 */
	int maxNumberOfInstancesPerGroup = dsc.getMaxNumberOfInstancesPerGroup();
	if (processingNode.getNumberOfInstancesForService(desc) >= maxNumberOfInstancesPerGroup) {
	    logger.info("SERVICE SKIPPED: " + desc + " -> there are already " + maxNumberOfInstancesPerGroup
		    + " instances in cluster");
	    return false;
	}
	/* check if we have any host restrictions */
	List<String> allowedHosts = dsc.getAllowedHosts();

	String hostName = InetAddress.getLocalHost().getHostName();
	if (allowedHosts != null && !allowedHosts.contains(hostName)) {
	    logger.info("SERVICE SKIPPED: " + desc + " -> hostname (" + hostName
		    + ") not included in the allowed hosts list " + allowedHosts);
	    return false;
	}

	return true;

    }

    private List<String> validate(ProcessingNodeConfiguration template) {
	List<String> errors = new ArrayList<String>();
	if (template == null) {
	    errors.add("ProcessingNodeConfiguration object cannot be null");
	} else {
	    // if (template.getGroupName() == null) {
	    // errors.add("groupName field cannot be null");
	    // }
	}
	return errors;
    }

    public ObjectFactory<DistributedServiceConfiguration, DistributedService> getDistributedServiceFactory() {
	return distributedServiceFactory;
    }

    public void setDistributedServiceFactory(
	    ObjectFactory<DistributedServiceConfiguration, DistributedService> distributedServiceFactory) {
	this.distributedServiceFactory = distributedServiceFactory;
    }

}
