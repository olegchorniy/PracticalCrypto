package kpi.ipt.labs.practicalcrypto.encryption.padding;

import kpi.ipt.labs.practicalcrypto.encryption.AsymmetricBlockCipher;
import kpi.ipt.labs.practicalcrypto.encryption.CipherParameters;
import kpi.ipt.labs.practicalcrypto.encryption.EncryptionUtils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author olch0615
 *         Date: 5/22/2017
 *         Time: 3:12 PM
 */
public class SimpleOAEPPadding implements AsymmetricBlockCipher {

    private final AsymmetricBlockCipher delegate;
    private final Random random;

    private final MessageDigest hashG;
    private final MessageDigest hashH;

    private final int k0;

    private final int inputBlockLength;

    private boolean forEncryption;

    public SimpleOAEPPadding(AsymmetricBlockCipher delegate, MessageDigest hashG, MessageDigest hashH, int k1) {
        this(delegate, new SecureRandom(), hashG, hashH, k1);
    }

    public SimpleOAEPPadding(AsymmetricBlockCipher delegate, Random random, MessageDigest hashG, MessageDigest hashH, int k1) {
        this.delegate = delegate;
        this.random = random;

        this.hashG = hashG;
        this.hashH = hashH;

        this.k0 = this.hashH.getDigestLength();

        int gInputLength = hashG.getDigestLength();

        if (delegate.getInputBlockSize() - k0 != gInputLength) {
            throw new IllegalArgumentException("Blocks length mismatch: "
                    + (delegate.getInputBlockSize() - k0) + " and " + gInputLength);
        }

        this.inputBlockLength = gInputLength - k1;
    }

    @Override
    public void init(boolean forEncryption, CipherParameters param) {
        this.forEncryption = forEncryption;

        this.hashG.reset();
        this.hashH.reset();

        this.delegate.init(forEncryption, param);
    }

    @Override
    public byte[] processBlock(byte[] block, int offset, int length) {
        if (forEncryption) {
            return encode(block, offset, length);
        } else {
            return decode(block, offset, length);
        }
    }

    private byte[] encode(byte[] block, int offset, int length) {
        EncryptionUtils.checkInputLengthLTE(inputBlockLength, length);

        byte[] x = new byte[hashG.getDigestLength()];
        System.arraycopy(block, offset, x, 0, length);

        byte[] y = randomBytes(k0);

        xor(x, resetAndHash(hashG, y));
        xor(y, resetAndHash(hashH, x));

        byte[] paddedMessage = EncryptionUtils.concat(x, y);

        return delegate.processBlock(paddedMessage, 0, paddedMessage.length);
    }

    private byte[] decode(byte[] block, int offset, int length) {

        int leftPartLength = hashG.getDigestLength();

        byte[] x = EncryptionUtils.slice(block, offset, leftPartLength);
        byte[] y = EncryptionUtils.slice(block, offset + leftPartLength, k0);

        xor(y, resetAndHash(hashH, x));
        xor(x, resetAndHash(hashG, y));

        checkZeroes(x);

        return delegate.processBlock(x, 0, inputBlockLength);
    }

    @Override
    public int getInputBlockSize() {
        //TODO: take forEncryption flag into account
        return inputBlockLength;
    }

    @Override
    public int getOutputBlockSize() {
        //TODO: take forEncryption flag into account
        return 0;
    }

    private byte[] randomBytes(int length) {
        byte[] randomBuffer = new byte[length];
        random.nextBytes(randomBuffer);

        return randomBuffer;
    }

    private void checkZeroes(byte[] x) {
        for (int i = inputBlockLength; i < x.length; i++) {
            if (x[i] != 0) {
                throw new IllegalStateException("Malformed message");
            }
        }
    }

    private static byte[] resetAndHash(MessageDigest digest, byte[] input) {
        digest.reset();
        return digest.digest(input);
    }

    private static void xor(byte[] accumulator, byte[] bytes) {
        for (int i = 0; i < accumulator.length; i++) {
            accumulator[i] ^= bytes[i];
        }
    }
}