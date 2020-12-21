package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

public class MyPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Range2Range _w2f;
    private Arena _ar;
    private int move = 0;
    private double value=0;
    private Image backgraound;
    private Image agent;
    private Image squirtle;


    MyPanel(){
        super();
        this.setOpaque(false);
        this.setBackground(Color.WHITE);
        this.agent=new ImageIcon("./data/ash2.png ").getImage();
        this.squirtle=new ImageIcon("./data/pik.png ").getImage();
        this.backgraound = new ImageIcon("data\\beckground.png").getImage();
    }

    public void updateFrame(Arena ar) {
        _ar = ar;
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        g.drawImage(this.backgraound,0, 0, w, h, null);
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        g.setFont(new Font("David",Font.BOLD,11));
        g.setColor(Color.blue);
        String s="Timer: "+MyFrame.timeToEnd + " " + MyFrame.senrio;
        g.drawString(s, 300, 50);


    }
    private void drawInfo(Graphics g) {
        int grade = _ar.getGrade();
        List<String> str = _ar.get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }
        g.drawString("Moves: " + move, 100, 50);
        g.drawString("Score: " +grade,50,100);
    }
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {

                CL_Pokemon f = itr.next();

                Point3D c = f.getLocation();
                int r=10;
                g.setColor(Color.green);
                if(f.getType()<0) {g.setColor(Color.orange);}
                if(c!=null) {

                    geo_location fp = this._w2f.world2frame(c);

                    g.drawImage(squirtle,(int)fp.x()-r, (int)fp.y()-r, 4*r, 4*r,null);
                    g.setColor(Color.red);
                    g.drawString(""+f.getValue(), (int)fp.x()-r, (int)fp.y()-r);
                }
            }
        }
    }


    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,11));
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r=8;
            String s="agent "+rs.get(i).getID()+" :	 "+rs.get(i).getValue();
            i++;
            if(c!=null) {

                geo_location fp = this._w2f.world2frame(c);

                g.drawImage(agent,(int)fp.x()-r, (int)fp.y()-r, 7*r, 7*r,null);

                g.drawString(rs.get(i-1).getSpeed() + "", (int)fp.x()-r, (int)fp.y()-r);
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);

    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    public void updateMove() {
        move ++;
    }
}