package kpi.ipt.labs.practicalcrypto.encryption.padding;

import kpi.ipt.labs.practicalcrypto.encryption.AsymmetricBlockCipher;
import kpi.ipt.labs.practicalcrypto.encryption.CipherParameters;
import kpi.ipt.labs.practicalcrypto.encryption.EncryptionUtils;
import kpi.ipt.labs.practicalcrypto.utils.DigestFactory;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

/**
 * @author olch0615
 *         Date: 5/22/2017
 *         Time: 3:12 PM
 */
public class OAEPPadding implements AsymmetricBlockCipher {

    private final Random random = new Random();

    private final AsymmetricBlockCipher engine;
    private final MGF mgf;

    private final byte[] lHash;
    private final int hLen;

    private boolean forEncryption;

    public OAEPPadding(AsymmetricBlockCipher cipher) {
        this(cipher, DigestFactory.createSHA1());
    }

    public OAEPPadding(
            AsymmetricBlockCipher cipher,
            MessageDigest hash) {
        this(cipher, hash, new MGF1(hash));
    }

    public OAEPPadding(
            AsymmetricBlockCipher cipher,
            MessageDigest hash,
            MGF mgf) {
        this(cipher, hash, mgf, null);
    }

    public OAEPPadding(
            AsymmetricBlockCipher cipher,
            MessageDigest hash,
            MGF mgf,
            byte[] label) {
        this.engine = cipher;
        this.mgf = mgf;

        hash.reset();

        if (label != null) {
            hash.update(label);
        }

        this.lHash = hash.digest();
        this.hLen = lHash.length;
    }

    @Override
    public void init(boolean forEncryption, CipherParameters param) {
        this.forEncryption = forEncryption;
        this.engine.init(forEncryption, param);
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
        EncryptionUtils.checkInputLengthLTE(getInputBlockSize(), length);

        final int k = engine.getInputBlockSize();
        final int dbLen = k - hLen - 1;

        byte[] em = new byte[k];
        byte[] seed = randomBytes(hLen);

        // 00, seed, DB = lHash | 00 ... 00 | 01 | M
        System.arraycopy(lHash, 0, em, hLen + 1, hLen);
        em[k - 1 - length] = 1;
        System.arraycopy(block, offset, em, k - length, length);

        //maskedDB = DB ^ MGF(seed)
        xor(em, hLen + 1, mgf.mask(seed, dbLen), 0, dbLen);

        //maskedSeed = seed ^ maskedDB
        xor(seed, 0, mgf.mask(em, hLen + 1, dbLen, hLen), 0, hLen);
        System.arraycopy(seed, 0, em, 1, hLen);

        //EM = 00 | maskedSeed | maskedDB
        return engine.processBlock(em, 0, em.length);
    }

    private byte[] decode(byte[] block, int offset, int length) {
        byte[] decrypted = engine.processBlock(block, offset, length);

        final int k = engine.getOutputBlockSize();
        EncryptionUtils.checkInputLengthEQ(k, decrypted.length);

        final int dbLen = k - hLen - 1;

        xor(decrypted, 1, mgf.mask(decrypted, hLen + 1, dbLen, hLen), 0, hLen);
        xor(decrypted, hLen + 1, mgf.mask(decrypted, 1, hLen, dbLen), 0, dbLen);

        if (decrypted[0] != 0) {
            throw new IllegalStateException("First byte should be 0x00");
        }

        for (int i = 0; i < hLen; i++) {
            if (lHash[i] != decrypted[hLen + 1 + i]) {
                throw new IllegalStateException("Label hash mismatch the original value");
            }
        }

        // find the position where real message starts. In other words - skip padding bytes.
        int mPos;
        for (mPos = 2 * hLen + 1; mPos < k; mPos++) {
            if (decrypted[mPos] == 1) {
                mPos += 1;
                break;
            }

            if (decrypted[mPos] != 0) {
                throw new IllegalArgumentException("Invalid padding");
            }
        }

        return Arrays.copyOfRange(decrypted, mPos, k);
    }

    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return engine.getInputBlockSize() - 2 * hLen - 2;
        } else {
            return engine.getInputBlockSize();
        }
    }

    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return engine.getOutputBlockSize();
        } else {
            return engine.getOutputBlockSize() - 2 * hLen - 2;
        }
    }

    private byte[] randomBytes(int length) {
        byte[] randomBuffer = new byte[length];
        random.nextBytes(randomBuffer);

        return randomBuffer;
    }

    private static void xor(byte[] accumulator,
                            int accOffset,
                            byte[] bytes,
                            int offset,
                            int length) {
        for (int i = 0; i < length; i++) {
            accumulator[i + accOffset] ^= bytes[i + offset];
        }
    }
}