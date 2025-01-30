package net.uniloftsky.nukkit.foes.ai.pathfinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Pathfinder class to find a path from one {@link Node} to another.
 * <p>
 * This class is an implementation of <b>A* algorithm</b>.
 */
public class PathFinder {

    /**
     * Search limit. If algorithm gets into this limit, the algorithm stops the search
     */
    private static final int SEARCH_LIMIT = 100000;

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
     * @return linked list that represents the path from the start Node to the finish Node
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
                    if (node.getGCost() < bestNode.getGCost()) {
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
     * @param parentNode Node to open its neighbors
     */
    private void openNearbyNodes(Node parentNode) {

        // open the next Node by positive x-axis
        openNode(parentNode.getX() + 1, parentNode.getY(), parentNode.getZ(), parentNode);

        // open the next Node by negative x-axis
        openNode(parentNode.getX() - 1, parentNode.getY(), parentNode.getZ(), parentNode);

        // open the next Node by positive z-axis
        openNode(parentNode.getX(), parentNode.getY(), parentNode.getZ() + 1, parentNode);

        // open the next Node by negative z-axis
        openNode(parentNode.getX(), parentNode.getY(), parentNode.getZ() - 1, parentNode);

        openNode(parentNode.getX() - 1, parentNode.getY(), parentNode.getZ() - 1, parentNode);

        openNode(parentNode.getX() - 1, parentNode.getY(), parentNode.getZ() + 1, parentNode);

        openNode(parentNode.getX() + 1, parentNode.getY(), parentNode.getZ() - 1, parentNode);

        openNode(parentNode.getX() + 1, parentNode.getY(), parentNode.getZ() + 1, parentNode);
    }

    /**
     * Open the Node with the given coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    private void openNode(int x, int y, int z, Node parentNode) {
        Node node = new Node(x, y, z);
        if (!openList.contains(node) && !closedList.contains(node)) {
            node.setParent(parentNode);

            // calculate G and H costs
            node.calculateGCost(parentNode);
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

    public Node getFinishNode() {
        return finishNode;
    }
}
