import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-10
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 *
 * 2 parameters:
 * 1st parameter: location of the stream on the computer
 * 2nd parameter: number of vertices in the streaming graph
 *
 **/
public class NaiveTriangleCounter {
    static int num_vertices;
    static List<List<Integer>> adj_list;

    public static void main(String[] args) throws Exception {
        num_vertices = Integer.parseInt(args[1]);
        adj_list = new ArrayList<List<Integer>>();

        for(int i=0; i < num_vertices; i++) {
            adj_list.add(new ArrayList<Integer>());
        }

        String grLoc = args[0];
//        String grLoc = "/Users/rosi/Desktop/proj_inp/new_subg_san";
        BufferedReader reader = new BufferedReader(new FileReader(grLoc));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] endptsS = line.split("\t");
            int u = Integer.parseInt(endptsS[0]);
            int v = Integer.parseInt(endptsS[1]);
            adj_list.get(u).add(v);
            adj_list.get(v).add(u);
        }

/* The following lines prints the adjacency list stored in memory

        System.out.println("Done adding elements into adj_list");
        for(int i=0; i < adj_list.size(); i++ ) {
            System.out.println("\n");
            for(int j=0; j < adj_list.get(i).size(); j++) {
                System.out.print(adj_list.get(i).get(j) + " ");
            }
        }
*/

        int triangleCount = 0;

        for(int i=0; i < num_vertices - 2; i++) {
            for(int j = i + 1; j < num_vertices - 1; j++) {
                for(int k = j + 1; k < num_vertices; k++) {
                    if (checkEdge(i,j) && checkEdge(j,k) && checkEdge(i,k)) {
                        triangleCount++;
                    }
                }
            }
        }

        System.out.println(triangleCount);
    }

    private static boolean checkEdge(int a, int b) {
        return adj_list.get(a).contains(b);
    }

}