package kpi.ipt.labs.practicalcrypto.rsa.key;

import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.ONE;

public class RSAKeyGenerator {

    private static final BigInteger E = BigInteger.valueOf(65537);

    private final Random random;

    public RSAKeyGenerator() {
        this(new Random());
    }

    public RSAKeyGenerator(Random random) {
        this.random = random;
    }

    public RSAKeyPair generateKeyPair(int keySize) {
        int pBitLength = keySize >> 1;
        int qBitLength = keySize - pBitLength;

        BigInteger primeP;
        BigInteger primeQ;
        BigInteger modulus;

        do {
            primeP = BigInteger.probablePrime(pBitLength, random);
            primeQ = BigInteger.probablePrime(qBitLength, random);

            modulus = primeP.multiply(primeQ);
        } while (modulus.bitLength() < keySize);

        BigInteger phi = primeP.subtract(ONE).multiply(primeQ.subtract(ONE));
        BigInteger publicExponent = E;
        BigInteger privateExponent = publicExponent.modInverse(phi);

        return new RSAKeyPair(
                new RSAPrivateKey(modulus, privateExponent),
                new RSAPublicKey(modulus, publicExponent)
        );
    }
}
