package net.uniloftsky.nukkit.foes;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpawnAreaProcessorTest {

    private final int entityId = 1;

    @Mock
    private SpawnArea spawnArea;

    @InjectMocks
    private SpawnAreaProcessor testSpawnAreaProcessor = new SpawnAreaProcessor(entityId, spawnArea);

    @BeforeEach
    void setUp() {
        testSpawnAreaProcessor = new SpawnAreaProcessor(entityId, spawnArea);
    }

    @Test
    public void testOnRun() {

        // given
        given(spawnArea.isFull()).willReturn(false);

        Position nextSpawnPosition = new Position();
        given(spawnArea.getNextSpawnPosition(any())).willReturn(nextSpawnPosition);

        try (MockedStatic<Entity> mockedStaticEntity = mockStatic(Entity.class);
             MockedStatic<Server> mockedStaticServer = mockStatic(Server.class)) {

            // mocking created entity
            Entity createdEntity = mock(Entity.class);
            mockedStaticEntity.when(() -> Entity.createEntity(entityId, nextSpawnPosition)).thenReturn(createdEntity);

            // mocking server to return mocked plugin manager
            Server server = mock(Server.class);
            mockedStaticServer.when(Server::getInstance).thenReturn(server);
            PluginManager pluginManager = mock(PluginManager.class);
            given(server.getPluginManager()).willReturn(pluginManager);

            // when
            int period = 100;
            testSpawnAreaProcessor.onRun(period);

            // then
            then(createdEntity).should().spawnToAll();
            then(pluginManager).should().callEvent(argThat(e -> e instanceof CreatureSpawnEvent));
            then(spawnArea).should().addEntity(createdEntity);
        }
    }

    @Test
    public void testOnRunSpawnAreaFull() {

        // given
        given(spawnArea.isFull()).willReturn(true);

        // when
        int period = 100;
        testSpawnAreaProcessor.onRun(period);

        // then
        then(spawnArea).should(times(0)).getNextSpawnPosition(any());
    }

    @Test
    public void testOnCancel() {

        // given
        Entity entity = mock(Entity.class);
        List<Entity> entities = List.of(entity);
        given(spawnArea.getEntities()).willReturn(entities);

        // when
        testSpawnAreaProcessor.onCancel();

        // then
        then(entity).should().close();
        then(spawnArea).should().removeEntity(entity);
    }
}
