package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Events publisher that pushes different type of {@link Event} further to its subscribers
 */
public class EventPublisher {

    private static final EventPublisher INSTANCE = new EventPublisher();

    /**
     * Executor service to notify subscribers concurrently
     */
    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Map of subscribers. Key - event type (event class), value - list of subscribers related to the given event type
     */
    private final Map<Class<? extends Event>, List<EventSubscriber>> subscribers = new HashMap<>();

    private EventPublisher() {
    }

    /**
     * Registers a subscriber to receive notifications for a specific event type.
     * <p>
     * The subscriber will be notified whenever an event of the specified type is published.
     *
     * @param subscriber subscriber to register
     * @param eventType  event type to subscribe to
     */
    public static synchronized void subscribe(EventSubscriber subscriber, Class<? extends Event> eventType) {
        List<EventSubscriber> subscribersByType = INSTANCE.subscribers.get(eventType);
        if (subscribersByType == null) {
            subscribersByType = new ArrayList<>();
        }
        subscribersByType.add(subscriber);
        INSTANCE.subscribers.put(eventType, subscribersByType);
    }

    /**
     * Push event to publisher. Publisher notifies subscribers about the given event
     *
     * @param event event to publish
     */
    public static void pushEvent(Event event) {
        Optional<List<EventSubscriber>> optionalSubscribers = Optional.ofNullable(INSTANCE.subscribers.get(event.getClass()));
        optionalSubscribers.ifPresent(subscribers -> {
            for (EventSubscriber subscriber : subscribers) {
                executor.submit(() -> subscriber.handleEvent(event));
            }
        });
    }

    /**
     * Should be called on programm shutdown
     */
    public static synchronized void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

}
