package kpi.ipt.labs.practicalcrypto.utils;

import java.math.BigInteger;

/**
 * BigInteger to/from byte array conversion utilities.
 */
public final class ConversionUtil {

    /**
     * Return the passed in value as an unsigned byte array.
     *
     * @param value value to be converted.
     * @return a byte array without a leading zero byte if present in the signed encoding.
     */
    public static byte[] asUnsignedByteArray(
            BigInteger value) {
        byte[] bytes = value.toByteArray();

        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];

            System.arraycopy(bytes, 1, tmp, 0, tmp.length);

            return tmp;
        }

        return bytes;
    }

    public static BigInteger fromUnsignedByteArray(byte[] buf) {
        return new BigInteger(1, buf);
    }
}
