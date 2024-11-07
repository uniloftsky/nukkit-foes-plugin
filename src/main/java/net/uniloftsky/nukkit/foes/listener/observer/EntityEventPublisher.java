package net.uniloftsky.nukkit.foes.listener.observer;

import cn.nukkit.entity.Entity;
import net.anotheria.idbasedlock.IdBasedLock;
import net.anotheria.idbasedlock.IdBasedLockManager;
import net.anotheria.idbasedlock.SafeIdBasedLockManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Entity events publisher that implements Observer pattern
 */
public class EntityEventPublisher {

    private static final EntityEventPublisher INSTANCE = new EntityEventPublisher();

    /**
     * Executor service to notify subscribers concurrently
     */
    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Lock manager to obtain locks on specific event types
     */
    private static IdBasedLockManager<EntityEventType> lockManager = new SafeIdBasedLockManager<>();

    /**
     * Map of subscribers that have a subscription for entity-related events
     */
    private Map<EntityEventType, List<EntityEventSubscriber>> subscribers = new HashMap<>();

    private EntityEventPublisher() {

        // initialize subscribers map
        for (EntityEventType eventType : EntityEventType.values()) {
            subscribers.put(eventType, new ArrayList<>());
        }
    }

    /**
     * Subscribe target class to this publisher. Should be called inside of client class that what to be subscribed to specific event types
     *
     * @param subscriber subscriber object
     * @param types      event types to subscribe
     */
    public static void subscribe(EntityEventSubscriber subscriber, EntityEventType... types) {
        for (EntityEventType type : types) {
            IdBasedLock<EntityEventType> lock = lockManager.obtainLock(type);
            lock.lock();
            try {
                List<EntityEventSubscriber> eventSubscribers = INSTANCE.subscribers.get(type);
                eventSubscribers.add(subscriber);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Push event to publisher. Publisher then will notify every subscriber about new event of specific type
     *
     * @param type   event type
     * @param entity affected entity
     */
    public static void pushEvent(EntityEventType type, Entity entity) {
        IdBasedLock<EntityEventType> lock = lockManager.obtainLock(type);
        lock.lock();
        try {
            List<EntityEventSubscriber> eventSubscribers = INSTANCE.subscribers.get(type);
            for (EntityEventSubscriber subscriber : eventSubscribers) {
                executor.submit(() -> subscriber.handleEvent(type, entity));
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Should be called on programm shutdown
     */
    public static void shutdownPublisher() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

}
