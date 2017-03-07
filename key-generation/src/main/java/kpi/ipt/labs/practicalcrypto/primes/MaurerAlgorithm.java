package kpi.ipt.labs.practicalcrypto.primes;

import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

//TODO: add sieve for prime finding, cleanup the code
public class MaurerAlgorithm {

    public static final int TRIAL_DIVISION_THRESHOLD = 20;

    //Maurer algorithm constants
    private static final double C = 0.1;
    private static final int M = 20;
    private static final int DOUBLE_M = M * 2;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);

    private final Random random;
    private int recursionLevel = -1;

    public MaurerAlgorithm(Random random) {
        this.random = random;
    }

    public BigInteger provablePrime(final int bitLength) {
        recursionLevel++;

        print("Find prime of length: " + bitLength);

        if (bitLength <= TRIAL_DIVISION_THRESHOLD) {
            print("fallback to trivial division: " + bitLength);

            BigInteger result = trialDivisionPrime(bitLength);
            recursionLevel--;
            return result;
        }

        double B = C * bitLength * bitLength;
        double r;
        if (bitLength > DOUBLE_M) {
            do {
                r = 0.5 + random.nextDouble() / 2;
            } while (bitLength * (1 - r) <= M);
        } else {
            r = 0.5;
        }

        print("r = " + r);

        //recursive call
        BigInteger q = provablePrime((int) (r * bitLength) + 1);

        // floor( 2^(k - 1) / (2 * q) )
        BigInteger I = ONE.shiftLeft(bitLength - 1).divide(q.shiftLeft(1));

        boolean success = false;
        BigInteger n = null;

        print("Start loop ...");
        while (!success) {
            // R is random in the interval [I + 1, 2I]
            BigInteger R = I.add(ONE).add(boundedRandom(I, random));
            //n = 2Rq + 1
            n = R.multiply(q).shiftLeft(1).add(ONE);

            if (trialDivision(n, (long) B)) {
                //a is random in the interval [2, n - 2] -> 2 + [0, n - 3)
                BigInteger a = TWO.add(boundedRandom(n.subtract(THREE), random));
                //b = a^(n - 1) mod n
                BigInteger b = a.modPow(n.subtract(ONE), n);

                if (b.equals(ONE)) {
                    print("b equals 1");
                    b = a.modPow(R.shiftLeft(1), n);
                    BigInteger d = n.gcd(b.subtract(ONE));

                    if (d.equals(ONE)) {
                        success = true;
                    } else {
                        print("d = " + d);
                    }
                }
            }
        }

        print("Found provable prime: " + n);

        recursionLevel--;
        return n;
    }

    private BigInteger trialDivisionPrime(int bitLength) {
        //2^(bitLength - 1) + 1
        int candidate = (1 << (bitLength - 1)) + 1;

        //2^(bitLength) - 1
        final int upperBound = (1 << bitLength) - 1;

        primeSearchLoop:
        while (candidate <= upperBound) {
            int divisorsBound = ((int) Math.sqrt(candidate)) + 1;

            for (int d = 3; d <= divisorsBound; d += 2) {
                if (candidate % d == 0) {
                    candidate += 2;
                    continue primeSearchLoop;
                }
            }

            print("Found by trial division prime: " + candidate);
            return BigInteger.valueOf(candidate);
        }

        throw new IllegalStateException("According to the Bertrand's postulate we should never reach this point");
    }

    private static boolean trialDivision(BigInteger testedNumber, long bound) {
        //check whether testedNumber is divisible by 2
        if (!testedNumber.testBit(0)) {
            return false;
        }

        for (int d = 3; d < bound; d += 2) {
            if (testedNumber.mod(BigInteger.valueOf(d)).equals(ZERO)) {
                return false;
            }
        }

        return true;
    }

    private static BigInteger boundedRandom(BigInteger bound, Random rnd) {
        int maxLength = bound.bitLength();

        BigInteger b;
        do {
            b = new BigInteger(maxLength, rnd);
        } while (b.compareTo(bound) >= 0);

        return b;
    }

    private void print(String message) {
        for (int i = 0; i < recursionLevel; i++) {
            if (i == recursionLevel - 1)
                System.out.print("|--");
            else
                System.out.print("|  ");
        }
        System.out.println(message);
    }
}
