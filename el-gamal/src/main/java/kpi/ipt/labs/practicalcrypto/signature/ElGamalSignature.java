package kpi.ipt.labs.practicalcrypto.signature;

import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKey;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalPrivateKey;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalPublicKey;
import kpi.ipt.labs.practicalcrypto.utils.ConversionUtil;
import kpi.ipt.labs.practicalcrypto.utils.DigestFactory;
import kpi.ipt.labs.practicalcrypto.utils.RandomUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

import static java.math.BigInteger.ONE;

public class ElGamalSignature {

    private final MessageDigest digest;
    private final Random random;
    private ElGamalKey key;

    public ElGamalSignature() {
        this(new Random());
    }

    public ElGamalSignature(Random random) {
        this(DigestFactory.createSHA1(), random);
    }

    public ElGamalSignature(String digestAlgorithm, Random random) {
        this(DigestFactory.createDigestInstance(digestAlgorithm), random);
    }

    public ElGamalSignature(MessageDigest digest, Random random) {
        this.digest = digest;
        this.random = random;
    }

    public void initSign(ElGamalPrivateKey key) {
        this.digest.reset();
        this.key = key;
    }

    public void initVerify(ElGamalPublicKey key) {
        this.digest.reset();
        this.key = key;
    }

    public void update(byte[] bytes, int offset, int length) {
        this.digest.update(bytes, offset, length);
    }

    public void update(byte[] bytes) {
        this.digest.update(bytes);
    }

    public byte[] sign() {
        ElGamalPrivateKey privateKey = (ElGamalPrivateKey) this.key;

        BigInteger p = privateKey.getP();
        BigInteger g = privateKey.getG();
        BigInteger x = privateKey.getX();

        byte[] digest = this.digest.digest();
        BigInteger h = ConversionUtil.fromUnsignedByteArray(trimToLength(digest, p));

        BigInteger pMinusOne = p.subtract(ONE);
        BigInteger k = selectK(pMinusOne, this.random);

        BigInteger r = g.modPow(k, p);
        //s = k^(-1) * (h(m) - x * r) mod (p - 1)
        BigInteger s = k.modInverse(pMinusOne).multiply(h.subtract(x.multiply(r))).mod(pMinusOne);

        return ConversionUtil.packToByteArray(r, s);
    }

    public boolean verify(byte[] signature) {
        ElGamalPublicKey publicKey = (ElGamalPublicKey) this.key;

        BigInteger p = publicKey.getP();
        BigInteger g = publicKey.getG();
        BigInteger y = publicKey.getY();

        byte[] digest = this.digest.digest();
        BigInteger h = ConversionUtil.fromUnsignedByteArray(trimToLength(digest, p));

        BigInteger[] sig = ConversionUtil.unpackFromByteArray(signature);
        BigInteger r = sig[0];
        BigInteger s = sig[1];

        //v1 = y^r * r^s mod p
        BigInteger v1 = y.modPow(r, p).multiply(r.modPow(s, p)).mod(p);

        //v2 = g^h(m) mod p
        BigInteger v2 = g.modPow(h, p);

        return v1.equals(v2);
    }

    private static byte[] trimToLength(byte[] array, BigInteger p) {
        // -1 necessary to be sure that recovered BigInteger will be definitely smaller than p
        int byteLength = (p.bitLength() - 1) / Byte.SIZE;

        if (array.length <= byteLength) {
            return array;
        }

        byte[] trimmed = new byte[byteLength];
        System.arraycopy(array, 0, trimmed, 0, byteLength);

        return trimmed;
    }

    private static BigInteger selectK(BigInteger pMinusOne, Random random) {
        BigInteger pMinusTwo = pMinusOne.subtract(ONE);

        BigInteger k;

        do {
            k = RandomUtils.createRandomInRange(ONE, pMinusTwo, random);
        } while (!k.gcd(pMinusOne).equals(ONE));

        return k;
    }
}
