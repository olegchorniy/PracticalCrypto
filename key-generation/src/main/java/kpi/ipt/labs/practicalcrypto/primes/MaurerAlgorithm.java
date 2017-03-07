package kpi.ipt.labs.practicalcrypto.primes;

import java.math.BigInteger;

public class MaurerAlgorithm {

    public static final int TRIAL_DIVISION_THRESHOLD = 20;

    public static BigInteger provablePrime(int bitLength) {
        if (bitLength <= TRIAL_DIVISION_THRESHOLD) {
            return trialDivisionPrime(bitLength);
        }

        return null;
    }

    private static BigInteger trialDivisionPrime(int bitLength) {
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

            return BigInteger.valueOf(candidate);
        }

        throw new IllegalStateException("According to the Bertrand's postulate we should never reach this point");
    }
}
