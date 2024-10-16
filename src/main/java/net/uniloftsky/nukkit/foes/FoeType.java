package net.uniloftsky.nukkit.foes;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.entity.mob.EntityZombie;

public enum FoeType {

    ZOMBIE(EntityZombie.class), SKELETON(EntitySkeleton.class);

    private final Class<? extends Entity> foeClass;

    FoeType(Class<? extends Entity> foeClass) {
        this.foeClass = foeClass;
    }

    public Class<? extends Entity> getFoeClass() {
        return foeClass;
    }
}
