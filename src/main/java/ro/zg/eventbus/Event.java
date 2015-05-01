package ro.zg.eventbus;

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
