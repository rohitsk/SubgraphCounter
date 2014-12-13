/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-09
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */

import defaults.Complex;
import java.math.BigInteger;

public class EstimatorObject {
    static int ng = 0;                       //number of vertices in G
    static int nh = 0;                       //number of vertices in H
    static int kh = 0;                       //number of edges in H
    static int[][] h_adj_mat;
    static int[] degH;
    static int noauto;
    static double multiplierTerm;


    /* * object specific variables:
    *  1. Q - random root of (2^t - 1)
    *  2. degree(v) for every vertex in H
    *     initialize 4k-wise independent hash functions
    *  3. X[c][w]: for every pair of vertices
    *  4. Y[w]: for every vertex in G
    * */

    Complex Q;
    BigInteger[][] hashes;
    Complex[][] X;
    int[] Y;
    Complex[] Z;

    //find edges
    static int edges[][];
    static int numedges = 0;

    private static void initializeDeg() {
        degH = new int[nh];
        for(int i=0; i < nh; i++) {
            degH[i] = 0;
            for(int j=0; j < nh; j++) {
                degH[i] += h_adj_mat[i][j];
            }
            if (degH[i] == 0) {
                throw new IllegalStateException("Subgraph contains Zero degree vertex. " +
                        "Algorithm can't handle this");
            }
        }
    }

    private static void initializeEdges() {
        edges = new int[2 * kh][2];
        for(int i=0; i < (nh - 1); i++) {
            for(int j=i+1; j < nh; j++) {
                if (h_adj_mat[i][j] != 0) {
                    edges[numedges][0] = i;
                    edges[numedges++][1] = j;

                }
            }
        }
        if (numedges != kh) {
            throw new RuntimeException("Something isn't right here");
        }
    }

    private static double getMultiplierTerm() {
        double t = (double) nh;
        double fact = 1;
        for(int i=1; i <= nh; i++) {
            fact *= (double) i;
        }
        return Math.pow(t,t) / (fact * (double) noauto);
    }

    private static void initializeAutomorphismChecker() {
        AutomorphismChecker checker = new AutomorphismChecker(nh);
        noauto = checker.getNumAutomorphisms(h_adj_mat);
        multiplierTerm = getMultiplierTerm();
    }

    public static void initSubgraph(int num_vg, int num_vh, int num_kh, int[][] h_edges) {
        ng = num_vg;
        nh = num_vh;
        kh = num_kh;
        h_adj_mat = new int[num_vh][num_vh];
        for(int i=0; i < num_vh; i++) {
            System.arraycopy(h_edges[i], 0, h_adj_mat[i], 0, num_vh);
        }
        initializeDeg();
        initializeEdges();
        initializeAutomorphismChecker();
    }

    private Complex initializeQ() {
        return Complex.getRandomRoot(Math.pow(2.0, (double) nh) - 1);
    }

    private BigInteger[][] initializeHashFuncs() {
        //first argument: 4k-wise hash fns
        //second argument: family of 4k functions
        return HashGenerator.genIndHashFunctions(4 * kh, 4 * kh);
    }

    private int[] initializeY() {
        int[] temp_y = new int[ng];
        for(int i=0; i < ng; i++) {
            int hashno = (int) Math.floor(Math.random() * 4 * kh);
            temp_y[i] = (int) Math.pow( 2.0,
                    (double)HashGenerator.computeHashValue(hashes[hashno], i + 1, 4 * kh, nh));
        }
        return temp_y;
    }

    private Complex[][] initializeX() {
        //1. select a random hash function between 1 and 4k
        //2. get that root of infinity

        Complex temp_x[][] = new Complex[nh][ng];
        for(int i = 0; i < nh; i++) {
            for(int j= 0; j < ng; j++) {
                int hashno = (int) Math.floor(Math.random() * 4 * kh);
                temp_x[i][j] = Complex.convertRootToComplex( (double)
                        HashGenerator.computeHashValue(hashes[hashno], j + 1, 4 * kh, degH[i]) , (double) degH[i]
                );
            }
        }

        return temp_x;
    }

    private Complex[] initializeZ() {
        Complex[] temp_z = new Complex[numedges];
        for(int i=0; i < numedges; i++){
            temp_z[i] = new Complex(0.0);
        }
        return temp_z;
    }

    private void initializeObj() {
        this.Q = initializeQ();
        this.hashes = initializeHashFuncs();
        this.Y = initializeY();
        this.X = initializeX();
        this.Z = initializeZ();
    }

    public EstimatorObject(){
        if (nh == 0 || ng == 0 || kh == 0 || h_adj_mat == null) {
            throw new RuntimeException("Subgraph uninstantiated");
        }
        initializeObj();
//        printValues();
    }

    private Complex getM(int a, int b, int u, int v){
        double expo1 = (double) Y[u] / (double) degH[a];
        double expo2 = (double) Y[v] / (double) degH[b];
        return X[a][u].multiply(X[b][v]).multiply(Q.pow(expo1 + expo2));
    }

    public void updateZValues(int u, int v) {
        for(int i=0; i < numedges; i++) {
            this.Z[i] = Z[i].add(getM(edges[i][0], edges[i][1], u, v)).add(getM(edges[i][0], edges[i][1], v, u));
        }
    }

    public int estimateNumSubgraphs(){
        Complex Zh = new Complex(1.0);
        for (int i=0; i < numedges; i++){
            Zh = Zh.multiply(Z[i]);
        }
        return (int) Math.round(Zh.real() * multiplierTerm);
    }

    // for testing
    public void printValues() {
        System.out.print("degH =");
        for(int i = 0; i < degH.length; i++) {
            System.out.print(" " + degH[i]);
        }
        System.out.println("\n\n");

        ///////////////////////////////////
        ///////////////////////////////////

        System.out.print("Y =");
        for(int i = 0; i < Y.length; i++) {
            System.out.print(" " + Y[i]);
        }
        System.out.println("\n\n");

        ///////////////////////////////////
        ///////////////////////////////////

        for(int i = 0; i < nh; i++) {
            System.out.print("X[" + i + "] =");
            for(int j= 0; j < ng; j++) {
                System.out.print(" " + X[i][j].toString());
            }
            System.out.print("\n\n");
        }

    }

}