package net.uniloftsky.nukkit.foes.ai.zombie;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.CompoundTag;
import net.uniloftsky.nukkit.foes.EntityConfig;

import java.util.concurrent.atomic.AtomicLong;

public class MyZombieBuilder {

    /**
     * Accumulator for created amount of entities
     */
    private AtomicLong entitiesCount;

    public MyZombieBuilder() {
        this.entitiesCount = new AtomicLong();
    }

    public Entity build(String name, Position position) {
        CompoundTag nbt = Entity.getDefaultNBT(position);
        nbt.putFloat(EntityConfig.SCALE.value, 1f);
        nbt.putString(EntityConfig.CUSTOM_NAME.value, name);
        nbt.putBoolean(EntityConfig.CUSTOM_NAME_ALWAYS_VISIBLE.value, true);

        Entity result = Entity.createEntity(MyZombie.NETWORK_ID_STRING, position.getChunk(), nbt);
        entitiesCount.incrementAndGet();
        System.out.println("Number of created entities:" + getCreatedEntitiesCount());
        return result;
    }

    public long getCreatedEntitiesCount() {
        return entitiesCount.get();
    }

}
