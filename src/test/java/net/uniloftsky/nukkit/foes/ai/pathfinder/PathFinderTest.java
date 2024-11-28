package net.uniloftsky.nukkit.foes.ai.pathfinder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderTest {

    @Test
    public void test() {

        // given
        Node startNode = new Node(0, 0, 0);
        Node finishNode = new Node(2, 0, 2);

        // when
        PathFinder pathFinder = new PathFinder(startNode, finishNode);
        List<Node> path = pathFinder.search();

        // then
        Node firstNode = path.getFirst();
        assertEquals(1.0, firstNode.getPosition().getX());
        assertEquals(0.0, firstNode.getPosition().getZ());

        Node secondNode = path.get(1);
        assertEquals(2.0, secondNode.getPosition().getX());
        assertEquals(0.0, secondNode.getPosition().getZ());

        Node thirdNode = path.get(2);
        assertEquals(2.0, thirdNode.getPosition().getX());
        assertEquals(1.0, thirdNode.getPosition().getZ());

        Node fourthNode = path.get(3);
        assertEquals(2.0, fourthNode.getPosition().getX());
        assertEquals(2.0, fourthNode.getPosition().getZ());
    }

}
