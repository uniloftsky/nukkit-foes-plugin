package net.uniloftsky.nukkit.foes.listener.observer;

import cn.nukkit.entity.Entity;

/**
 * Entity-related events subscriber. Part of the Observer Pattern.
 * Class that is supposed to be subscriber of {@link EntityEventPublisher} should implement this interface
 */
public interface EntityEventSubscriber {

    /**
     * Handle entity-related event inside of subscriber. Every subscriber should implement this method in their corresponding way.
     *
     * @param eventType event type
     * @param entity    affected entity
     */
    void handleEvent(EntityEventType eventType, Entity entity);

}
