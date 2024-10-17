package net.uniloftsky.nukkit.foes.listener;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.server.ServerStopEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import net.uniloftsky.nukkit.foes.FoesPlugin;
import net.uniloftsky.nukkit.foes.listener.observer.EntityEventPublisher;
import net.uniloftsky.nukkit.foes.listener.observer.EntityEventType;

import java.util.Map;

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
    public void onServerStop(ServerStopEvent event) {
        Map<Integer, Level> levels = Server.getInstance().getLevels();
        for (Level level : levels.values()) {
            for (Entity entity : level.getEntities()) {
                if (entity instanceof EntityLiving livingEntity) {
                    plugin.getLogger().info("entity will be killed");
                    EntityDeathEvent deathEvent = new EntityDeathEvent(livingEntity, new Item[0]);
                    Server.getInstance().getPluginManager().callEvent(deathEvent);
                }
            }
        }
    }

}
