package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.entity.mob.EntityZombie;

public enum FoeType {

    ZOMBIE(EntityZombie.NETWORK_ID), SKELETON(EntitySkeleton.NETWORK_ID);

    private Class<? extends Entity> foeClass;

    private final int networkId;

    FoeType(int networkId) {
        this.networkId = networkId;
    }

    public int getNetworkId() {
        return networkId;
    }
}
