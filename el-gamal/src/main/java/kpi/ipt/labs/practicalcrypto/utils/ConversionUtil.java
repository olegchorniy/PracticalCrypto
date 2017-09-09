package kpi.ipt.labs.practicalcrypto.utils;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * BigInteger to/from byte array conversion utilities.
 */
public abstract class ConversionUtil {

    private ConversionUtil() {
    }

    public static byte[] packToByteArray(BigInteger first, BigInteger second) {
        byte[] firstArray = asUnsignedByteArray(first);
        byte[] secondArray = asUnsignedByteArray(second);

        int maxLength = Integer.max(firstArray.length, secondArray.length);

        byte[] targetArray = new byte[maxLength * 2];
        System.arraycopy(firstArray, 0, targetArray, 0, firstArray.length);
        System.arraycopy(secondArray, 0, targetArray, maxLength, secondArray.length);

        return targetArray;
    }

    public static byte[] packToByteArray(BigInteger value, int targetLength) {
        byte[] bytes = asUnsignedByteArray(value);
        if (bytes.length == targetLength) {
            return bytes;
        }

        byte[] paddedBytes = new byte[targetLength];
        System.arraycopy(bytes, 0, paddedBytes, targetLength - bytes.length, bytes.length);

        return paddedBytes;
    }

    public static byte[] packToByteArray(BigInteger first, BigInteger second, int targetLength) {
        byte[] high = asUnsignedByteArray(first);
        byte[] low = asUnsignedByteArray(second);

        byte[] result = new byte[targetLength];

        System.arraycopy(high, 0, result, targetLength / 2 - high.length, high.length);
        System.arraycopy(low, 0, result, targetLength - low.length, low.length);

        return result;
    }

    public static BigInteger[] unpackFromByteArray(byte[] array) {
        return unpackFromByteArray(array, 0, array.length);
    }

    public static BigInteger[] unpackFromByteArray(byte[] array, int offset, int length) {
        if ((length & 1) != 0) {
            throw new IllegalArgumentException("Byte sequence should be even length");
        }

        int halfLen = length / 2;

        byte[] firstArray = new byte[halfLen];
        byte[] secondArray = new byte[halfLen];

        System.arraycopy(array, offset, firstArray, 0, halfLen);
        System.arraycopy(array, offset + halfLen, secondArray, 0, halfLen);

        BigInteger first = fromUnsignedByteArray(firstArray);
        BigInteger second = fromUnsignedByteArray(secondArray);

        return new BigInteger[]{first, second};
    }

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

    public static byte[] slice(byte[] block, int offset, int length) {
        if (offset == 0 && length == block.length) {
            return block;
        }

        return Arrays.copyOfRange(block, offset, offset + length);
    }

    public static BigInteger fromUnsignedByteArray(byte[] buf, int offset, int length) {
        return fromUnsignedByteArray(slice(buf, offset, length));
    }

    public static BigInteger fromUnsignedByteArray(byte[] buf) {
        return new BigInteger(1, buf);
    }
}
