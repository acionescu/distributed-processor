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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.util.data.reflection.ReflectionUtility;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.MasterLogManager;

public class ReflectionBasedDistributedService extends AbstractDistributedService {
    private static Logger logger = MasterLogManager.getLogger(ReflectionBasedDistributedService.class.getName());
    private Object targetObject;
    private Map<String, List<String>> argumentsForMethods;
    private Map<String, String[]> compiledArgumetsTypes;
    private boolean setResourceLoaderOnStart = true;
    private boolean callInitOnStart = true;

    public ReflectionBasedDistributedService() {

    }

    public void start() throws ContextAwareException {
	super.start();
	try {
	    if (setResourceLoaderOnStart) {
		ReflectionUtility.callMethod(targetObject, "setResourcesLoader", new Object[] { getResourcesLoader() },
			new Class[] { ClassLoader.class });
	    }
	    if (callInitOnStart) {
		ReflectionUtility.callMethod(targetObject, "init", new Object[0]);
	    }
	    compileArgumetsForMethods();
	    if (logger.isDebugEnabled()) {
		logger.debug("Initializing reflection based service for target " + targetObject
			+ " with argument types : " + compiledArgumetsTypes);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void compileArgumetsForMethods() throws Exception {
	if (argumentsForMethods == null) {
	    return;
	}
	compiledArgumetsTypes = new HashMap<String, String[]>();
	for (Map.Entry<String, List<String>> e : argumentsForMethods.entrySet()) {
	    List<String> classes = new ArrayList<String>();
	    for (String className : e.getValue()) {

		classes.add(className);
	    }
	    compiledArgumetsTypes.put(e.getKey(), classes.toArray(new String[0]));
	}
    }

    public ReflectionBasedDistributedService(ProcessingNode processingNode, DistributedServiceDescription desc) {
	super(processingNode, desc);
    }

    public TaskProcessingResponse processTask(Task task) throws Exception {
	String methodName = task.getMethodName();
	Object[] arguments = (Object[]) task.getContent();
	int argslen = 0;
	if (arguments != null) {
	    argslen = arguments.length;
	}
	/* get the arguments types for this method if defined */
	String[] argumentsTypes = (compiledArgumetsTypes != null) ? compiledArgumetsTypes.get(methodName + "."
		+ argslen) : null;
	Serializable resp = null;
	TaskProcessingResponse tpr = null;

	try {
	    if (argumentsTypes != null) {
		if (logger.isDebugEnabled()) {
		    logger.debug("Calling method '" + methodName + "' on object " + targetObject + " with arguments "
			    + Arrays.asList(arguments) + " with types " + Arrays.asList(argumentsTypes));
		}
		resp = (Serializable) ReflectionUtility.callMethod(targetObject, methodName, arguments, argumentsTypes);
	    } else {
		if (logger.isDebugEnabled()) {
		    logger.debug("Calling method '" + methodName + "' on object " + targetObject
			    + ((arguments != null) ? " with arguments " + Arrays.asList(arguments) : ""));
		}
		resp = (Serializable) ReflectionUtility.callMethod(targetObject, methodName, arguments);
	    }
	    tpr = new TaskProcessingResponse(task.getTaskId(), resp);
	} catch (Exception e) {
	    tpr = new TaskProcessingResponse(task.getTaskId(), null, e/* (Exception)e.getCause() */);
	}

	return tpr;
    }

    /**
     * @return the targetObject
     */
    public Object getTargetObject() {
	return targetObject;
    }

    /**
     * @param targetObject
     *            the targetObject to set
     */
    public void setTargetObject(Object targetObject) {
	this.targetObject = targetObject;
    }

    /**
     * @return the argumentsForMethods
     */
    public Map<String, List<String>> getArgumentsForMethods() {
	return argumentsForMethods;
    }

    /**
     * @param argumentsForMethods
     *            the argumentsForMethods to set
     */
    public void setArgumentsForMethods(Map<String, List<String>> argumentsForMethods) {
	this.argumentsForMethods = argumentsForMethods;
    }

    @Override
    protected void configureProperty(String propName, Object value) throws ContextAwareException {
	try {
	    ReflectionUtility.setValueToField(targetObject, propName, value);
	} catch (Exception e) {
	    throw new ContextAwareException(e);
	}

    }

    /**
     * @return the setResourceLoaderOnStart
     */
    public boolean isSetResourceLoaderOnStart() {
	return setResourceLoaderOnStart;
    }

    /**
     * @return the callInitOnStart
     */
    public boolean isCallInitOnStart() {
	return callInitOnStart;
    }

    /**
     * @param setResourceLoaderOnStart
     *            the setResourceLoaderOnStart to set
     */
    public void setSetResourceLoaderOnStart(boolean setResourceLoaderOnStart) {
	this.setResourceLoaderOnStart = setResourceLoaderOnStart;
    }

    /**
     * @param callInitOnStart
     *            the callInitOnStart to set
     */
    public void setCallInitOnStart(boolean callInitOnStart) {
	this.callInitOnStart = callInitOnStart;
    }

}
