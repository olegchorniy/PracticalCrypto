package kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key;

import java.io.Serializable;

public class ElGamalKeyPair implements Serializable {

    private final ElGamalPublicKey publicKey;
    private final ElGamalPrivateKey privateKey;

    public ElGamalKeyPair(ElGamalPublicKey publicKey, ElGamalPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public ElGamalPublicKey getPublicKey() {
        return publicKey;
    }

    public ElGamalPrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String toString() {
        return "Public: " + publicKey.toString() + ", Private: " + privateKey.toString();
    }
}
