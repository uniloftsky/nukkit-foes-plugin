package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Events publisher that pushes different type of {@link Event} further to its subscribers
 */
public abstract class EventPublisher {

    /**
     * Executor service to notify subscribers concurrently
     */
    private ExecutorService executor;

    /**
     * Map of subscribers. Key - event type (event class), value - list of subscribers related to the given event type
     */
    private Map<Class<? extends Event>, List<EventSubscriber>> subscribers = new ConcurrentHashMap<>();

    public EventPublisher(ExecutorService executorService) {
        this.executor = executorService;
    }

    /**
     * Registers a subscriber to receive notifications for a specific event type.
     * <p>
     * The subscriber will be notified whenever an event of the specified type is published.
     *
     * @param subscriber subscriber to register
     */
    public void subscribe(EventSubscriber subscriber) {
        if (subscriber.getEventTypes().isEmpty()) {
            throw new IllegalArgumentException("Subscriber should have at least one registered event type");
        }
        for (Class<? extends Event> event : subscriber.getEventTypes()) {
            List<EventSubscriber> subscribersByType = subscribers.computeIfAbsent(event, k -> new ArrayList<>());
            if (!subscribersByType.contains(subscriber)) {
                subscribersByType.add(subscriber);
            }
        }
    }

    /**
     * Push event to publisher. Publisher notifies subscribers about the given event
     *
     * @param event event to publish
     */
    protected void pushEvent(Event event) {
        Optional<List<EventSubscriber>> optionalSubscribers = Optional.ofNullable(subscribers.get(event.getClass()));
        optionalSubscribers.ifPresent(subscribers -> {
            for (EventSubscriber subscriber : subscribers) {
                executor.submit(() -> subscriber.handleEvent(event));
            }
        });
    }

    /**
     * Should be called on programm shutdown
     */
    public synchronized void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
