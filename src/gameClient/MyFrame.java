package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
    private static final long serialVersionUID = 1L;
    private MyPanel panel;
    static long timeToEnd=0;static int senrio=0;

    MyFrame(String a) {
        super(a);
        panel = new MyPanel();
        panel.setBounds(-150, -150, 1000, 600);
        panel.setVisible(true);
        add(panel);
    }

    public void update(Arena ar) {
        panel.updateFrame(ar);
    }

    public void updateMoves() {
        panel.updateMove();
    }
    public void print_time(long timeToEnd, int senrio) {
        this.timeToEnd=timeToEnd;
        this.senrio=senrio;
    }
}