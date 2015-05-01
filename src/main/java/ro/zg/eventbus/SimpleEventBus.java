package ro.zg.eventbus;

import java.util.HashSet;
import java.util.Set;

public class SimpleEventBus implements EventBus {
    private Set<EventListener> listeners = new HashSet<EventListener>();

    public void postEvent(Event event) {
	for(EventListener l : listeners) {
	    l.onEvent(event);
	}
    }

    public void registerListener(EventListener listener) {
	listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
	listeners.remove(listener);
    }

}
