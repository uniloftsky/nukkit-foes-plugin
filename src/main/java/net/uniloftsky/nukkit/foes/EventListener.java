package net.uniloftsky.nukkit.foes;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDeathEvent;
import net.uniloftsky.nukkit.foes.observer.EventPublisher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Event listener to catch in-game entity-related events.
 * EventListener only listens for events and doesn't perform any actions above them expect one.
 * It delegates the handling to its subscribers. Each subscriber should define how to handle the event.
 */
public class EventListener extends EventPublisher implements Listener {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public EventListener() {
        super(EXECUTOR_SERVICE);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        pushEvent(event);
    }

}
