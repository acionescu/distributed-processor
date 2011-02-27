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

public class ServicePropertyConfig {
    private String propertyName;
    private String sourceServiceName;
    private String methodName;
    private List<Object> arguments;
    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }
    /**
     * @return the sourceServiceName
     */
    public String getSourceServiceName() {
        return sourceServiceName;
    }
    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }
    /**
     * @return the arguments
     */
    public List<Object> getArguments() {
        return arguments;
    }
    /**
     * @param propertyName the propertyName to set
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    /**
     * @param sourceServiceName the sourceServiceName to set
     */
    public void setSourceServiceName(String sourceServiceName) {
        this.sourceServiceName = sourceServiceName;
    }
    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    /**
     * @param arguments the arguments to set
     */
    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }
    
}
