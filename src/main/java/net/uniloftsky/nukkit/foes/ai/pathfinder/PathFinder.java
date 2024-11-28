package net.uniloftsky.nukkit.foes.ai.pathfinder;

import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Pathfinder class to find a path from one {@link Node} to another.
 * <p>
 * This class is an implementation of <b>A* algorithm</b>.
 * <p>
 * For now, it supports only path finding in <b>two-dimensional</b> space.
 */
public class PathFinder {

    /**
     * Search limit. If algorithm gets into this limit, the path is marked as unreachable
     */
    private static final int SEARCH_LIMIT = 1000;

    /**
     * List with open for processing Nodes
     */
    private final List<Node> openList = new ArrayList<>();

    /**
     * List with closed, processed Nodes
     */
    private final List<Node> closedList = new ArrayList<>();

    /**
     * Start node
     */
    private final Node startNode;

    /**
     * Finish node
     */
    private final Node finishNode;

    /**
     * Current processing node
     */
    private Node currentNode;

    /**
     * Is finish Node reached
     */
    private boolean reached;

    /**
     * Public constructor with parameters.
     *
     * @param startNode  start node
     * @param finishNode finish node
     */
    public PathFinder(Node startNode, Node finishNode) {
        assert startNode != null && finishNode != null;

        this.startNode = startNode;
        this.finishNode = finishNode;
    }

    /**
     * Primary method to begin the search.
     *
     * @return linked list that represents the path to the finish Node
     */
    public List<Node> search() {
        init();

        int attempt = 1;
        while (!reached && attempt < SEARCH_LIMIT) {
            closedList.add(currentNode);
            openList.remove(currentNode);

            openNearbyNodes(currentNode);

            Node bestNode = openList.getFirst();
            double bestFCost = bestNode.getFCost();
            for (Node node : openList) {
                if (node.getFCost() < bestFCost) {
                    bestNode = node;
                    bestFCost = node.getFCost();
                } else if (node.getFCost() == bestFCost) {
                    if (node.getGCost() < node.getGCost()) {
                        bestNode = node;
                    }
                }
            }

            currentNode = bestNode;
            if (currentNode.equals(finishNode)) {
                reached = true;
            }

            attempt++;
        }

        // getting the result path using back-tracking
        List<Node> result = new LinkedList<>();
        if (reached) {
            while (currentNode.getParent() != null) {
                result.addFirst(currentNode);
                currentNode = currentNode.getParent();
            }
        }
        return result;
    }

    /**
     * Open the Nodes near to given Node.
     *
     * @param node Node to open its neighbors
     */
    private void openNearbyNodes(Node node) {
        Vector3 nodePosition = node.getPosition();

        // open the next Node by positive x-axis
        openNode(nodePosition.getX() + 1, nodePosition.getZ(), node);

        // open the next Node by negative x-axis
        openNode(nodePosition.getX() - 1, nodePosition.getZ(), node);

        // open the next Node by positive z-axis
        openNode(nodePosition.getX(), nodePosition.getZ() + 1, node);

        // open the next Node by negative z-axis
        openNode(nodePosition.getX(), nodePosition.getZ() - 1, node);
    }

    /**
     * Open the Node with the given coordinates.
     *
     * @param x x-coordinate
     * @param z z-coordinate
     */
    private void openNode(double x, double z, Node currentNode) {
        Node node = new Node(x, 0, z);
        if (!openList.contains(node) && !closedList.contains(node)) {
            node.setParent(currentNode);

            // calculate G and H costs
            node.calculateGCost(currentNode);
            node.calculateHCost(finishNode);

            openList.add(node);
        }
    }

    /**
     * Search initialization method.
     * Use it for every new search process.
     */
    private void init() {
        openList.clear();
        closedList.clear();

        currentNode = startNode;
        openList.add(currentNode);
    }

}
