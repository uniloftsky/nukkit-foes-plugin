package net.uniloftsky.nukkit.foes.ai.pathfinder;

import org.junit.jupiter.api.Test;

import java.util.List;

public class PathFinderTest {

    @Test
    public void test() {

        // given
        Node startNode = new Node(0, 0, 0);
        Node finishNode = new Node(2, 0, 2);

        // when
        PathFinder pathFinder = new PathFinder(startNode, finishNode);
        List<Node> path = pathFinder.search();

        System.out.println();

        // then
        /*Node firstNode = path.getFirst();
        assertEquals(1.0, firstNode.getX());
        assertEquals(0.0, firstNode.getZ());

        Node secondNode = path.get(1);
        assertEquals(2.0, secondNode.getX());
        assertEquals(0.0, secondNode.getZ());

        Node thirdNode = path.get(2);
        assertEquals(2.0, thirdNode.getX());
        assertEquals(1.0, thirdNode.getZ());

        Node fourthNode = path.get(3);
        assertEquals(2.0, fourthNode.getX());
        assertEquals(2.0, fourthNode.getZ());*/
    }

    @Test
    public void secondTest() {

        // given
        Node startNode = new Node(59, 86, 243);
        Node finishNode = new Node(70, 86, 256);

        // when
        PathFinder pathFinder = new PathFinder(startNode, finishNode);
        List<Node> path = pathFinder.search();

        System.out.println("Path: " + path);
        for (Node node : path) {
            System.out.printf("x: %d, z: %d", node.getX(), node.getZ());
        }
    }

}
