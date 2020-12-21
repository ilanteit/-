package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ex2 implements Runnable {
    //    private static Start st;
    private static MyFrame _win;
    private static Arena _ar;
    private static int id=-1;
    private int senrio=0;
    private static int scenario_num=0;

    public static void main(String[] a) {
        if (a.length!=0) {
            id = Integer.parseInt(a[0]);
            scenario_num = Integer.parseInt(a[1]);
        }
        Thread client = new Thread(new Ex2());
        client.start();
    }


    @Override
    public void run() {
        //        if(id==-1) {
        //            st = new Start();
        //            scenario_num = st.scenario_num;
        //        }
        game_service game = Game_Server_Ex2.getServer(23); // you have [0,23] games
        game.login(1);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);

        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;

        while(game.isRunning()) {
            _win.updateMoves();
            dt = moveAgants(game, gg);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(game.timeToEnd());
            _win.print_time(seconds,senrio);
            try {
                Grade(game);
                if(ind%1==0) {

                    _win.repaint();}
                Thread.sleep(dt);
                ind++;
                //if(ind % 10 == 0) dt--;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        //  System.exit(0);
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     * @param game
     * @param gg
     * @param
     */
    private static long moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for(int a = 0;a<ffs.size();a++) { Arena.updateEdge(ffs.get(a),gg);}
        double[][] distAgentsPokemons = getAllDistAgentsPokemons(gg, ffs, log);
        double[][] distPokemonPokemon = getAllDistPokemonPokemon(gg, ffs);
        HashMap<Integer, List<PokemonDist>> pokemonsPaths = getAllPaths(gg, distAgentsPokemons, distPokemonPokemon);
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if(dest==-1) {
                dest = nextNode(i, distAgentsPokemons, distPokemonPokemon, pokemonsPaths, gg);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
            }
        }
        return delayTime(game, gg);
    }

    class PokemonDist {
        CL_Pokemon p;
        double d;
        public PokemonDist(CL_Pokemon p, double d) {
            this.p = p;
            this.d = d;
        }
        @Override
        public String toString() {
            return "(pokemon: " + p + ", dist= " + d + ")";
        }

        @Override
        public boolean equals(Object obj) {
            return ((PokemonDist)obj).p == p;
        }
    }

    private static HashMap<Integer, List<PokemonDist>> getAllPaths(directed_weighted_graph gg, double[][] distAgentsPokemons, double[][] distPokemonPokemon) {
        HashMap<Integer, List<PokemonDist>> paths = new HashMap<Integer, List<PokemonDist>>();
        List<CL_Agent> agents = _ar.getAgents();
        List<CL_Pokemon> pokemons = _ar.getPokemons();
        for (int i = 0; i < agents.size(); i++) {
            List<PokemonDist> order = new LinkedList<PokemonDist>();
            double totalDist = 0;
            int lastPokemon = -1;
            for (int j = 0; j < pokemons.size(); j++) {
                int minPokemonIndex = -1;
                double minDist = Double.POSITIVE_INFINITY;
                for (int k = 0; k < pokemons.size(); k++) {
                    if(order.isEmpty()) {
                        if(distAgentsPokemons[i][k]/(pokemons.get(k).getValue()/50+agents.get(i).getSpeed()/10) < minDist) {
                            minDist = distAgentsPokemons[i][k];
                            minPokemonIndex = k;
                        }
                    }
                    else {
                        if(!order.contains(new Ex2().new PokemonDist(pokemons.get(k),totalDist)) && distPokemonPokemon[lastPokemon][k]/(pokemons.get(k).getValue()/50+agents.get(i).getSpeed()/10) < minDist) {
                            minDist = distPokemonPokemon[lastPokemon][k];
                            minPokemonIndex = k;
                        }
                    }
                }
                totalDist += minDist;
                order.add(new Ex2().new PokemonDist(pokemons.get(minPokemonIndex), totalDist));
                lastPokemon = minPokemonIndex;
            }
            paths.put(i, order);
        }
        return paths;
    }

    private static double[][] getAllDistPokemonPokemon(directed_weighted_graph g, List<CL_Pokemon> pokemons) {
        DWGraph_Algo g_algo = new DWGraph_Algo(g);
        double[][] d = new double[pokemons.size()][pokemons.size()];
        for (int i = 0; i < pokemons.size(); i++) {
            CL_Pokemon p1 = pokemons.get(i);
            for (int j = 0; j < pokemons.size(); j++) {
                if(i == j) d[i][j] = 0;
                else {
                    CL_Pokemon p2 = pokemons.get(j);
                    d[i][j] = g_algo.shortestPathDist(p1.get_edge().getDest(), p2.get_edge().getSrc()) + p1.get_edge().getWeight() + p2.get_edge().getWeight();
                }
            }
        }
        return d;
    }

    private static double[][] getAllDistAgentsPokemons(directed_weighted_graph g, List<CL_Pokemon> pokemons, List<CL_Agent> log) {
        DWGraph_Algo g_algo = new DWGraph_Algo(g);
        double[][] d = new double[log.size()][pokemons.size()];
        for (int i = 0; i < log.size(); i++) {
            CL_Agent a = log.get(i);
            for (int j = 0; j < pokemons.size(); j++) {
                CL_Pokemon p = pokemons.get(j);
                d[i][j] = g_algo.shortestPathDist(a.getSrcNode(), p.get_edge().getSrc()) + p.get_edge().getWeight();
            }
        }
        return d;
    }


    private static long delayTime(game_service game, directed_weighted_graph gg) {
        List<CL_Agent> agents = _ar.getAgents();
        List<CL_Pokemon> pokemons = _ar.getPokemons();
        double min_time = Double.POSITIVE_INFINITY;
        boolean pokemon = false;
        for(CL_Agent a : agents) {
            boolean isPokemon = false;
            for (CL_Pokemon p : pokemons) {
                double timeToGet = Double.POSITIVE_INFINITY;
                //  System.out.println(a.get_curr_edge());
                if(a.get_curr_edge() == p.get_edge()) {
                    edge_data e = a.get_curr_edge();
                    double edgeSize = gg.getNode(e.getSrc()).getLocation().distance(gg.getNode(e.getDest()).getLocation());
                    double relation = p.getLocation().distance(a.getLocation()) / edgeSize;
                    timeToGet = 1000 * e.getWeight() * relation/(a.getSpeed());
                    isPokemon = true;
                    pokemon = true;
                }
                if(timeToGet < min_time) min_time = timeToGet;
            }
            if(!isPokemon && a.getNextNode() != -1) {
                edge_data e = a.get_curr_edge();
                double edgeSize = gg.getNode(e.getSrc()).getLocation().distance(gg.getNode(e.getDest()).getLocation());
                double relation = a.getLocation().distance(gg.getNode(a.getNextNode()).getLocation()) / edgeSize;
                double timeToGet = 1000 * e.getWeight() * relation/(a.getSpeed());
                if(timeToGet < min_time) min_time = timeToGet;
            }
        }
        // System.out.println("Dist: " + (min_dist));
        if(min_time == Double.POSITIVE_INFINITY) return 0;
        return Math.min(220,(long)(min_time));
    }

    /**
     * a very simple random walk implementation!
     * @param agent
     * @param distPokemonPokemon
     * @param distAgentsPokemons
     * @param pokemonsPaths
     * @param g
     * @param g
     * @param
     * @param
     * @param
     * @return
     */
    private static int nextNode(int agent, double[][] distAgentsPokemons, double[][] distPokemonPokemon, HashMap<Integer, List<PokemonDist>> pokemonsPaths, directed_weighted_graph g) {
        // System.out.println(pokemonsPaths);
        DWGraph_Algo g_algo = new DWGraph_Algo(g);
        List<CL_Agent> agents = _ar.getAgents();
        List<PokemonDist> my_Pokemons =  pokemonsPaths.get(agent);
        while (!my_Pokemons.isEmpty()) {
            PokemonDist p = my_Pokemons.get(0);
            if(minDist(agents, agent, p, pokemonsPaths)) {
                if(p.p.get_edge().getSrc() == agents.get(agent).getSrcNode())
                    return p.p.get_edge().getDest();
                else
                    return g_algo.shortestPath(agents.get(agent).getSrcNode(), p.p.get_edge().getSrc()).get(1).getKey();
            }
            else {
                my_Pokemons = updateList(agents, agent, my_Pokemons, distAgentsPokemons, distPokemonPokemon);
            }
        }
        PokemonDist p =  pokemonsPaths.get(agent).get(0);
        if(p.p.get_edge().getSrc() == agents.get(agent).getSrcNode())
            return p.p.get_edge().getDest();
        else
            return g_algo.shortestPath(agents.get(agent).getSrcNode(), p.p.get_edge().getSrc()).get(1).getKey();
    }

    private static List<PokemonDist> updateList(List<CL_Agent> agents, int i, List<PokemonDist> a_pokemons, double[][] distAgentsPokemons, double[][] distPokemonPokemon) {
        List<CL_Pokemon> pokemons = new LinkedList<CL_Pokemon>();
        for (int j = 1; j < a_pokemons.size(); j++) {
            pokemons.add(a_pokemons.get(j).p);
        }
        List<PokemonDist> order = new LinkedList<PokemonDist>();
        double totalDist = 0;
        int lastPokemon = -1;
        for (int j = 0; j < pokemons.size(); j++) {
            int minPokemonIndex = -1;
            double minDist = Double.POSITIVE_INFINITY;
            for (int k = 0; k < pokemons.size(); k++) {
                if(order.isEmpty()) {
                    if(distAgentsPokemons[i][k]/(pokemons.get(k).getValue()/50+agents.get(i).getSpeed()/10) < minDist) {
                        minDist = distAgentsPokemons[i][k];
                        minPokemonIndex = k;
                    }
                }
                else {
                    if(!order.contains(pokemons.get(k)) && distPokemonPokemon[lastPokemon][k]/(pokemons.get(k).getValue()/50+agents.get(i).getSpeed()/10) < minDist) {
                        minDist = distPokemonPokemon[lastPokemon][k];
                        minPokemonIndex = k;
                    }
                }
            }
            totalDist += minDist;
            order.add(new Ex2().new PokemonDist(pokemons.get(minPokemonIndex), totalDist));
            lastPokemon = minPokemonIndex;
        }
        return order;
    }

    private static boolean minDist(List<CL_Agent> agents, int i, PokemonDist p, HashMap<Integer, List<PokemonDist>> pokemonsPaths) {
        for (int j = 0; j < agents.size(); j++) {
            if(j != i) {
                List<PokemonDist> pokemons = pokemonsPaths.get(j);
                for(PokemonDist p2 : pokemons) {
                    if(p2.p == p.p && p2.d < p.d) return false;
                }
            }
        }
        return true;
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            //  int grade = ttt.getInt("grade");
            // _ar.setGrade(grade);
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
            for(int a = 0;a<rs;a++) {
                int ind = a%cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }
    private void Grade(game_service game) {
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int grade = ttt.getInt("grade");
            _ar.setGrade(grade);
        }
        catch (JSONException e) {e.printStackTrace();}
    }

}
