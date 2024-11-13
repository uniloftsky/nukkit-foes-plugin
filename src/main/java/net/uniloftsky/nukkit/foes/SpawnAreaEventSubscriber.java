package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.plugin.PluginLogger;
import net.uniloftsky.nukkit.foes.observer.EventHandler;
import net.uniloftsky.nukkit.foes.observer.EventSubscriber;

import java.util.Map;

/**
 * Events handler for spawn area.
 * <p>
 * Functionality of this class is based on events from EventPublisher.
 * Subscriber component consumes events and performs some actions on the spawn area considering the event type.
 */
public class SpawnAreaEventSubscriber extends EventSubscriber {

    private final int entityId;

    private SpawnArea spawnArea;

    SpawnAreaEventSubscriber(int entityId, SpawnArea spawnArea, PluginLogger logger) {
        super(logger);

        this.entityId = entityId;
        this.spawnArea = spawnArea;
    }

    @Override
    protected Map<Class<? extends Event>, EventHandler<? extends Event>> getHandlersMap() {
        return Map.of(EntityDeathEvent.class, (EventHandler<EntityDeathEvent>) this::onEntityDeath);
    }

    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getNetworkId() == entityId) {
            spawnArea.removeEntity(entity);
        }
    }
}
