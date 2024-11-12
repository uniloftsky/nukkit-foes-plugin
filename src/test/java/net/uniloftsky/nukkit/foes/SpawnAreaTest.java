package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.math.NukkitRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpawnAreaTest {

    private final int maxEntitiesSize = 5;

    @Spy
    private List<Position> spawnPositions = new ArrayList<>();

    @Spy
    private List<Entity> aliveEntities = new ArrayList<>();

    @Spy
    @InjectMocks
    private SpawnArea testSpawnArea = new SpawnArea(spawnPositions, maxEntitiesSize);

    @BeforeEach
    void setUp() {
        spawnPositions.clear();
        aliveEntities.clear();

        spawnPositions.add(new Position(0, 0));
        spawnPositions.add(new Position(0, 1));
    }

    /**
     * The purpose of the getNextSpawnPosition() method is to ensure that the same index is not returned consecutively.
     * This test is critically important to verify that the result of the first call to getNextSpawnPosition() is not equal to the result of the second call.
     */
    @Test
    public void testGetNextSpawnPosition() {

        // given
        NukkitRandom mockedRandom = mock(NukkitRandom.class);
        int firstIndex = spawnPositions.size() - 1;
        int secondIndex = 0;
        given(mockedRandom.nextRange(0, spawnPositions.size() - 1)).willReturn(firstIndex, secondIndex);

        // when first time
        Position result = testSpawnArea.getNextSpawnPosition(mockedRandom);

        // then first time
        assertNotNull(result);
        assertEquals(spawnPositions.getLast(), result);

        // when second time
        Position secondResult = testSpawnArea.getNextSpawnPosition(mockedRandom);

        // then second time
        assertNotNull(secondResult);
        assertEquals(spawnPositions.getFirst(), secondResult);

        assertNotEquals(result, secondResult);
    }

    @Test
    public void testAddEntity() {

        // given
        Entity entity = mock(Entity.class);

        // when first time
        testSpawnArea.addEntity(entity);

        // then first time
        assertEquals(1, aliveEntities.size());

        // when second time with the same entity
        testSpawnArea.addEntity(entity);

        // then second time
        // test that duplicate wasn't added to the alive entities list
        assertEquals(1, aliveEntities.size());
    }

    @Test
    public void testAddEntityFull() {

        // given
        Entity entity = mock(Entity.class);

        // simulate that spawn area is full
        doReturn(true).when(testSpawnArea).isFull();

        try {

            // when
            testSpawnArea.addEntity(entity);
        } catch (IllegalStateException e) {

            // then
            assertNotNull(e);
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testRemoveEntity() {

        // given
        Entity entity = mock(Entity.class);

        // when
        testSpawnArea.removeEntity(entity);

        // then
        then(aliveEntities).should(times(1)).remove(entity);
    }

    @Test
    public void testIsFull() {

        // given
        given(aliveEntities.size()).willReturn(maxEntitiesSize);

        // when
        boolean result = testSpawnArea.isFull();

        // then
        assertTrue(result);
    }

    @Test
    public void testIsNotFull() {

        // given
        given(aliveEntities.size()).willReturn(0);

        // when
        boolean result = testSpawnArea.isFull();

        // then
        assertFalse(result);
    }

    @Test
    public void testGetEntities() {

        // given
        Entity entity = mock(Entity.class);
        aliveEntities.add(entity);

        // when
        List<Entity> result = testSpawnArea.getEntities();

        // then
        assertNotEquals(aliveEntities, result); // should be not the same because of defensive copy
        assertEquals(aliveEntities.size(), result.size());
        assertEquals(entity, result.getFirst()); // but the entity inside should equal
    }

}
