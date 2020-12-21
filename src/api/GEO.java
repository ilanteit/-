package api;

import java.util.Objects;

public class GEO implements geo_location {

    public double x;
    public double y;
    public double z;

    public GEO(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GEO() {
        this.x =0;
        this.y =0;
        this.z =0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GEO geo = (GEO) o;
        return Double.compare(geo.x, x) == 0 &&
                Double.compare(geo.y, y) == 0 &&
                Double.compare(geo.z, z) == 0;
    }

    @Override
    public String toString() {
        return
                  x +","
                 + y +","
                 + z
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public double distance(geo_location g) {
        return Math.sqrt(Math.pow(x(),2)+Math.pow(y(),2)+Math.pow(z(),2));
    }
}
