package net.uniloftsky.nukkit.foes;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import net.uniloftsky.nukkit.foes.listener.entity.EntityEventPublisher;
import net.uniloftsky.nukkit.foes.listener.entity.EntityEventSubscriber;
import net.uniloftsky.nukkit.foes.listener.entity.EntityEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FoesSpawnArea<T extends Entity> extends Task implements EntityEventSubscriber {

    private static final FoesPlugin PLUGIN = FoesPlugin.getInstance();

    private static final String ENTITY_ID_FIELD = "_id";

    /**
     * Set of spawn points where foes can be spawned
     */
    private List<Position> spawnPoints;

    /**
     * Max size of spawned foes at the same time
     */
    private int maxEntitiesSize;

    /**
     * List of alive foes in current spawn area
     */
    private List<Entity> aliveEntities;

    /**
     * Type of foe
     */
    private FoeType foeType;

    public FoesSpawnArea(List<Position> spawnPoints, int maxEntitiesSize, FoeType foeType) {
        this.spawnPoints = new ArrayList<>(spawnPoints);
        this.maxEntitiesSize = maxEntitiesSize;
        this.aliveEntities = new ArrayList<>(maxEntitiesSize);
        this.foeType = foeType;

        EntityEventPublisher.subscribe(this, EntityEventType.ENTITY_KILLED, EntityEventType.ENTITY_REMOVED);
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
            aliveEntities.add(entity); // add entity to the list of alive entities
        }
    }

    private Entity createEntity() {
        Position randomSpawnPosition = getRandomSpawnPosition();
        return Entity.createEntity("Zombie", randomSpawnPosition);
    }

    private Position getRandomSpawnPosition() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return spawnPoints.get(random.nextInt(spawnPoints.size()));
    }

    private void spawnEntity(Entity entity) {
        CreatureSpawnEvent spawnEvent = new CreatureSpawnEvent(entity.getNetworkId(), entity.getPosition(), entity.namedTag, CreatureSpawnEvent.SpawnReason.NATURAL);
        Server.getInstance().getPluginManager().callEvent(spawnEvent);
        entity.spawnToAll();
    }

    @Override
    public void handleEvent(EntityEventType eventType, Entity entity) {
        switch (eventType) {
            case ENTITY_KILLED:
            case ENTITY_REMOVED:
                aliveEntities.remove(entity);
        }
    }
}
