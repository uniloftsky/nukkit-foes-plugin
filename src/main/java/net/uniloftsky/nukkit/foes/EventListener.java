package net.uniloftsky.nukkit.foes;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDeathEvent;
import net.uniloftsky.nukkit.foes.observer.EventPublisher;

/**
 * Event listener to catch in-game entity-related events
 */
public class EventListener implements Listener {

    public EventListener() {
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        // push entity killed event
        EventPublisher.getInstance().pushEvent(event);
    }

}
