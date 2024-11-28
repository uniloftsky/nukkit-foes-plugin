package net.uniloftsky.nukkit.foes.ai.pathfinder;

import cn.nukkit.math.Vector3;

import java.util.Objects;

/**
 * Node is a part of A* pathfinding algorithm.
 * Node is basically a {@link Vector3} with coordinates.
 */
public class Node {

    /**
     * Parent node to build the path using back-tracking
     */
    private Node parent;

    /**
     * Node coordinates
     */
    private final Vector3 position;

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

    Node(Vector3 position) {
        this.position = new Vector3(position.getX(), position.getY(), position.getZ());
    }

    Node(double x, double y, double z) {
        this.position = new Vector3(x, y, z);
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void calculateGCost(Node currentNode) {
        if (this != currentNode) {
            double xDifference = Math.abs(this.getPosition().getX() - currentNode.getPosition().getX());
            double zDifference = Math.abs(this.getPosition().getZ() - currentNode.getPosition().getZ());
            this.gCost = zDifference + xDifference;
        }
    }

    public double getGCost() {
        if (gCost <= 0) {
            throw new IllegalStateException("G-Cost for the Node wasn't calculated!");
        }
        return gCost;
    }

    public void calculateHCost(Node finishNode) {
        if (this != finishNode) {
            double xDifference = Math.abs(finishNode.getPosition().getX() - this.getPosition().getX());
            double zDifference = Math.abs(finishNode.getPosition().getZ() - this.getPosition().getZ());
            this.hCost = zDifference + xDifference;
        }
    }

    public double getHCost() {
        if (hCost <= 0) {
            throw new IllegalStateException("H-Cost for the Node wasn't calculated!");
        }
        return hCost;
    }

    public double getFCost() {
        if (fCost == 0) {
            fCost = gCost + hCost;
        }
        return fCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(position, node.position);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }

    @Override
    public String toString() {
        return "{" +
                "position=" + position +
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                ", fCost=" + fCost +
                '}';
    }
}
