/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-10
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Take 3 input arguments:
 * a) location of the file that stores the subgraph.
 * b) location of the file that stores the original streaming graph.
 * c) number of Estimator objects that need to be invoked.
 */
public class CountSubgraphs {

    private int numEstimators;
    EstimatorObject[] estimatorObjects;

    public CountSubgraphs(BufferedReader reader, int numEstimators) throws Exception{
        this.numEstimators = numEstimators;
        estimatorObjects = new EstimatorObject[numEstimators];
        initializeSubgraph(reader);

        for(int i=0; i < numEstimators; i++) {
            estimatorObjects[i] = new EstimatorObject();
        }
    }

    public void initializeSubgraph(BufferedReader reader) throws Exception {

        String firstLine;
        firstLine = reader.readLine();
        String[] paramstr = firstLine.split(" ");
        int[] params = new int[paramstr.length];

        for(int i=0; i < paramstr.length; i++) {
            params[i] = Integer.parseInt(paramstr[i]);
        }

        int t = params[0];
        // int k = params[1];
        // int ng = params[2];

        int adj_mat[][] = new int[t][t];

        for(int i=0; i < t; i++) {
            String[] adjmatrowS = reader.readLine().split(" ");
            for(int j=0; j < t; j++) {
                adj_mat[i][j] = Integer.parseInt(adjmatrowS[j]);
            }
        }

        EstimatorObject.initSubgraph(params[2], t, params[1], adj_mat);
    }

    public void updateWithEdges(BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] endptsS = line.split("\t");
            int u = Integer.parseInt(endptsS[0]);
            int v = Integer.parseInt(endptsS[1]);
            for(int i=0; i < numEstimators; i++) {
                estimatorObjects[i].updateZValues(u, v);
            }
        }
    }

    public int retrieveAndPublishResults() {
        int[] estimates = new int[numEstimators];
        double avg = 0.0;
        for(int i=0; i < numEstimators; i++) {
            estimates[i] = estimatorObjects[i].estimateNumSubgraphs();
            avg += (double) estimates[i] / (double) numEstimators;
            //System.out.println(estimates[i]);
        }
        return (int) Math.round(avg);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            throw new IllegalArgumentException("Arguments not specified");
        }

        String subgrLoc = args[0];
        String grLoc = args[1];
        int numEstimators = Integer.parseInt(args[2]);


       // String subgrLoc = "/Users/rosi/Desktop/proj_inp/h";
       // String grLoc = "/Users/rosi/Desktop/proj_inp/new_subg_san";
       // int numEstimators = 100;

        BufferedReader hReader = new BufferedReader(new FileReader(subgrLoc));
        CountSubgraphs sgcounter = new CountSubgraphs(hReader, numEstimators);

        BufferedReader gReader = new BufferedReader(new FileReader(grLoc));
        sgcounter.updateWithEdges(gReader);
        int estimate = sgcounter.retrieveAndPublishResults();
        System.out.println(estimate);
    }
}

