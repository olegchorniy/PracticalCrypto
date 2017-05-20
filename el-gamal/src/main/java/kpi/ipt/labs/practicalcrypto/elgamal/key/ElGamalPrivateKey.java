package kpi.ipt.labs.practicalcrypto.elgamal.key;

import java.math.BigInteger;

public class ElGamalPrivateKey extends ElGamalKey {

    private final BigInteger x;

    public ElGamalPrivateKey(BigInteger p, BigInteger g, BigInteger x) {
        super(p, g);
        this.x = x;
    }

    public BigInteger getX() {
        return x;
    }

    @Override
    public String toString() {
        return super.toString() + ", x = " + x.toString(16);
    }
}
