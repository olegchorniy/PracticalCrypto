package kpi.ipt.labs.practicalcrypto.rsa.key;

import java.io.Serializable;
import java.math.BigInteger;

public class RSAPrivateKey extends RSAKey implements Serializable {

    private BigInteger privateExponent;

    public RSAPrivateKey() {
    }

    public RSAPrivateKey(BigInteger modulus, BigInteger privateExponent) {
        super(modulus);
        this.privateExponent = privateExponent;
    }

    public void setPrivateExponent(BigInteger privateExponent) {
        this.privateExponent = privateExponent;
    }

    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }
}