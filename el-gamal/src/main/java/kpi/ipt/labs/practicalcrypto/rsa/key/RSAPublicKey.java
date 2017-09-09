package kpi.ipt.labs.practicalcrypto.rsa.key;

import java.io.Serializable;
import java.math.BigInteger;


public class RSAPublicKey extends RSAKey implements Serializable {

    private BigInteger publicExponent;

    public RSAPublicKey() {
    }

    public RSAPublicKey(BigInteger modulus, BigInteger publicExponent) {
        super(modulus);
        this.publicExponent = publicExponent;
    }

    public void setPublicExponent(BigInteger publicExponent) {
        this.publicExponent = publicExponent;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
}
