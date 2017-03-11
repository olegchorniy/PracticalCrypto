package kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key;

import java.math.BigInteger;

public class ElGamalPublicKey extends ElGamalKey {

    private final BigInteger y;

    public ElGamalPublicKey(BigInteger p, BigInteger g, BigInteger y) {
        super(p, g);
        this.y = y;
    }

    public BigInteger getY() {
        return y;
    }

    @Override
    public String toString() {
        return super.toString() + ", y = " + y.toString(16);
    }
}
