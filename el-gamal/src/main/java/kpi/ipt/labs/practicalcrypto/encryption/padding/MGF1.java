package kpi.ipt.labs.practicalcrypto.encryption.padding;

import java.security.MessageDigest;

public class MGF1 implements MGF {

    private final MessageDigest hash;
    private final int hLen;

    public MGF1(MessageDigest hash) {
        this.hash = hash;
        this.hLen = hash.getDigestLength();
    }

    @Override
    public byte[] mask(byte[] seed, int seedOffset, int seedLength, int length) {
        byte[] mask = new byte[length];
        byte[] C = new byte[4];

        // C = I2OSP(counter, 4)
        // T = T || HASH(seed || C)

        final int iterations = length / hLen;
        int maskPos = 0;
        int counter = 0;

        while (counter < iterations) {
            i2osp(counter, C);
            System.arraycopy(hash(seed, seedOffset, seedLength, C), 0, mask, maskPos, hLen);

            maskPos += hLen;
            counter++;
        }

        if (maskPos < length) {
            i2osp(counter, C);
            System.arraycopy(hash(seed, seedOffset, seedLength, C), 0, mask, maskPos, length - maskPos);
        }

        return mask;
    }

    private byte[] hash(byte[] seed, int seedOffset, int seedLength, byte[] C) {
        hash.update(seed, seedOffset, seedLength);
        hash.update(C);
        return hash.digest();
    }

    private void i2osp(int value, byte[] holder) {
        holder[0] = (byte) (value & 0xFF);
        holder[1] = (byte) ((value >> 8) & 0xFF);
        holder[2] = (byte) ((value >> 16) & 0xFF);
        holder[3] = (byte) ((value >> 24) & 0xFF);
    }
}
