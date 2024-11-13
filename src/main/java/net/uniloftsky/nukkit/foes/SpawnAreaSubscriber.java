package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.plugin.PluginLogger;
import net.uniloftsky.nukkit.foes.observer.EventHandler;
import net.uniloftsky.nukkit.foes.observer.EventSubscriber;

import java.util.Map;

public class SpawnAreaSubscriber extends EventSubscriber {

    private final int entityId;

    private SpawnArea spawnArea;

    SpawnAreaSubscriber(int entityId, SpawnArea spawnArea, PluginLogger logger) {
        super(logger);

        this.entityId = entityId;
        this.spawnArea = spawnArea;
    }

    @Override
    protected Map<Class<? extends Event>, EventHandler<? extends Event>> getHandlersMap() {
        return Map.of(EntityDeathEvent.class, (EventHandler<EntityDeathEvent>) this::onEntityDeath);
    }

    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getNetworkId() == entityId) {
            Entity entity = event.getEntity();
            spawnArea.removeEntity(entity);
        }
    }
}
