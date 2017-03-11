package kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key;

import java.io.Serializable;
import java.math.BigInteger;

abstract public class ElGamalKey implements Serializable {

    private final BigInteger g;
    private final BigInteger p;

    public ElGamalKey(BigInteger p, BigInteger g) {
        this.p = p;
        this.g = g;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getP() {
        return p;
    }

    @Override
    public String toString() {
        return "p = " + p.toString(16) + ", g = " + g.toString(16);
    }
}
