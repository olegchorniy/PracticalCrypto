package kpi.ipt.labs.practicalcrypto.encryption;

/**
 * @author olch0615
 *         Date: 5/22/2017
 *         Time: 3:28 PM
 */
public class EncryptionUtils {

    public static byte[] slice(byte[] source, int srcOffset, int targetLength) {
        byte[] sliced = new byte[targetLength];

        System.arraycopy(source, srcOffset, sliced, 0, targetLength);

        return sliced;
    }

    public static byte[] concat(byte[] left, byte[] right) {

        byte[] result = new byte[left.length + right.length];

        System.arraycopy(left, 0, result, 0, left.length);
        System.arraycopy(right, 0, result, left.length, right.length);

        return result;
    }

    public static void checkInputLength(int expected, int actual) {
        if (expected < actual) {
            throw new IllegalArgumentException("Input message too long");
        }
    }
}