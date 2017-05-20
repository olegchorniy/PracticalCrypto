package kpi.ipt.labs.practicalcrypto.elgamal.key;

import kpi.ipt.labs.practicalcrypto.elgamal.utils.RandomUtils;

import java.math.BigInteger;
import java.util.Random;

public class ElGamalKeyGenerator {

    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    private static final int CERTAINTY = 100;

    private final Random random;

    public ElGamalKeyGenerator(Random random) {
        this.random = random;
    }

    public ElGamalKeyPair generateKeyPair(int pBitLength) {
        BigInteger[] safePrimes = generateSafePrimes(pBitLength, CERTAINTY, this.random);

        BigInteger p = safePrimes[0];
        BigInteger q = safePrimes[1];

        BigInteger g = selectGenerator(p, q, this.random);
        BigInteger x = RandomUtils.createRandomInRange(TWO, p.subtract(TWO), this.random);
        BigInteger y = g.modPow(x, p);

        ElGamalPrivateKey privateKey = new ElGamalPrivateKey(p, g, x);
        ElGamalPublicKey publicKey = new ElGamalPublicKey(p, g, y);

        return new ElGamalKeyPair(publicKey, privateKey);
    }


    /*
     * Finds a pair of prime BigInteger's {p, q: p = 2q + 1}
     *
     * (see: Handbook of Applied Cryptography 4.86)
     */
    private static BigInteger[] generateSafePrimes(int size, int certainty, Random random) {
        BigInteger p, q;
        int qLength = size - 1;

        do {
            q = new BigInteger(qLength, certainty, random);

            // p = 2q + 1
            p = q.shiftLeft(1).add(ONE);

        } while (!p.isProbablePrime(certainty));

        return new BigInteger[]{p, q};
    }

    /*
     * Select a high order element of the multiplicative group Zp*
     *
     * p and q must be s.t. p = 2*q + 1, where p and q are prime (see generateSafePrimes)
     */
    private static BigInteger selectGenerator(BigInteger p, BigInteger q, Random random) {
        BigInteger pMinusTwo = p.subtract(TWO);
        BigInteger g;

        /*
         * (see: Handbook of Applied Cryptography 4.80)
         */
        do {
            g = RandomUtils.createRandomInRange(TWO, pMinusTwo, random);
        }
        while (g.modPow(TWO, p).equals(ONE) || g.modPow(q, p).equals(ONE));

        return g;
    }
}
