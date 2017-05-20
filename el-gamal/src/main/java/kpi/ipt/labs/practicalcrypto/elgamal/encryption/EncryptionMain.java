package kpi.ipt.labs.practicalcrypto.elgamal.encryption;

import sun.security.pkcs11.SunPKCS11;

import java.security.AuthProvider;

public class EncryptionMain {

    public static void main(String[] args) {
        AuthProvider provider = new SunPKCS11("");
    }
}
