package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.plugin.PluginLogger;
import net.uniloftsky.nukkit.foes.observer.EventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SpawnAreaEventSubscriberTest {

    private final int entityId = 1;

    @Mock
    private PluginLogger logger;

    @Mock
    private SpawnArea spawnArea;

    @InjectMocks
    private SpawnAreaEventSubscriber testSpawnAreaEventSubscriber = new SpawnAreaEventSubscriber(entityId, spawnArea, logger);

    @Test
    public void testGetHandlersMap() {
        Map<Class<? extends Event>, EventHandler<? extends Event>> result = testSpawnAreaEventSubscriber.getHandlersMap();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testOnEntityDeath() {

        // given
        EntityDeathEvent event = mock(EntityDeathEvent.class);
        Entity entity = mock(Entity.class);
        given(entity.getNetworkId()).willReturn(entityId);
        given(event.getEntity()).willReturn(entity);

        // when
        testSpawnAreaEventSubscriber.onEntityDeath(event);

        // then
        then(spawnArea).should().removeEntity(entity);
    }

    // wrong entity means that entity network id is different
    @Test
    public void testOnEntityDeathWrongEntity() {

        // given
        EntityDeathEvent event = mock(EntityDeathEvent.class);
        Entity entity = mock(Entity.class);
        given(entity.getNetworkId()).willReturn(Integer.MAX_VALUE); // dummy network id
        given(event.getEntity()).willReturn(entity);

        // when
        testSpawnAreaEventSubscriber.onEntityDeath(event);

        // then
        then(spawnArea).should(times(0)).removeEntity(entity);
    }
}
