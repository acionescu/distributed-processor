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

/**
 * Holds informations about the advertised service
 * 
 * @author adi
 *
 */
public class DistributedServiceDescription implements Serializable, Comparable {
    /**
    * 
    */
    private static final long serialVersionUID = 7189121979409898875L;
    private String name;
    private String description;
    private String container;

    public DistributedServiceDescription() {

    }

    public DistributedServiceDescription(String name, String description) {
	this.name = name;
	this.description = description;
    }
    
    

    public DistributedServiceDescription(String name, String description, String container) {
	super();
	this.name = name;
	this.description = description;
	this.container = container;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public void setContainer(String container) {
	this.container = container;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getContainer() {
	return container;
    }

    public int compareTo(Object o) {
	if (o instanceof DistributedServiceDescription) {
	    DistributedServiceDescription dsd = (DistributedServiceDescription) o;
	    int r = name.compareTo(dsd.getName());
	    if (r != 0) {
		return r;
	    }
	    if (container == null) {
		if (dsd.getContainer() != null) {
		    return -1;
		}
	    } else {
		return container.compareTo(dsd.getContainer());
	    }
	}
	return 0;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((container == null) ? 0 : container.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DistributedServiceDescription other = (DistributedServiceDescription) obj;
	if (container == null) {
	    if (other.container != null)
		return false;
	} else if (!container.equals(other.container))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "DistributedServiceDescription [name=" + name + ", description=" + description + ", container="
		+ container + "]";
    }

}
