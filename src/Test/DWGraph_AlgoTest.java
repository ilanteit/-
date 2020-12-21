package Test;

import static org.junit.jupiter.api.Assertions.*;


import api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DWGraph_AlgoTest {

    private static int _errors = 0, _tests = 0, _number_of_exception = 0;
    private static String _log = "";


    @Test
    public void CopyGraph() {
        directed_weighted_graph g1 = new DWGraph();
        for (int i = 0; i < 20; i++) {
            node_data vertex = new Node__Data(i);
            g1.addNode(vertex);
        }
        dw_graph_algorithms newGraph = new DWGraph_Algo();
        newGraph.init(g1);
        directed_weighted_graph g2 = newGraph.copy();
        assertEquals(g1, g2);
        g2.removeNode(8);
        g2.removeNode(1);
        g2.removeNode(2);
        g1.removeNode(5);
        g2.removeNode(16);
        g2.removeNode(3);
        g2.removeNode(3);
        assertFalse(g1.equals(g2));

        g2.removeNode(5);
        g2.addNode(new Node__Data(3));
        g2.addNode(new Node__Data(16));
        g2.addNode(new Node__Data(8));
        g2.addNode(new Node__Data(1));
        g2.addNode(new Node__Data(2));

        assertEquals(g1, g2);
    }

    @Test

    public void IsConnectedv1() {
        directed_weighted_graph g = new DWGraph();
        for (int i = 0; i < 5; i++) {
            g.addNode(new Node__Data(i));
        }
        g.connect(0,1,10);
        g.connect(0,2,10);
        g.connect(0,3,10);
        g.connect(0,4,10);
        g.connect(0,5,10);

        dw_graph_algorithms newgraph1 = new DWGraph_Algo();
        newgraph1.init(g);
        assertFalse(newgraph1.isConnected());


        g.connect(1,0,10);
        g.connect(2,0,10);
        g.connect(3,0,10);
        g.connect(4,0,10);
        g.connect(5,0,10);

        dw_graph_algorithms newgraph2 = new DWGraph_Algo();
        newgraph2.init(g);
        assertTrue(newgraph2.isConnected());


    }

    @Test
    public void IsConnectedv2(){
        directed_weighted_graph g = new DWGraph();
        for (int i = 0; i < 2; i++) {
            g.addNode(new Node__Data(i));
        }
        g.connect(0,1,10);
        g.connect(0,2,10);
        g.connect(1,0,10);
        g.connect(2,0,10);
        g.connect(1,2,10);
        g.connect(2,1,10);
        dw_graph_algorithms newgraph1 = new DWGraph_Algo();
        newgraph1.init(g);
        assertTrue(newgraph1.isConnected());
        g.removeEdge(0,1);
        newgraph1.init(g);
        assertFalse(newgraph1.isConnected());
        g.removeNode(0);
        newgraph1.init(g);
        assertTrue(newgraph1.isConnected());

    }



    @Test
    @Order(8)
    public void shortestPath() {
        directed_weighted_graph g1 = new DWGraph();
        for (int i = 0; i < 5; i++) {
            g1.addNode(new Node__Data(i));
        }
     g1.connect(0,1 , 100);
        g1.connect(1,2 , 5);
        g1.connect(2,1 , 8);
        g1.connect(2,3 , 9);
        g1.connect(3,2 , 9);
        g1.connect(0,3 , 16);
        g1.connect(3,0 , 22);
        g1.connect(0,4 , 3);
        g1.connect(4,1 , 7);
        g1.connect(0,2 , 3);
        g1.connect(2,0 , 5);

        dw_graph_algorithms newGraph = new DWGraph_Algo();
        newGraph.init(g1);
        List<node_data> path1 = new LinkedList<node_data>();
        List<node_data> pathTrue = new LinkedList<node_data>();
        path1.add(g1.getNode(0));
        path1.add(g1.getNode(4));
        path1.add(g1.getNode(1));
        assertEquals(path1, newGraph.shortestPath(0, 1));
        List<node_data> path2 = new LinkedList<node_data>();
        path2.add(g1.getNode(3));
        path2.add(g1.getNode(2));
        path2.add(g1.getNode(0));
        path2.add(g1.getNode(4));
        assertEquals(path2, newGraph.shortestPath(3, 4));

    }
    @Test
    @Order(9)
    public void shortestpathdist() {
        directed_weighted_graph g1 = new DWGraph();
        for (int i = 0; i < 5; i++) {
            g1.addNode(new Node__Data(i));
        }
        g1.connect(0,1 , 100);
        g1.connect(1,2 , 5);
        g1.connect(2,1 , 8);
        g1.connect(2,3 , 9);
        g1.connect(3,2 , 9);
        g1.connect(0,3 , 16);
        g1.connect(3,0 , 22);
        g1.connect(0,4 , 3);
        g1.connect(4,1 , 7);
        g1.connect(0,2 , 3);
        g1.connect(2,0 , 5);

        dw_graph_algorithms ag = new DWGraph_Algo();
        ag.init(g1);
        assertEquals(10, ag.shortestPathDist(0, 1));
        assertEquals(17, ag.shortestPathDist(3, 4));
        assertEquals(-1, ag.shortestPathDist(1, 5));
    }

    @Test
    @Order(1)
    public void save() {
        directed_weighted_graph g = new DWGraph();
        for (int i = 0; i < 20; i++) {
            g.addNode(new Node__Data(i));
        }
        g.connect(0, 1, 17.5);
        g.connect(2, 3, 10.4);
        g.connect(5, 10, 1);
        dw_graph_algorithms newGraph = new DWGraph_Algo();
        newGraph.init(g);
        String gs = "checkSave";
        assertTrue(newGraph.save(gs));
    }

    @Test
    @Order(2)
    public void load() {
        dw_graph_algorithms newGraph = new DWGraph_Algo();
        assertTrue(newGraph.load("checkSave"));
    }

    @Test
    @Order(10)
    public void graphEqual() {
        directed_weighted_graph g1 = new DWGraph();
        g1.addNode(new Node__Data(5));
        g1.addNode(new Node__Data(6));
        g1.addNode(new Node__Data(10));
        g1.addNode(new Node__Data(15));
        g1.addNode(new Node__Data(55));
        g1.addNode(new Node__Data(12));
        g1.addNode(new Node__Data(10));
        g1.addNode(new Node__Data(10));
        g1.connect(5, 10, 220);
        g1.connect(5, 55, 11);
        g1.connect(12, 15, 1);
        directed_weighted_graph g2 = new DWGraph();
        g2.addNode(new Node__Data(5));
        g2.addNode(new Node__Data(6));
        g2.addNode(new Node__Data(10));
        g2.addNode(new Node__Data(15));
        g2.addNode(new Node__Data(55));
        g2.addNode(new Node__Data(12));
        g2.addNode(new Node__Data(10));
        g2.addNode(new Node__Data(10));
        g2.connect(5, 10, 220);
        g2.connect(5, 55, 11);
        g2.connect(12, 15, 1);


        assertEquals(g1, g2);


    }






}