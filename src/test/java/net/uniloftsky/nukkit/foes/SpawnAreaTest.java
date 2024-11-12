package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SpawnAreaTest {

    private final int maxEntitiesSize = 10;
    private final int entityId = 1;

    @Spy
    private List<Position> spawnPoints = new ArrayList<>();

    @Spy
    private List<Position> preparedSpawnPoints = new ArrayList<>();

    @Spy
    private List<Entity> entities = new ArrayList<>();

    @Test
    public void testOnRun() {

        // given

        // when

        // then
    }
}
