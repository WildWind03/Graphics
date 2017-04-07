package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.google.common.eventbus.EventBus;

public class EventBusSingleton {

    private static final String EVENT_BUS = "EVENT_BUS";

    private EventBus eventBus = new EventBus(EVENT_BUS);

    private static class Holder {
        private static final EventBusSingleton INSTANCE = new EventBusSingleton();
    }
    private EventBusSingleton() {

    }

    public static EventBus getInstance() {
        return Holder.INSTANCE.eventBus;
    }
}
