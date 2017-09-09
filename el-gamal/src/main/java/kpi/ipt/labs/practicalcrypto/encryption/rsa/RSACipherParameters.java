package kpi.ipt.labs.practicalcrypto.encryption.rsa;

import kpi.ipt.labs.practicalcrypto.encryption.CipherParameters;
import kpi.ipt.labs.practicalcrypto.rsa.key.RSAKey;

public class RSACipherParameters implements CipherParameters {

    private final RSAKey rsaKey;

    public RSACipherParameters(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    public RSAKey getRsaKey() {
        return rsaKey;
    }
}
