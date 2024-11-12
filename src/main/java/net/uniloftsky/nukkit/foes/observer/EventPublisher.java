package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Events publisher that pushes different type of {@link Event} further to its subscribers
 */
public final class EventPublisher {

    /**
     * Default executor service for event publisher
     */
    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newCachedThreadPool();

    /**
     * Publisher instance. Null before it's enabled
     */
    private static EventPublisher INSTANCE;

    /**
     * Get singleton instance
     *
     * @return one and only one instance of the event publisher
     */
    public static synchronized EventPublisher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventPublisher(DEFAULT_EXECUTOR);
        }
        return INSTANCE;
    }

    /**
     * Executor service to notify subscribers concurrently
     */
    private ExecutorService executor;

    /**
     * Map of subscribers. Key - event type (event class), value - list of subscribers related to the given event type
     */
    private Map<Class<? extends Event>, List<EventSubscriber>> subscribers = new ConcurrentHashMap<>();

    EventPublisher(ExecutorService executorService) {
        this.executor = executorService;
    }

    /**
     * Registers a subscriber to receive notifications for a specific event type.
     * <p>
     * The subscriber will be notified whenever an event of the specified type is published.
     *
     * @param subscriber subscriber to register
     * @param eventTypes event types to subscribe to
     */
    @SafeVarargs /* safe because it calls the internal method */
    public final void subscribe(EventSubscriber subscriber, Class<? extends Event>... eventTypes) {
        for (Class<? extends Event> event : eventTypes) {
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
    public void pushEvent(Event event) {
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
