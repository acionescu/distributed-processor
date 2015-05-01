package ro.zg.eventbus;

public interface EventBus {

    void postEvent(Event event);
    
    void registerListener(EventListener listener);
    
    void removeListener(EventListener listener);
    
}
