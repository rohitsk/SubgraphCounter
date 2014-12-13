/**
 * Created with IntelliJ IDEA.
 * User: rosi
 * Date: 2014-04-08
 * Time: 11:34 PM
 * To change this template use File | Settings | File Templates.
 */

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class HashGenerator {
    private static final int MAXNUMBITS = 29;
    private static final int MINNUMBITS = 24;

    //Generated BigInteger is at most 2^30 -1 which is still
    // in integer range.
    private static BigInteger genProbablePrime(int numbits){
        Random ran = new SecureRandom();
        return BigInteger.probablePrime(numbits, ran);
    }

    private static BigInteger[] genSingleHashFunction(int k) {
        BigInteger[] hasharray = new BigInteger[k + 1];
        int numbits = (int) Math.floor(MINNUMBITS + Math.random() * (MAXNUMBITS - MINNUMBITS + 1));
        hasharray[k] = genProbablePrime(numbits);
        Random rnd = new Random();
        for(int i=0; i < k; i++) {
            //generate random numbers less than our prime
            do {
                hasharray[i] = new BigInteger(numbits - 1, rnd);
            } while(hasharray[i].compareTo(hasharray[k]) > 0);
        }
        return hasharray;
    }

    private static BigInteger bigint(int num){
        return new BigInteger(Integer.toString(num));
    }

    public static BigInteger[][] genIndHashFunctions(int k, int num){
        BigInteger[][] hashvalues = new BigInteger[num][k+1];
        for(int fnno = 0; fnno < num; fnno++) {
            BigInteger[] hasharray = genSingleHashFunction(k);
            System.arraycopy(hasharray, 0, hashvalues[fnno], 0, k + 1);
        }

        return hashvalues;
    }

    //Arguments: hasharray, input, power of fn + 1, result mod
    public static int computeHashValue(BigInteger[] hasharray, int inp, int k, int modden){
        BigInteger p = hasharray[k];
        BigInteger hashval = bigint(0);
        BigInteger input = bigint(inp);

        BigInteger[] copyArray = new BigInteger[k];

        for(int i=0; i < k; i++) {
            copyArray[i] = hasharray[i];
            copyArray[i] = copyArray[i].multiply(input.modPow(bigint(i),p)).mod(p);
            hashval = hashval.add(copyArray[i]);
            hashval = hashval.mod(p);
        }

        return hashval.mod(bigint(modden)).intValue();
    }

}