package net.uniloftsky.nukkit.foes.ai.pathfinder;

import cn.nukkit.math.Vector3;

import java.util.Objects;

/**
 * Node is a part of A* pathfinding algorithm.
 * The Node object contains its coordinates, G-Cost, H-Cost and F-Cost.
 */
public class Node {

    /**
     * Parent node to build the result path using back-tracking
     */
    private Node parent;

    private int x;

    private int y;

    private int z;

    /**
     * Node coordinates
     */
    private Vector3 position;

    /**
     * G-Cost is a path cost from the <b>current processing</b> Node to <b>this</b> Node
     */
    private double gCost;

    /**
     * H-Cost is a path cost from <b>this</b> Node to the <b>finish</b> Node
     */
    private double hCost;

    /**
     * F-Cost is a sum of G-Cost and H-Cost
     */
    private double fCost;

    public Node(Vector3 position) {
        this.position = new Vector3(position.getX(), position.getY(), position.getZ());
    }

    public Node(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get Node position/coordinates.
     *
     * @return {@link Vector3}
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Get Node G-Cost.
     *
     * @return G-Cost of the node
     * @throws IllegalStateException if G-cost of the Node wasn't calculated yet
     */
    public double getGCost() {
        if (gCost <= 0) {
            throw new IllegalStateException("G-Cost for the Node wasn't calculated!");
        }
        return gCost;
    }

    /**
     * Get Node F-Cost.
     *
     * @return F-Cost of the Node
     */
    public double getFCost() {
        if (fCost == 0) {
            fCost = gCost + hCost;
        }
        return fCost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }

    void calculateHCost(Node finishNode) {
        if (this != finishNode) {
            double xDifference = Math.abs(finishNode.getX() - this.getX());
            double zDifference = Math.abs(finishNode.getZ() - this.getZ());
            this.hCost = Math.min(xDifference, zDifference) * Math.sqrt(2) + Math.abs(xDifference - zDifference);
        }
    }

    void calculateGCost(Node currentNode) {
        if (this != currentNode) {
            double xDifference = Math.abs(this.getX() - currentNode.getX());
            double zDifference = Math.abs(this.getZ() - currentNode.getZ());
            this.gCost = Math.min(xDifference, zDifference) * Math.sqrt(2) + Math.abs(xDifference - zDifference);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y && z == node.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                ", fCost=" + fCost +
                '}';
    }
}
