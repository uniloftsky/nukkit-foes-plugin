package net.uniloftsky.nukkit.foes.listener.entity;

import cn.nukkit.entity.Entity;

/**
 * Event subscriber. Part of the Observer Pattern
 */
public interface EntityEventSubscriber {

    void handleEvent(EntityEventType eventType, Entity entity);

}
