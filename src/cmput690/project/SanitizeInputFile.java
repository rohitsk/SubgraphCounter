import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-11
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 *
 *
 * 3 arguments:
 * arg[0]: max. vertex number in streaming graph's edges
 * arg[1]: location of stream
 * arg[2]: location of sanitized output stream
 */

public class SanitizeInputFile {
    static List<List<Integer>> adj_list;
    static List<List<Integer>> final_adj_list;
    static int num_vertices;
    static int actual_vertices[];

    public static void main(String[] args) throws Exception {
        num_vertices = Integer.parseInt(args[0]);
        actual_vertices = new int[num_vertices];

        for(int i=0; i < num_vertices; i++) {
            actual_vertices[i] = -1;
        }

        adj_list = new ArrayList<List<Integer>>();
        final_adj_list = new ArrayList<List<Integer>>();

        for(int i=0; i < num_vertices; i++) {
            adj_list.add(new ArrayList<Integer>());
        }

        String grLoc = args[1];
        BufferedReader reader = new BufferedReader(new FileReader(grLoc));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] endptsS = line.split("\t");
            int u = Integer.parseInt(endptsS[0]);
            int v = Integer.parseInt(endptsS[1]);
            adj_list.get(u).add(v);
            adj_list.get(v).add(u);
            actual_vertices[u] = 1;
            actual_vertices[v] = 1;
        }

        int counter = 0;
        for(int i=0; i < num_vertices; i++) {
            if(actual_vertices[i] != -1) {
                actual_vertices[i] = counter++;
            }
        }

        //System.out.print(counter);  //=> 317080

        counter = 0;
        for(int i=0; i < adj_list.size(); i++){
            if(actual_vertices[i] != -1) {
                final_adj_list.add(new ArrayList<Integer>());
                for (int j=0; j < adj_list.get(i).size(); j++) {
                    final_adj_list.get(counter).add(actual_vertices[adj_list.get(i).get(j)]);
                }
                counter++;
            }
        }

        PrintWriter writer = new PrintWriter(args[2]);
        boolean hasPrinted = false;
        for (int i=0; i < final_adj_list.size(); i++) {
            for (int j=0; j < final_adj_list.get(i).size(); j++) {
                int x = final_adj_list.get(i).get(j);
                if (x > i) {
                    if (hasPrinted) {
                        writer.print("\n");
                    } else {
                        hasPrinted = true;
                    }
                    writer.print(i + "\t" + x);
                }
            }
        }
        writer.close();
    }
}