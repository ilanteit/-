package api;

import java.util.Objects;

public class edgeData implements edge_data {


    private int src;
    private  int dest;
    private double weight;
    private String info;
    private  int tag;

    public edgeData(int src, int dest, double weight, String info, int tag) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = "";
        this.tag = 0;
    }
    public edgeData(int src, int dest, double weight){
        this.info = "";
        this.tag = 0;
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        edgeData edgeData = (edgeData) o;
      //  System.out.println((src == edgeData.src )+ " " + (dest == edgeData.dest) + " " + (Double.compare(edgeData.weight, weight) == 0 )  + " " +  (tag == edgeData.tag) + " " +
      //          info.equals(edgeData.info));

        return src == edgeData.src &&
                dest == edgeData.dest &&
                Double.compare(edgeData.weight, weight) == 0 &&
                tag == edgeData.tag &&
                info.equals(edgeData.info);
    }

    @Override
    public String toString() {
        return "edgeData{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight, info, tag);
    }
}
