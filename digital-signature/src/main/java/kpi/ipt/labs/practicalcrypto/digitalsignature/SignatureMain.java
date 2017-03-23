package kpi.ipt.labs.practicalcrypto.digitalsignature;

import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.ElGamalSignature;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.KeyStore;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalKeyPair;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalPrivateKey;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalPublicKey;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.utils.SignatureUtils;

import java.io.File;
import java.util.Random;

public class SignatureMain {

    private static final File testFile = new File("D:\\work_dir\\pract_crypt\\test_file.bin");

    public static void main(String[] args) throws Exception {
        ElGamalKeyPair keyPair = KeyStore.getKeyPair();

        byte[] signature = SignatureUtils.sign(keyPair.getPrivateKey(), testFile);
        System.out.println("Signature: " + toString(signature));

        boolean result = SignatureUtils.verify(keyPair.getPublicKey(), signature, testFile);
        System.out.println("Verification: " + result);
    }

    public static void fullTest() throws Exception {
        Random random = new Random();

        ElGamalSignature elGamal = new ElGamalSignature("SHA-256", random);

        ElGamalKeyPair keyPair = KeyStore.generateAndSave(512);
        ElGamalPrivateKey privateKey = keyPair.getPrivateKey();
        ElGamalPublicKey publicKey = keyPair.getPublicKey();

        byte[] message = {1, 2, 3, 4, 5, 6, 7, 8};

        // 1. generate signature
        elGamal.init(true, privateKey);
        elGamal.update(message);
        byte[] signature = elGamal.sign();

        System.out.print("Signature: " + toString(signature));

        // 2. verify
        elGamal.init(false, publicKey);
        elGamal.update(message);

        System.out.println("Verification: " + elGamal.verify(signature));
    }

    private static String toString(byte[] values) {
        StringBuilder builder = new StringBuilder();

        builder.append("[");

        for (int i = 0; i < values.length; i++) {
            if (i != 0) {
                builder.append(", ");
            }

            builder.append(Integer.toHexString(values[i] & 0xFF));
        }

        builder.append("]");

        return builder.toString();
    }
}
