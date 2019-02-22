package com.event;

import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class EventBusFactory {

  //hold the instance of the event bus here
    private static EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
  
    public static EventBus getEventBus() {
        return eventBus;
    }

}