package net.uniloftsky.nukkit.foes;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.Task;
import net.uniloftsky.nukkit.foes.observer.EventSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnArea extends Task {

    private static final PluginLogger log = FoesPlugin.getInstance().getLogger();

    /**
     * Set of spawn points where foes can spawn
     */
    private final List<Position> spawnPoints;

    /**
     * Max size of spawned foes at the same time
     */
    private final int maxEntitiesSize;

    /**
     * List of alive foes in current spawn area
     */
    private final List<Entity> aliveEntities;

    /**
     * Entity NETWORK_ID value
     */
    private final int entityId;

    /**
     * Constructor to create a spawn area
     *
     * @param entityId        entity NETWORK_ID. Can be retrieved from static NETWORK_ID property of every entity class. For example 'EntityZombie.NETWORK_ID'
     * @param spawnPoints     list of spawn points positions
     * @param maxEntitiesSize max amount of entities alive at once
     */
    public SpawnArea(int entityId, List<Position> spawnPoints, int maxEntitiesSize) {
        this.spawnPoints = new ArrayList<>(spawnPoints);
        this.maxEntitiesSize = maxEntitiesSize;
        this.aliveEntities = new ArrayList<>(maxEntitiesSize);
        this.entityId = entityId;

        new SpawnAreaSubscriber();
    }

    /**
     * Initialize and start the foes spawn area.
     *
     * @param period ticks interval how often spawn area would be processed by scheduler
     */
    @Override
    public void onRun(int period) {
        if (aliveEntities.size() < maxEntitiesSize) {
            Entity entity = createEntity();
            spawnEntity(entity);
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();

        // despawn and clear all the entities on cancel
        for (Entity aliveEntity : aliveEntities) {
            aliveEntity.close();
        }
        aliveEntities.clear();
    }

    private Entity createEntity() {
        Position randomSpawnPosition = getRandomSpawnPosition();
        return Entity.createEntity(entityId, randomSpawnPosition);
    }

    private Position getRandomSpawnPosition() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return spawnPoints.get(random.nextInt(spawnPoints.size()));
    }

    private void spawnEntity(Entity entity) {
        CreatureSpawnEvent spawnEvent = new CreatureSpawnEvent(entity.getNetworkId(), entity.getPosition(), entity.namedTag, CreatureSpawnEvent.SpawnReason.NATURAL);
        Server.getInstance().getPluginManager().callEvent(spawnEvent);
        entity.spawnToAll();
        aliveEntities.add(entity);
    }

    /**
     * Thread-safe method to remove entity from the alive entities list
     *
     * @param entity entity to remove
     */
    private synchronized void removeAliveEntity(Entity entity) {
        aliveEntities.remove(entity);
    }

    /**
     * Events subscriber
     */
    private class SpawnAreaSubscriber extends EventSubscriber {

        private SpawnAreaSubscriber() {
            super(log);

            subscribeOnEvent(EntityDeathEvent.class, this::handleEntityDeathEvent);
        }

        private void handleEntityDeathEvent(EntityDeathEvent event) {
            if (event.getEntity().getNetworkId() == entityId) {
                removeAliveEntity(event.getEntity());
            }
        }
    }
}
