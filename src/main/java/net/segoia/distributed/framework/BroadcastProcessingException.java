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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class BroadcastProcessingException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 6914572324033353861L;

    private Map<String, Exception> exceptions = new HashMap<String, Exception>();
    
    public BroadcastProcessingException(Map<String,Exception> ex) {
	this.exceptions = ex;
    }
    
    public void printStackTrace(PrintStream s) {
	for(Map.Entry<String, Exception> e : exceptions.entrySet()) {
	    s.println("Exception on node "+e.getKey()+" : ");
	    e.getValue().printStackTrace(s);
	}
    }
    
    public void printStackTrace(PrintWriter s) {
	for(Map.Entry<String, Exception> e : exceptions.entrySet()) {
	    s.println("Exception on node "+e.getKey()+" : ");
	    e.getValue().printStackTrace(s);
	}
    }
    
}
