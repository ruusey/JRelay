package com.event.bus;
import com.google.common.eventbus.EventBus;
 
public class SimpleEventBusExample {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new SimpleListener());
        System.out.println("Post Simple EventBus Example");
        eventBus.post("Simple EventBus Example");
    }
}