package kpi.ipt.labs.practicalcrypto.elgamal.utils;

import kpi.ipt.labs.practicalcrypto.elgamal.signature.ElGamalSignature;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalPrivateKey;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalPublicKey;

import java.io.*;

public final class SignatureUtils {

    private static final int BUFF_SIZE = 8192;

    private SignatureUtils() {
    }

    public static byte[] sign(ElGamalPrivateKey privateKey, File file) throws IOException {
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file))) {
            return sign(privateKey, fileStream);
        }
    }

    public static byte[] sign(ElGamalPrivateKey privateKey, InputStream is) throws IOException {
        ElGamalSignature elGamal = new ElGamalSignature();
        elGamal.initSign(privateKey);

        consumeBytes(is, elGamal::update);

        return elGamal.sign();
    }

    public static boolean verify(ElGamalPublicKey publicKey, byte[] signature, File file) throws IOException {
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file))) {
            return verify(publicKey, signature, fileStream);
        }
    }

    public static boolean verify(ElGamalPublicKey publicKey, byte[] signature, InputStream is) throws IOException {
        ElGamalSignature elGamal = new ElGamalSignature();
        elGamal.initVerify(publicKey);

        consumeBytes(is, elGamal::update);

        return elGamal.verify(signature);
    }

    private static void consumeBytes(InputStream is, BytesConsumer consumer) throws IOException {
        byte[] buffer = new byte[BUFF_SIZE];
        int read;

        while ((read = is.read(buffer)) != -1) {
            consumer.consume(buffer, 0, read);
        }
    }

    private interface BytesConsumer {
        void consume(byte[] bytes, int offset, int length);
    }
}
