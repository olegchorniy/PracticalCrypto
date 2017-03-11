package kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal;

import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalKey;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalPrivateKey;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalPublicKey;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.utils.ConversionUtil;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.utils.RandomUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.math.BigInteger.ONE;

public class ElGamalSignature {

    private static final BigInteger TWO = BigInteger.valueOf(2);

    private final MessageDigest digest;
    private final Random random;
    private ElGamalKey key;

    public ElGamalSignature(String digestAlgorithm, Random random) {
        this.random = random;
        try {
            this.digest = MessageDigest.getInstance(digestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unknown message digest algorithm [" + digestAlgorithm + "]", e);
        }
    }

    public void init(boolean forSigning, ElGamalKey key) {
        if (forSigning) {
            if (!(key instanceof ElGamalPrivateKey)) {
                throw new IllegalArgumentException("Private key required for signing");
            }
        } else {
            if (!(key instanceof ElGamalPublicKey)) {
                throw new IllegalArgumentException("Public key required for verification");
            }
        }

        this.digest.reset();
        this.key = key;
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
        System.out.println("[Signing] h = " + h.toString(16));

        BigInteger pMinusOne = p.subtract(ONE);
        BigInteger k = selectK(pMinusOne, this.random);

        BigInteger r = g.modPow(k, p);
        //s = k^(-1) * (h(m) - x * r) mod (p - 1)
        BigInteger s = k.modInverse(pMinusOne).multiply(h.subtract(x.multiply(r))).mod(pMinusOne);

        System.out.println("[Signing] r = " + r.toString(16));
        System.out.println("[Signing] s = " + s.toString(16));

        return packToByteArray(r, s);
    }

    public boolean verify(byte[] signature) {
        ElGamalPublicKey publicKey = (ElGamalPublicKey) this.key;

        BigInteger p = publicKey.getP();
        BigInteger g = publicKey.getG();
        BigInteger y = publicKey.getY();

        byte[] digest = this.digest.digest();
        BigInteger h = ConversionUtil.fromUnsignedByteArray(trimToLength(digest, p));
        System.out.println("[Verification] h = " + h.toString(16));

        BigInteger[] sig = unpackFromByteArray(signature);
        BigInteger r = sig[0];
        BigInteger s = sig[1];

        System.out.println("[Verification] r = " + r.toString(16));
        System.out.println("[Verification] s = " + s.toString(16));

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

    private static byte[] packToByteArray(BigInteger r, BigInteger s) {
        byte[] rArray = ConversionUtil.asUnsignedByteArray(r);
        byte[] sArray = ConversionUtil.asUnsignedByteArray(s);

        int maxLength = Integer.max(rArray.length, sArray.length);

        byte[] targetArray = new byte[maxLength * 2];
        System.arraycopy(rArray, 0, targetArray, 0, rArray.length);
        System.arraycopy(sArray, 0, targetArray, maxLength, sArray.length);

        return targetArray;
    }

    private static BigInteger[] unpackFromByteArray(byte[] array) {
        if ((array.length & 1) != 0) {
            throw new IllegalArgumentException("Signature array should have even length");
        }

        int halfLen = array.length / 2;

        byte[] rArray = new byte[halfLen];
        byte[] sArray = new byte[halfLen];

        System.arraycopy(array, 0, rArray, 0, halfLen);
        System.arraycopy(array, halfLen, sArray, 0, halfLen);

        BigInteger r = ConversionUtil.fromUnsignedByteArray(rArray);
        BigInteger s = ConversionUtil.fromUnsignedByteArray(sArray);

        return new BigInteger[]{r, s};
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
