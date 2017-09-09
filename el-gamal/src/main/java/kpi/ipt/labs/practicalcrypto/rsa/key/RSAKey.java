package kpi.ipt.labs.practicalcrypto.rsa.key;

import java.math.BigInteger;

public abstract class RSAKey {

    private BigInteger modulus;

    public RSAKey() {
    }

    public RSAKey(BigInteger modulus) {
        this.modulus = modulus;
    }

    public BigInteger getModulus() {
        return modulus;
    }

    public void setModulus(BigInteger modulus) {
        this.modulus = modulus;
    }
}
