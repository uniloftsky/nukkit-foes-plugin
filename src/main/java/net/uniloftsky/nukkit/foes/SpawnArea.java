package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Spawn area to store and manage alive entities.
 */
public class SpawnArea {

    /**
     * Limit for the safety counter. Safety counter is used by choosing the next spawn position
     */
    private static final int SAFETY_COUNTER_LIMIT = 10;

    /**
     * Set of spawn points where foes can spawn
     */
    private List<Position> spawnPoints;

    /**
     * Index of the last chosen spawn position
     */
    private int lastUsedPositionIndex;

    /**
     * Max size of spawned foes at the same time
     */
    private final int maxEntitiesSize;

    /**
     * List of alive foes in current spawn area
     */
    private List<Entity> aliveEntities;

    /**
     * Constructor to create a spawn area
     *
     * @param spawnPoints     list of spawn points positions
     * @param maxEntitiesSize max amount of entities alive at once
     */
    public SpawnArea(List<Position> spawnPoints, int maxEntitiesSize) {
        this.spawnPoints = new ArrayList<>(spawnPoints);
        this.maxEntitiesSize = maxEntitiesSize;
        this.aliveEntities = new ArrayList<>(maxEntitiesSize);
    }

    /**
     * Get next spawn position for an entity.
     * <p>
     * The spawn position is chosen randomly from the provided list of spawn points.
     *
     * @return next spawn position for an entity
     */
    public Position getNextSpawnPosition() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int spawnPositionIndex = random.nextInt(spawnPoints.size());

        int safetyCounter = 1;
        while (spawnPositionIndex == lastUsedPositionIndex && safetyCounter < SAFETY_COUNTER_LIMIT) {
            spawnPositionIndex = random.nextInt(spawnPoints.size());
            safetyCounter++;
        }

        lastUsedPositionIndex = spawnPositionIndex;
        return spawnPoints.get(spawnPositionIndex);
    }

    /**
     * Add an entity to the alive entities list. Method internally prevents from adding duplicate entities.
     *
     * @param entity entity to add
     * @throws IllegalStateException if spawn area is full
     */
    public void addEntity(Entity entity) {
        if (isFull()) {
            throw new IllegalStateException("Spawn area limit is already exceeded");
        }
        if (!aliveEntities.contains(entity)) {
            aliveEntities.add(entity);
        }
    }

    /**
     * Remove an entity from the alive entities list
     *
     * @param entity entity to remove
     */
    public void removeEntity(Entity entity) {
        aliveEntities.remove(entity);
    }

    /**
     * Is spawn area already full and cannot accept more entities
     *
     * @return true if full, false if not
     */
    public boolean isFull() {
        return aliveEntities.size() == maxEntitiesSize;
    }

    /**
     * Get the list of alive entities
     *
     * @return list of entities
     */
    public List<Entity> getEntities() {
        return new ArrayList<>(aliveEntities);
    }
}
