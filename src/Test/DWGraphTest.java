package Test;

import api.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraphTest {


    @Test
    public void EmptyGraph() {
        directed_weighted_graph g = new DWGraph();
        assertEquals(null, g.getNode(0));
    }
    @Test
    public void addNodeV1() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(1));
        g.addNode(new Node__Data(2));
        assertEquals(0, g.getNode(0).getKey());
        assertEquals(2, g.getNode(2).getKey());
    }

    @Test
    public void addNodeV2() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(1));
        g.addNode(new Node__Data(2));
        g.addNode(new Node__Data(1));
        assertEquals(0, g.getNode(0).getKey());
        assertEquals(3, g.nodeSize());
    }



    @Test
    public void connectV1() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(1));
        g.addNode(new Node__Data(2));
        g.addNode(new Node__Data(4));
        g.connect(0, 1, 10);
        g.connect(0, 2, 15);
        g.connect(0, 4, 20);
        assertEquals(10, g.getEdge(0, 1).getWeight());
        assertEquals(20, g.getEdge(0, 4).getWeight());
        assertEquals(g.edgeSize(),3);
    }

    @Test
    public void connectV2() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(1));
        g.addNode(new Node__Data(2));
        g.connect(0, 1, 50);
        g.connect(0, 1, 50);
        g.connect(2, 2, 50);
        System.out.println(g.getE(0));
        assertEquals(1, g.edgeSize());
        assertEquals(null, g.getEdge(2, 2));
    }
    @Test
    public void removeNodev1() {
        directed_weighted_graph graph = new DWGraph();
        for (int i = 0; i < 1000; i++) {
            graph.addNode(new Node__Data(i));
        }
        ConnectAll(graph);
        graph.removeNode(0);
        for (int i = 0; i < 1000; i++) {
            graph.removeNode(i);
        }
    }
    private void ConnectAll(directed_weighted_graph graph) {
        for (int i = 0; i < graph.nodeSize(); i++) {
            for (int j = 0; j < graph.nodeSize(); j++) {
                graph.connect(i, j, 5.5);
            }
        }
    }


    @Test
    public void removeEdge() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(1));
        g.addNode(new Node__Data(2));
        g.connect(0, 1, 10);
        assertEquals(10, g.removeEdge(0, 1).getWeight());
        assertEquals(null, g.removeEdge(0, 2));
    }

    @Test
    public void removeEdgebothways() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        g.addNode(new Node__Data(1));
        g.connect(0, 1, 15);
        g.connect(1, 0, 20);
        assertEquals(2, g.edgeSize());
        g.removeEdge(0, 1);
        assertNotEquals(null, g.getEdge(1, 0));
        assertEquals(null, g.getEdge(0, 1));

    }

    @Test
    public void getE() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        for (int i = 1; i < 1000000; i++) {
            g.addNode(new Node__Data(i));
            g.connect(0, i, i);
        }
        boolean[] b = new boolean[1000000];
        Collection<edge_data> col = g.getE(0);
        int dest = 0;
        for (edge_data d : col) {
            dest = d.getDest();
            b[dest] = !b[dest];
        }
        for (int i = 1; i < b.length; i++) {
            if (!b[i]) {
                System.out.println(i);
                fail();
            }
        }
        assertEquals(1000000, col.size() + 1);
    }

    @Test
    public void removeNodev2() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        for (int i = 1; i < 10; i++) {
            g.addNode(new Node__Data(i));
            g.connect(0, i, i);
        }
        g.removeNode(0);
        for (int i = 1; i < 10; i++) {
            if (g.getEdge(0, i) != null) {

                System.out.println(i);
                fail();
            }
        }
       // System.out.println( g.getNode(0));
        assertEquals(null, g.getNode(0));
    }

    @Test
    public void removeNodev3() {
        directed_weighted_graph g = new DWGraph();
        g.addNode(new Node__Data(0));
        for (int i = 1; i < 1000000; i++) {
            g.addNode(new Node__Data(i));
            g.connect(0, i, i);
            g.connect(i, 0, i);

        }

        g.removeNode(0);
        for (int i = 1; i < 1000000; i++) {
            if (g.getEdge(0, i) != null || g.getEdge(i, 0) != null) {
                System.out.println(i);
                fail();
            }
        }
        assertEquals(null, g.getNode(0));
    }
}