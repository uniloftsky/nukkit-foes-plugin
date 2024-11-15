package net.uniloftsky.nukkit.foes.ai.zombie;

import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class MyZombie extends EntityZombie {

    public static final int NETWORK_ID = 32;
    public static final String NETWORK_ID_STRING = String.valueOf(NETWORK_ID);

    public MyZombie(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
}
