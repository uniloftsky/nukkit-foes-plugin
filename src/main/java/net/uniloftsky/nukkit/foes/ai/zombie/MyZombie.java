package net.uniloftsky.nukkit.foes.ai.zombie;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public class MyZombie extends EntityZombie {

    public static final int NETWORK_ID = 32;

    public static final String NETWORK_ID_STRING = String.valueOf(NETWORK_ID);

    /**
     * Default speed. Measured in blocks per tick.
     * <p>
     * <code>0.1f</code> represents <code>0.1</code> block per tick which means <code>2</code> blocks per second <code>(0.1 * 20 Ticks)</code>
     */
    public static final float DEFAULT_SPEED = 0.1f;

    private boolean movement = true;
    protected Vector3 target = new Vector3(67, 86, 260);
    private boolean friendly;
    protected Entity followTarget;

    public MyZombie(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public void setFriendly(boolean bool) {
        this.friendly = bool;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }

        int tickDiff = currentTick - this.lastUpdate;
        this.lastUpdate = currentTick;
        this.entityBaseTick(tickDiff);

        this.updateMove(tickDiff);
        return true;
    }

    public Vector3 updateMove(int tickDiff) {
        if (movement) {
            Vector3 target = this.target;
            double deltaX = target.x - this.x;
            double deltaY = target.z - this.z;
            double summ = Math.abs(deltaX) + Math.abs(deltaY);

            this.motionX = DEFAULT_SPEED * (deltaX / summ);
            this.motionZ = DEFAULT_SPEED * (deltaY / summ);

            this.move(this.motionX, this.motionY, this.motionY);
            this.yaw = calculateYaw(target.x, target.z);
            this.updateMovement();
            if (this.x >= target.x) {
                movement = false;
            }
        }
        return null;
    }

    public int nearbyDistanceMultiplier() {
        return 1;
    }

    public void setMovement(boolean value) {
        this.movement = value;
    }

    public boolean isMovement() {
        return this.movement;
    }

    public boolean isKnockback() {
        return this.knockBackTime > 0;
    }

    public boolean canTarget(Entity entity) {
        return entity instanceof Player;
    }

    public boolean isFriendly() {
        return this.friendly;
    }

    public Vector3 getTargetVector() {
        if (this.followTarget != null) {
            return this.followTarget;
        } else if (this.target instanceof Entity) {
            return this.target;
        } else {
            return null;
        }
    }

    /**
     * Method to calculate entity yaw depending on the target <code>x</code> and target <code>z</code> coordinates.
     * Use this method to rotate the entity's look at the target direction.
     * <p>
     * Yaw is calculated based on <code>atan2</code> function.
     *
     * @param targetX target <code>x</code> coordinate
     * @param targetZ target <code>z</code> coordinate
     * @return calculated yaw value
     */
    private double calculateYaw(double targetX, double targetZ) {

        /*
        Delta x should be negative because Minecraft's yaw system is different from the typical Cartesian coordinate system.
        In Minecraft the positive x-axis corresponds  -90 degrees while in the default system the x-axis corresponds to 0 degrees.
        */
        double deltaX = -(targetX - this.x);
        double deltaZ = targetZ - this.z;
        double yawInRadians = Math.atan2(deltaZ, deltaX);
        return -Math.toDegrees(yawInRadians) + 90;
    }
}
