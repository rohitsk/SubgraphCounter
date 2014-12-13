import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-18
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 *
 *
 * 4 parameters:
 * arg[0]: number of vertices in graph
 * arg[1]: number of vertices needed in subgraph
 * arg[2]: original graph location
 * arg[3]: Subgraph Output location
 */
public class GenerateSubgraph {
    static Integer[] sample;

    private static boolean notInArray(int random_pick) {
        int i=0;
        int sample_size = sample.length; //sample_size
        while(i < sample_size && sample[i] != -1) {
            if(sample[i] == random_pick) {
                return false;
            }
            i++;
        }
        return true;
    }

    // i'm not doing any sanity checks here. Assume that num_vert_g >>> sample_size
    public static void pickRandomVertices(int num_vert_g, int sample_size) {
        int numAttempts = 0;
        sample = new Integer[sample_size];
        for(int i=0; i < sample_size; i++) {
            sample[i] = -1;
        }

        int counter = 0;
        while(counter < sample_size) {
            int random_pick = (int) Math.floor(Math.random() * num_vert_g);
            if (notInArray(random_pick)){
                sample[counter++] = random_pick;
                System.out.println(random_pick);
            }

        }
    }

    public static void main(String[] args) throws Exception {
        int num_vert_g = Integer.parseInt(args[0]);
        int num_vert_out = Integer.parseInt(args[1]);

        pickRandomVertices(num_vert_g, num_vert_out);
        List<Integer> sampleList = Arrays.asList(sample);

        // Done picking vertices. These vertices are now stores in sample[]

        String grLoc = args[2];
        BufferedReader reader = new BufferedReader(new FileReader(grLoc));

        PrintWriter writer = new PrintWriter(args[3]);
        boolean hasPrinted = false;

        String line;
        Integer u, v;
        while ((line = reader.readLine()) != null) {
            String[] endptsS = line.split("\t");
            u = Integer.parseInt(endptsS[0]);
            v = Integer.parseInt(endptsS[1]);
            if (sampleList.contains(u) && sampleList.contains(v)) {
                if (hasPrinted) {
                    writer.print("\n");
                } else {
                    hasPrinted = true;
                }
                writer.print(u.intValue() + "\t" + v.intValue());
            }

        }

    }

}
