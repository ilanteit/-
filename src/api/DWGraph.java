package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DWGraph implements directed_weighted_graph {

    private HashMap<Integer, HashMap<Integer, edge_data>> edges; //graph edges
    private HashMap<Integer, node_data> nodes; //graph nodes
    private HashMap<Integer, HashMap<Integer, edge_data>> destEdges; //is the reverse hashmap of the "edges" hashmap

    public int modeCount =0;
    int sizeEdge=0;

    public DWGraph(){    //empty constructor
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.destEdges=new HashMap<>();
        this.sizeEdge=0;
        this.modeCount=0;
    }



    public DWGraph(node_data n){ // init with one node
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.nodes.put(n.getKey(),n);
        this.sizeEdge=0;
        this.destEdges=new HashMap<>();

    }


    @Override
    public node_data getNode(int key) {

        return nodes.get(key);


    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if (edges.get(src)==null||edges.get(src).get(dest)==null) {
            return null;
        }
        return edges.get(src).get(dest);

    }

    @Override
    public void addNode(node_data n) {

        if(!nodes.containsKey(n.getKey())) {
            edges.put(n.getKey(), new HashMap<>());
            destEdges.put(n.getKey(), new HashMap<>());
            nodes.put(n.getKey(), n);
            modeCount++;
        }

    }



    @Override
    public void connect(int src, int dest, double w) {

        if(src == dest || this.getNode(src) == null || this.getNode(dest) == null || w<0){ //checks if the src and dest are different and not null
            return;
        }


        edgeData edge = new edgeData(src,dest,w);

            if (!(edges.get(src).containsKey(dest))) {  // check if the edge exist
                edges.get(src).put(dest, edge); //adding to the edges hashmap
                destEdges.get(dest).put(src,edge); //adding to the "reverse edge" hashmap
                sizeEdge++;
            }


        else {
            edges.get(src).put(dest,edge);
            destEdges.get(src).put(dest,edge);

        }
        modeCount++;

    }

    @Override
    public Collection<node_data> getV()
    {
        return nodes.values();

    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if(this.edges.containsKey(node_id))
            return this.edges.get(node_id).values();
        else
            return null;

    }

    @Override
    public node_data removeNode(int key) {

        node_data removed=nodes.remove(key);
        if(removed!=null){
            if(edges.containsKey(key)){  //checks if key has edges

                for(Integer edge1:destEdges.get(key).keySet()) { //going through all keys edges
                    if (edges.get(edge1) != null) {
                        edges.get(edge1).remove(key);
                        sizeEdge--;
                    }
                }

                sizeEdge-=getE(key).size();//  removing the size of the edges of the neighboors of the node.

//                }
                destEdges.remove(key);
                edges.remove(key);
            }
            modeCount++;
        }
        //you need to run all the edges and check if the dest is the key and delete it





return removed;






    }

    @Override
    public edge_data removeEdge(int src, int dest) {
if(getNode(src)==null || getNode(dest)==null)
{
    return null;
}
        modeCount++;
        sizeEdge--;
        return edges.get(src).remove(dest);






    }

    @Override
    public int nodeSize() {
       return nodes.size();
    }

    @Override
    public int edgeSize() {

        return sizeEdge;
    }

    @Override
    public int getMC() {
        return modeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph dwGraph = (DWGraph) o;

        return
                sizeEdge == dwGraph.sizeEdge &&
                edges.equals(dwGraph.edges) &&
                nodes.equals(dwGraph.nodes);

    }

    @Override
    public int hashCode() {
        return Objects.hash(edges, nodes, modeCount, sizeEdge);
    }
}
