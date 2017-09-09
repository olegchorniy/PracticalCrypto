package kpi.ipt.labs.practicalcrypto.rsa.key;

import java.io.Serializable;

/**
 * Created by Олег on 18.09.2015.
 */
public class RSAKeyPair implements Serializable {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public RSAKeyPair(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }
}
