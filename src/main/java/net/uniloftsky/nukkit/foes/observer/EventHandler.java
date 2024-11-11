package net.uniloftsky.nukkit.foes.observer;

import cn.nukkit.event.Event;

@FunctionalInterface
public interface EventHandler<T extends Event> {

    void handle(T event);

}
