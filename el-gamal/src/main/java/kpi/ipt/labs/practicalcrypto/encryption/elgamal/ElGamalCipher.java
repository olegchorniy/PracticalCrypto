package kpi.ipt.labs.practicalcrypto.encryption.elgamal;

import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKey;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalPrivateKey;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalPublicKey;
import kpi.ipt.labs.practicalcrypto.encryption.AsymmetricBlockCipher;
import kpi.ipt.labs.practicalcrypto.encryption.CipherParameters;
import kpi.ipt.labs.practicalcrypto.encryption.EncryptionUtils;
import kpi.ipt.labs.practicalcrypto.utils.ConversionUtil;
import kpi.ipt.labs.practicalcrypto.utils.RandomUtils;

import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.ONE;

public class ElGamalCipher implements AsymmetricBlockCipher {

    private static final BigInteger TWO = BigInteger.valueOf(2);

    private final Random random;

    private ElGamalKey key;
    private int bitSize;
    private boolean forEncryption;

    public ElGamalCipher() {
        this(new Random());
    }

    public ElGamalCipher(Random random) {
        this.random = random;
    }

    @Override
    public void init(boolean forEncryption, CipherParameters param) {
        ElGamalCipherParameters egParams = (ElGamalCipherParameters) param;

        this.key = egParams.getKey();
        this.bitSize = this.key.getP().bitLength();
        this.forEncryption = forEncryption;

        if (this.forEncryption && !(this.key instanceof ElGamalPublicKey)) {
            throw new IllegalArgumentException("Key type doesn't match working mode");
        }
    }

    @Override
    public byte[] processBlock(byte[] block, int offset, int length) {
        EncryptionUtils.checkInputLengthLTE(getInputBlockSize(), length);

        if (forEncryption) {
            return encrypt(block, offset, length);
        } else {
            return decrypt(block, offset, length);
        }
    }

    private byte[] encrypt(byte[] block, int offset, int length) {
        if (length > getInputBlockSize()) {
            throw new IllegalArgumentException("Input block too large for current ElGamal parameters");
        }

        BigInteger m = ConversionUtil.fromUnsignedByteArray(block, offset, length);
        BigInteger[] cipherText = encrypt((ElGamalPublicKey) key, m);

        return ConversionUtil.packToByteArray(cipherText[0], cipherText[1], getOutputBlockSize());
    }

    private BigInteger[] encrypt(ElGamalPublicKey key, BigInteger m) {

        BigInteger g = key.getG();
        BigInteger p = key.getP();
        BigInteger y = key.getY();

        BigInteger k = RandomUtils.createRandomInRange(ONE, p.subtract(TWO), random);

        //c1 = g^k (mod p)
        //c2 = y^k * M (mod p) = g^(kx) * M (mod p)

        BigInteger c1 = g.modPow(k, p);
        BigInteger c2 = y.modPow(k, p).multiply(m).mod(p);

        return new BigInteger[]{c1, c2};
    }

    private byte[] decrypt(byte[] block, int offset, int length) {
        if (length > getInputBlockSize()) {
            throw new IllegalArgumentException("Input block too large for current ElGamal parameters");
        }

        BigInteger[] c = ConversionUtil.unpackFromByteArray(block, offset, length);
        BigInteger plainText = decrypt((ElGamalPrivateKey) key, c);

        return ConversionUtil.asUnsignedByteArray(plainText);
    }

    private BigInteger decrypt(ElGamalPrivateKey key, BigInteger[] c) {
        BigInteger p = key.getP();
        BigInteger x = key.getX();

        BigInteger c1 = c[0];
        BigInteger c2 = c[1];

        //M = c2 * (c1^x)^(-1) (mod p) = g^(kx) * M * (g^(kx))^(-1) (mod p) = M
        return c2.multiply(c1.modPow(x, p).modInverse(p)).mod(p);
    }

    @Override
    public int getInputBlockSize() {
        if (forEncryption) {
            return (bitSize - 1) / 8;
        }

        return 2 * ((bitSize + 7) / 8);
    }

    @Override
    public int getOutputBlockSize() {
        if (forEncryption) {
            return 2 * ((bitSize + 7) / 8);
        }

        return (bitSize - 1) / 8;
    }
}
