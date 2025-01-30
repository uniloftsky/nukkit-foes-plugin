package net.uniloftsky.nukkit.foes.ai.zombie;

import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.ListTag;
import net.uniloftsky.nukkit.foes.ai.pathfinder.Node;
import net.uniloftsky.nukkit.foes.ai.pathfinder.PathFinder;

import java.util.Iterator;
import java.util.List;

public class MyZombie extends EntityZombie {

    public static final int NETWORK_ID = 32;

    public static final String NETWORK_ID_STRING = String.valueOf(NETWORK_ID);

    /**
     * Default speed. Measured in blocks per tick.
     * <p>
     * <code>0.1f</code> represents <code>0.1</code> block per tick which means <code>2</code> blocks per second <code>(0.1 * 20 Ticks)</code>
     */
    public static final float DEFAULT_SPEED = 0.1f;

    private PathFinder pathFinder;
    private List<Node> path;
    private Iterator<Node> pathIterator;
    private boolean movement = true;
    protected Vector3 target;

    public MyZombie(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        ListTag<DoubleTag> position = nbt.getList("Pos", DoubleTag.class);
        Double x = position.get(0).getData();
        Double y = position.get(1).getData();
        Double z = position.get(2).getData();
        Node startNode = new Node(x.intValue(), y.intValue(), z.intValue());
        Node finishNode = new Node(70, 86, 256);

        this.pathFinder = new PathFinder(startNode, finishNode);
        this.path = pathFinder.search();

        this.pathIterator = this.path.iterator();
        this.target = new Vector3(x, y, z);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
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
            if ((this.x >= target.x && this.x <= target.x + 1) && (this.z >= target.z && this.z <= target.z + 1) && this.pathIterator.hasNext()) {
                Node node = this.pathIterator.next();
                this.target = new Vector3(node.getX(), node.getY(), node.getZ());
            }
            double deltaX = target.x + 0.5 - this.x;
            double deltaY = target.z + 0.5 - this.z;
            double summ = Math.abs(deltaX) + Math.abs(deltaY);

            this.motionX = DEFAULT_SPEED * (deltaX / summ);
            this.motionZ = DEFAULT_SPEED * (deltaY / summ);

            this.move(this.motionX, this.motionY, this.motionZ);
            this.yaw = calculateYaw(target.x + 0.5, target.z + 0.5);
            this.updateMovement();
            if ((this.x >= pathFinder.getFinishNode().getX() && this.x <= pathFinder.getFinishNode().getX() + 1)
                    && (this.z >= pathFinder.getFinishNode().getZ() && this.z <= pathFinder.getFinishNode().getZ() + 1)) {
                movement = false;
            }
        }
        return null;
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
        In Minecraft the positive x-axis corresponds -90 degrees while in the default system the x-axis corresponds to 0 degrees.
        */
        double deltaX = -(targetX - this.x);
        double deltaZ = targetZ - this.z;
        double yawInRadians = Math.atan2(deltaZ, deltaX);
        return -Math.toDegrees(yawInRadians) + 90; // converting the whole result to Minecraft's yaw system
    }
}
