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
package net.segoia.eventbus;

import java.io.Serializable;

public class Event implements Serializable {
    protected String type;
    protected long timestamp;
    protected Serializable content;
    
    public Event(String type, long timestamp, Serializable content) {
	super();
	this.type = type;
	this.timestamp = timestamp;
	this.content = content;
    }
    
    

    public Event(String type, Serializable content) {
	super();
	this.type = type;
	this.content = content;
	this.timestamp=System.currentTimeMillis();
    }



    /**
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
	return timestamp;
    }

    /**
     * @return the content
     */
    public Serializable getContent() {
	return content;
    }

}
