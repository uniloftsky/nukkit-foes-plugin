package net.uniloftsky.nukkit.foes.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.entity.EntityDespawnEvent;
import net.uniloftsky.nukkit.foes.FoesPlugin;
import net.uniloftsky.nukkit.foes.listener.observer.EntityEventPublisher;
import net.uniloftsky.nukkit.foes.listener.observer.EntityEventType;

/**
 * Event listener to catch in-game entity-related events
 */
public class EventListener implements Listener {

    /**
     * Plugin instance
     */
    private FoesPlugin plugin;

    public EventListener(FoesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        // push entity killed event
        EntityEventPublisher.pushEvent(EntityEventType.ENTITY_KILLED, event.getEntity());
    }

    @EventHandler
    public void onEntityDespawn(EntityDespawnEvent event) {

        // push entity removed event
        EntityEventPublisher.pushEvent(EntityEventType.ENTITY_REMOVED, event.getEntity());
    }

}
