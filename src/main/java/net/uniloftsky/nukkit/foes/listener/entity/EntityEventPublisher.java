package net.uniloftsky.nukkit.foes.listener.entity;

import cn.nukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity events publisher that implements Producer-Consumer pattern
 */
public class EntityEventPublisher {

    private static final EntityEventPublisher INSTANCE = new EntityEventPublisher();

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

    public static void subscribe(EntityEventSubscriber subscriber, EntityEventType... types) {
        for (EntityEventType type : types) {
            List<EntityEventSubscriber> eventSubscribers = INSTANCE.subscribers.get(type);
            eventSubscribers.add(subscriber);
        }
    }

    public static void pushEvent(EntityEventType type, Entity entity) {
        List<EntityEventSubscriber> eventSubscribers = INSTANCE.subscribers.get(type);
        for (EntityEventSubscriber subscriber : eventSubscribers) {
            subscriber.handleEvent(type, entity);
        }
    }

}
