package kpi.ipt.labs.practicalcrypto.encryption.elgamal;

import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKey;
import kpi.ipt.labs.practicalcrypto.encryption.CipherParameters;

public class ElGamalCipherParameters implements CipherParameters {

    private final ElGamalKey key;

    public ElGamalCipherParameters(ElGamalKey key) {
        this.key = key;
    }

    public ElGamalKey getKey() {
        return key;
    }
}
