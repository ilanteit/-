package api;

import java.util.Objects;

public class Node__Data implements node_data {
    private int key;
    private String info;
    private int tag;
    private double weight;
    //private HashMap<Integer,Ex2.node_data>vertex;
    private GEO Point;

public Node__Data(node_data n){
    this.key=n.getKey();
    this.info=n.getInfo();
    this.tag=n.getTag();
    this.weight=n.getWeight();
    this.Point=new GEO(n.getLocation().x(), n.getLocation().y(),n.getLocation().z());
}

    public Node__Data(int key, GEO p) {
        this.key = key;
        this.info = "";
        this.tag = -1;
        this.weight=0;
        this.Point=new GEO(p.x(),p.y(),p.z()) ;
        //vertex = new HashMap<>();
    }
    public Node__Data(int key) {
        this.key = key;
        this.info = "";
        this.tag = -1;
        this.weight=0;
        this.Point=new GEO() ;
        //vertex = new HashMap<>();
    }




    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return Point;
    }

    @Override
    public void setLocation(geo_location p) {
        Point.setX(p.x());
        Point.setY(p.y());
        Point.setZ(p.z());


    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight=w;

    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info=s;

    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;

    }

//    @Override
//    public String toString() {
//        return "Node__Data{" +
//                "key=" + key +
//
//                '}';
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node__Data that = (Node__Data) o;
        return key == that.key &&
                tag == that.tag &&
                Double.compare(that.weight, weight) == 0 &&
                info.equals(that.info) &&
                Point.equals(that.Point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, info, tag, weight, Point);
    }
}
