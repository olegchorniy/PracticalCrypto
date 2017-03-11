package kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.utils;

import java.math.BigInteger;
import java.util.Random;

public final class RandomUtils {
    private RandomUtils() {
    }

    /**
     * Return a random BigInteger not less than 'min' and not greater than 'max'
     *
     * @param min    the least value that may be generated
     * @param max    the greatest value that may be generated
     * @param random the source of randomness
     * @return a random BigInteger value in the range [min,max]
     */
    public static BigInteger createRandomInRange(
            BigInteger min,
            BigInteger max,
            Random random) {

        // [min, max] = min + [0, max - min]
        BigInteger diff = max.subtract(min);
        int diffLength = diff.bitLength();

        BigInteger value;

        do {
            value = new BigInteger(diffLength, random);
        } while (value.compareTo(diff) > 0);

        return value.add(min);
    }
}
