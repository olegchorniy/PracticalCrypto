package kpi.ipt.labs.practicalcrypto.primes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class MaurerAlgorithm {

    private static final int TRIAL_DIVISION_THRESHOLD = 20;

    private static final BigInteger[] SMALL_PRIMES = {
            null, null,
            BigInteger.valueOf(3),
            BigInteger.valueOf(5),
            BigInteger.valueOf(11),
            BigInteger.valueOf(17),
            BigInteger.valueOf(37),
            BigInteger.valueOf(67),
            BigInteger.valueOf(131),
            BigInteger.valueOf(257),
            BigInteger.valueOf(521),
            BigInteger.valueOf(1031),
            BigInteger.valueOf(2053),
            BigInteger.valueOf(4099),
            BigInteger.valueOf(8209),
            BigInteger.valueOf(16411),
            BigInteger.valueOf(32771),
            BigInteger.valueOf(65537),
            BigInteger.valueOf(131101),
            BigInteger.valueOf(262147),
            BigInteger.valueOf(524309)
    };

    // Algorithm constants
    private static final double C = 0.2;
    private static final int M = 20;
    private static final int DOUBLE_M = M * 2;

    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);

    private final Random random;

    public MaurerAlgorithm(Random random) {
        this.random = random;
    }

    public BigInteger generatePrime(final int bitLength) {
        if (bitLength < 2) {
            throw new IllegalArgumentException("Bit length must be greater equal than 2");
        }

        double B = C * bitLength * bitLength;
        if ((long) B > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Requested bit length exceeds supported range");
        }

        //pre-compute primes necessary for trial division
        List<BigInteger> primes = eratosthenesSieve((int) B);

        return provablePrimeRecursive(bitLength, primes);
    }

    private BigInteger provablePrimeRecursive(final int bitLength, List<BigInteger> primes) {

        if (bitLength <= TRIAL_DIVISION_THRESHOLD) {
            return SMALL_PRIMES[bitLength];
        }

        BigInteger B = BigInteger.valueOf((int) (C * bitLength * bitLength));

        // choose appropriate value of r
        double r;
        if (bitLength > DOUBLE_M) {
            do {
                r = 0.5 + random.nextDouble() / 2;
            } while (bitLength * (1 - r) <= M);
        } else {
            r = 0.5;
        }

        // recursive call
        BigInteger q = provablePrimeRecursive((int) (r * bitLength + 1), primes);

        // floor( 2^(k - 1) / (2 * q) )
        BigInteger I = ONE.shiftLeft(bitLength - 1).divide(q.shiftLeft(1));

        while (true) {
            // R is random in the interval [I + 1, 2I]
            BigInteger R = I.add(ONE).add(boundedRandom(I, random));
            // n = 2Rq + 1
            BigInteger n = R.multiply(q).shiftLeft(1).add(ONE);

            if (trialDivision(n, primes, B)) {
                // a is random in the interval [2, n - 2]
                BigInteger a = TWO.add(boundedRandom(n.subtract(THREE), random));
                // b = a^(n - 1) mod n
                BigInteger b = a.modPow(n.subtract(ONE), n);

                if (b.equals(ONE)) {
                    // b = a^2R mod n
                    b = a.modPow(R.shiftLeft(1), n);
                    BigInteger d = n.gcd(b.subtract(ONE));

                    if (d.equals(ONE)) {
                        return n;
                    }
                }
            }
        }
    }

    private static boolean trialDivision(BigInteger testedNumber, List<BigInteger> primeDivisors, BigInteger bound) {
        for (BigInteger primeDivisor : primeDivisors) {

            if (primeDivisor.compareTo(bound) > 0) {
                break;
            }

            if (testedNumber.mod(primeDivisor).equals(ZERO)) {
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

    private static List<BigInteger> eratosthenesSieve(int boundExclusive) {
        boolean[] candidates = new boolean[boundExclusive];

        candidates[0] = candidates[1] = false;
        Arrays.fill(candidates, true);

        int rootSqrt = (int) Math.sqrt(boundExclusive);

        for (int i = 2; i <= rootSqrt; i++) {
            if (candidates[i]) {
                for (int j = i * i; j < boundExclusive; j += i) {
                    candidates[j] = false;
                }
            }
        }

        List<BigInteger> primes = new ArrayList<>();
        for (int i = 2; i < boundExclusive; i++) {
            if (candidates[i]) {
                primes.add(BigInteger.valueOf(i));
            }
        }

        return primes;
    }
}
