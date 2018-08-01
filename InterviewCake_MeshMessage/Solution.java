import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Solution{


    public static void main(String[] args){

        /*
        network = {
            'Min'     : ['William', 'Jayden', 'Omar'],
            'William' : ['Min', 'Noam'],
            'Jayden'  : ['Min', 'Amelia', 'Ren', 'Noam'],
            'Ren'     : ['Jayden', 'Omar'],
            'Amelia'  : ['Jayden', 'Adam', 'Miguel'],
            'Adam'    : ['Amelia', 'Miguel', 'Sofia', 'Lucas'],
            'Miguel'  : ['Amelia', 'Adam', 'Liam', 'Nathan'],
            'Noam'    : ['Nathan', 'Jayden', 'William'],
            'Omar'    : ['Ren', 'Min', 'Scott']
        }
        */
        //List<Node> graph = new ArrayList<Node>();
        Map<String, String[]> graph = new HashMap<String,String[]>();
        String[] str = new String[]{"Min", "Jayden", "Omar"};
        int[] arr_int = new int[]{1, 2, 3};
        Node min = new Node("Min", 
        new String[]{"Min", "Jayden", "Omar"}
    );
        Node william = new Node("William", 
        new String[]{"Min", "Noam"}
    );
        Node jayden = new Node("Min", 
        new String[]{"Min", "Amelia", "Ren", "Noam"}
    );
        Node ren = new Node("Min", 
        new String[]{"Jayden", "Omar"}
    );
        Node amelia = new Node("Min", 
        new String[]{"Jayden", "Adm", "Miguel"}
    );
        Node adam = new Node("Min", 
        new String[]{"Amelia", "Miguel", "Sofia", "Lucas"}
    );
        Node miquel = new Node("Min", 
        new String[]{"Amelia", "Adam", "Liam", "Nathan"}
    );
        Node noam = new Node("Min", 
        new String[]{"Nathan", "Jayden", "William"}
    );
        Node omar = new Node("Min", 
        new String[]{"Ren", "Min", "Scott"}
    );
        /*
        graph.add(min);
        graph.add(william);
        graph.add(jayden);
        graph.add(ren);
        graph.add(amelia);
        graph.add(adam);
        graph.add(miquel);
        graph.add(noam);
        graph.add(omar);
        */
        graph.put("Min", new String[]{"William", "Jayden", "Omar"});
        graph.put("William", new String[]{"Min", "Noam"} );
        graph.put("Jayden", new String[]{"Min", "Amelia", "Ren", "Noam"});
        graph.put("Ren", new String[]{"Jayden", "Omar"});
        graph.put("Amelia", new String[]{"Jayden", "Adm", "Miguel"});
        graph.put("Adam", new String[]{"Amelia", "Miguel", "Sofia", "Lucas"});
        graph.put("Miguel", new String[]{"Amelia", "Adam", "Liam", "Nathan"});
        graph.put("Noam", new String[]{"Nathan", "Jayden", "William"});
        graph.put("Min", new String[]{"Ren", "Min", "Scott"});
        List<String> result = new ArrayList<String>();
        bfs(graph, "Jayden", "Adam", result);

    }


    public static void bfs(Map<String, String[]> g, String start, String end, List<String> result){
        Queue<String> queue = new LinkedList<String>();
        Set<String> visited = new HashSet<String>();
        queue.offer(start);
        String[] edges ;
        int level = 0;
        while(!queue.isEmpty()){
            level++;
            String v = queue.poll();
            edges = g.get(v);
            for (int i=0;i<edges.length;i++){
                queue.offer(edges[i]);
            }
            
            if (!visited.contains(v)){
                visited.add(v);
                result.add(v);
                if (v == end){
                    break;
                }
                else { 
                    edges = g.get(v);
                    for (int i=0;i<edges.length;i++){
                        queue.offer(edges[i]);
                    }
                }

            }

        }
    }

    static class Node {
        String vertice;
        List<String> edges; 
        Node(String v, String[] e){
            vertice = v;
            for (int i=0;i<e.length;i++){
                edges.add(e[i]);
            }
        }
    }



}