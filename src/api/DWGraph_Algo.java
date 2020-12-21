package api;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;



public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph dwg = new DWGraph();

    private Map<node_data, Double> distance = new HashMap<>(); //hashmap of the distance between nodes

    public DWGraph_Algo(directed_weighted_graph graph) {
        init(graph);
    }

    public DWGraph_Algo() {
        this.distance = new HashMap<>();
        this.dwg = new DWGraph();
    }


    @Override
    public void init(directed_weighted_graph g) {
        this.dwg = g;


    }

    @Override
    public directed_weighted_graph getGraph() {
        return dwg;
    }

    @Override
    public directed_weighted_graph copy() {


        directed_weighted_graph copyGraph = new DWGraph();
        Collection<node_data> allnodes = dwg.getV();
        if (allnodes.isEmpty()) //copies an empty graph
            return copyGraph;

        if (this.dwg != null) {

            for (node_data node : allnodes) { //going through all the nodes of the graph
                copyGraph.addNode(new Node__Data(node));
                Collection<edge_data> edges = dwg.getE(node.getKey()); //going through all the edges that the node has
                for (edge_data edge : edges) {
                    copyGraph.addNode(new Node__Data(dwg.getNode(edge.getDest())));
                    copyGraph.connect(edge.getSrc(), edge.getDest(), edge.getWeight()); //connecting all the edges that the original graph has.
                }

            }

        }
        return copyGraph;

    }

    @Override
    public boolean isConnected() {

        reset(); //resseting the tags
        LinkedList<node_data> vertices = new LinkedList<>(dwg.getV());
        BFS(dwg, vertices.getFirst().getKey()); //checking the first node of the graph threw bfs and changing the tags
        for (node_data V : dwg.getV()) {
            if (V.getTag() == -1) {
                return false;
            }
        }
        reset();
        directed_weighted_graph ReverseGraph = ReverseCopy(); //reversing the graph and checking for stronger conectivity

        BFS(ReverseGraph, vertices.getFirst().getKey());//checking the first node of the reversed graph through bfs
        for (node_data V : dwg.getV()) {
            if (V.getTag() == -1) {
                return false;
            }
        }


        return true;
    }


    public directed_weighted_graph ReverseCopy() {   //using this function to check if the graph isconnected "קשירות חזקה"
        directed_weighted_graph reverse = new DWGraph();
        if (this.dwg != null) {

            for (node_data node : dwg.getV()) { //going through all the nodes of the graph
                reverse.addNode(node);
                Collection<edge_data> edges = dwg.getE(node.getKey()); //going through all the edges that the node has
                for (edge_data edge : edges) {
                    reverse.addNode(dwg.getNode(edge.getDest()));
                    reverse.connect(edge.getDest(), edge.getSrc(), edge.getWeight()); //connecting all the edges that the original graph has.
                }

            }

        }
        return reverse;
    }


    void BFS(directed_weighted_graph g, int s) {
        // Mark all the vertices as not visited(By default
        // set as false)
        if (g.getNode(s) == null) return;
        int size = g.nodeSize();
        if (size == 0) return; //

        node_data first = dwg.getNode(s);

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        first.setTag(1); //visit
        queue.add(s);  // enqueue it to the tor
        // the tor isnt empty

        while (queue.size() != 0) {
            // Dequeue a vertex from queue and print it
            s = queue.poll();
            node_data sNode = g.getNode(s);


            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            Collection<edge_data> NiS = g.getE(s);
            if (NiS != null)

                for (edge_data E : NiS) {
                    node_data dest = g.getNode(E.getDest());
                    if (dest != null) {
                        if (dest.getTag() == -1) {
                            dest.setTag(1); //mark visit

                            queue.add(dest.getKey());
                        }
                    }
                } //end for nei

        }// end while


    } //end function


    private void reset() {
        for (node_data i : dwg.getV()) {
            i.setTag(-1); //unvisited
        }
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        if (dwg.getNode(src) != null && dwg.getNode(dest) != null) {

            if (src == dest) return 0;
            // all the nodes set to infinty
            for (node_data i : dwg.getV()) {
                i.setWeight(Double.POSITIVE_INFINITY);

            }
            node_data Nsrc = dwg.getNode(src);
            Nsrc.setWeight(0.);
            int keyS = Nsrc.getKey();
            Nsrc.setInfo("" + keyS); // info for know the path from src -> dest
            // Create Tor for dijksta algorithem
            LinkedList<node_data> Q = new LinkedList<node_data>();
            //add all nodes to the queue
            for (node_data i : dwg.getV()) {
                Q.add(i);
            }
            // create comperator
            comp C = new comp();

            while (!Q.isEmpty()) {
                Q.sort(C);
                node_data u = Q.poll();
                //neighbor of u
                for (edge_data nei : dwg.getE(u.getKey())) {
                    //compute the distance to nei- until u+ u -> v
                    node_data newNei = dwg.getNode(nei.getDest());
                    double alt = u.getWeight() + nei.getWeight(); //the weight until now + current wight on the edge
                    // find the min weight path to current Node
                    if (alt < newNei.getWeight()) {
                        newNei.setWeight(alt);
                        newNei.setInfo(u.getInfo() + " " + newNei.getKey()); // add to the info of path curr node key
                    }
                }

            }
            //	System.out.println(graphAlgo.getNode(dest).getInfo());
            return dwg.getNode(dest).getWeight();    // return the weight of the dest node

        }
        return -1;
    }


    @Override
    public List<node_data> shortestPath(int src, int dest) {
        double w = shortestPathDist(src, dest); //call to function find min weight path
        if (w == -1) return null; // no path to their
        // create a list to add the node_info
        LinkedList<node_data> list = new LinkedList<node_data>();

        String path = dwg.getNode(dest).getInfo(); // now the info is a String numbers
        // split space and add to the arr
        String[] splited = path.split("\\s+");

        for (int i = 0; i < splited.length; i++) {
            int key = Integer.parseInt(splited[i]);
            node_data nod = dwg.getNode(key);
            list.add(nod);
        }
        return list;
    }


    @Override
    public boolean save(String file) {


        JSONObject Jobj = new JSONObject();
        JSONArray jvertex = new JSONArray();
        JSONArray JEdges = new JSONArray();
        Collection<node_data> V = dwg.getV();
        Iterator<node_data> iter = V.iterator();
        Collection<edge_data> E = null;
        Iterator<edge_data> itr = null;

        try {
            while (iter.hasNext()) {
                node_data nn = iter.next();
                int n = nn.getKey();
                String p = nn.getLocation().toString();
                JSONObject node = new JSONObject();
                JSONObject GEO = new JSONObject();
                node.put("id", n);
                node.put("pos", (Object) p);
                jvertex.put((Object) node);
                //itr = dwg.getE(n).iterator();
                //  while (itr.hasNext()) {
                if (dwg.getE(n) != null)
                    for (edge_data ee : dwg.getE(n)) {
                        // edge_data ee = itr.next();
                        JSONObject edge = new JSONObject();
                        edge.put("src", ee.getSrc());
                        edge.put("dest", ee.getDest());
                        edge.put("w", ee.getWeight());
                        JEdges.put((Object) edge);
                    }
            }
            Jobj.put(("Nodes"), (Object) jvertex);
            Jobj.put(("Edges"), (Object) JEdges);



            FileWriter f = new FileWriter(file);
            f.write(JSONObject.valueToString(Jobj));
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    @Override
    public boolean load(String file) {




        try {
            JSONObject Jobj = new JSONObject((Files.readString(Path.of(file))));

            JSONArray JNodes = Jobj.getJSONArray("Nodes");


            //init all the nodes
            for (int i = 0; i < JNodes.length(); i++) {
                //create objcet of one node each index
                JSONObject jvertex = (JSONObject) JNodes.get(i);

                int key = jvertex.getInt("id");
                String location = (String) jvertex.getString("pos");
                String[] points = location.split(",");
                // crate new point for location
                GEO point = new GEO();
                //create point from Json Objcect "pos"
                point.x = Double.parseDouble(points[0]);
                point.y = Double.parseDouble(points[1]);
                point.z = Double.parseDouble(points[2]);

                //the uniq id of the node


                node_data n = new Node__Data(key, point);
                n.setLocation(point); //set location of Ex2.node_data
                //add the current node to my graph
                this.dwg.addNode(n);
            }
            JSONArray JEdges = Jobj.getJSONArray("Edges");

            // add the Edges from Json
            for (int i = 0; i < JEdges.length(); i++) {
                JSONObject jedge = (JSONObject) JEdges.get(i);
//                String location = (String) jedge.getString("pos");
                int src = jedge.getInt("src");
                int dest = jedge.getInt("dest");
                double w = jedge.getDouble("w");
                dwg.connect(src, dest, w);
            }
            this.init(dwg);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    class comp implements Comparator<node_data>{


        @Override
        public int compare(node_data o1, node_data o2) {
            // TODO Auto-generated method stub
            return (int)(o1.getWeight()-o2.getWeight());
        }
    }
}
