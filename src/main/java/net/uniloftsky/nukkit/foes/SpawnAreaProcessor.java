package net.uniloftsky.nukkit.foes;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.level.Position;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.Task;
import net.uniloftsky.nukkit.foes.observer.EventSubscriber;

public class SpawnAreaProcessor extends Task {

    private static final PluginLogger logger = FoesPlugin.getInstance().getLogger();

    /**
     * Spawn area
     */
    private SpawnArea spawnArea;

    /**
     * Nukkit random object
     */
    private NukkitRandom random;

    /**
     * Entity NETWORK_ID value
     */
    private final int entityId;

    /**
     * Public constructor to create a spawn area processor
     *
     * @param entityId  entity NETWORK_ID. Can be retrieved from static NETWORK_ID property of every entity class. For example 'EntityZombie.NETWORK_ID'
     * @param spawnArea spawn area {@link SpawnArea}
     */
    public SpawnAreaProcessor(int entityId, SpawnArea spawnArea) {
        this.entityId = entityId;
        this.spawnArea = spawnArea;
        this.random = new NukkitRandom();

        new SpawnAreaProcessorSubscriber();
    }

    /**
     * Initialize and start the foes spawn area.
     *
     * @param period ticks interval how often spawn area would be processed by scheduler
     */
    @Override
    public void onRun(int period) {
        if (!spawnArea.isFull()) {
            Position nextSpawnPosition = spawnArea.getNextSpawnPosition(random);
            Entity entity = Entity.createEntity(entityId, nextSpawnPosition);
            spawnEntity(entity);
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();

        // despawn and clear all the entities on cancel
        for (Entity aliveEntity : spawnArea.getEntities()) {
            aliveEntity.close();
            spawnArea.removeEntity(aliveEntity);
        }
    }

    public void spawnEntity(Entity entity) {
        CreatureSpawnEvent spawnEvent = new CreatureSpawnEvent(entity.getNetworkId(), entity.getPosition(), entity.namedTag, CreatureSpawnEvent.SpawnReason.NATURAL);
        Server.getInstance().getPluginManager().callEvent(spawnEvent);
        entity.spawnToAll();
        spawnArea.addEntity(entity);
    }

    /**
     * Events subscriber
     */
    private class SpawnAreaProcessorSubscriber extends EventSubscriber {

        private SpawnAreaProcessorSubscriber() {
            super(logger);

            subscribeOnEvent(EntityDeathEvent.class, this::handleEntityDeathEvent);
        }

        private void handleEntityDeathEvent(EntityDeathEvent event) {
            if (event.getEntity().getNetworkId() == entityId) {
                spawnArea.removeEntity(event.getEntity());
            }
        }
    }
}
