/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-08
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */

public class AutomorphismChecker {
    public AutomorphismChecker(int num_vertices) {
        this.num_vertices = num_vertices;
    }

    private boolean isAutoMorphism(int adj_matrix1[][],
                                 int adj_matrix2[][]){
        for(int i=0;i < num_vertices; i++){
            for(int j=0; j< num_vertices; j++) {
                if (adj_matrix1[i][j] != adj_matrix2[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    // all numbers are distinct.
    // We don't need to worry about equality
    private int[] findNextPerm(int[] curr_perm){
        int i = num_vertices - 2;

        //array copy
        int[] next_perm = new int[num_vertices];
        System.arraycopy(curr_perm, 0, next_perm, 0, num_vertices);

        //edge case - IndexOutOfBounds
        while(i >= 0) {
            if(curr_perm[i] < curr_perm[i+1]) {
                //point of deflection
                for(int j=i+1; j < num_vertices; j++){
                    next_perm[j] = curr_perm[num_vertices + i - j];
                }
                for(int j=i+1; j < num_vertices; j++){
                    //swap 2 numbers
                    if(next_perm[i] < next_perm[j]){
                        int temp = next_perm[i];
                        next_perm[i] = next_perm[j];
                        next_perm[j] = temp;
                        return next_perm;
                    }
                }
            }
            i--;
        }
        if(i == -1) {
            reached_max = true;
        }
        return next_perm;
    }

    public int getNumAutomorphisms(int[][] adj_mat){
        int[] curr_perm = new int[num_vertices];
        for(int i=0; i< num_vertices; i++){
            curr_perm[i] = i;
        }
        int auto_count = 1;

        while(!reached_max){
            int[] new_perm = findNextPerm(curr_perm);
            counter++;
            if (!reached_max) {
                int[][] new_adj_mat = new int[num_vertices][num_vertices];
                for (int i=0; i < num_vertices; i++){
                    for(int j=0; j < num_vertices; j++){
                        new_adj_mat[i][j] = adj_mat[new_perm[i]][new_perm[j]];
                    }
                }
                if (isAutoMorphism(adj_mat, new_adj_mat)) {
                    auto_count++;
                }
            }
            else {
                break;
            }
            curr_perm = new_perm;
        }
        return auto_count;
    }

    //counter isn't used now.
    // But gives the number of times nextPerm is invoked.
    private int counter = 0;

    private boolean reached_max = false;
    private final int num_vertices;

}