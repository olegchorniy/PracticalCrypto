package kpi.ipt.labs.practicalcrypto.digitalsignature;

import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.ElGamalSignature;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalKeyGenerator;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalKeyPair;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalPrivateKey;
import kpi.ipt.labs.practicalcrypto.digitalsignature.elgamal.key.ElGamalPublicKey;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SignatureMain {

    private static final Path keyStore = Paths.get("D:", "work_dir", "pract_crypt", "key.bin");

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Random random = new Random();

        ElGamalSignature elGamal = new ElGamalSignature("SHA-256", random);

        generateNewKey(512);
        ElGamalKeyPair keyPair = deserialize();
        ElGamalPrivateKey privateKey = keyPair.getPrivateKey();
        ElGamalPublicKey publicKey = keyPair.getPublicKey();

        byte[] message = {1, 2, 3, 4, 5, 6, 7, 8};

        // 1. generate signature
        elGamal.init(true, privateKey);
        elGamal.update(message);
        byte[] signature = elGamal.sign();

        System.out.print("Signature: ");
        print(signature);

        // 2. verify
        elGamal.init(false, publicKey);
        elGamal.update(message);

        System.out.println("Verdict: " + elGamal.verify(signature));
    }

    public static void generateNewKey(int bitLength) {
        Random random = new Random();

        ElGamalKeyGenerator keyGenerator = new ElGamalKeyGenerator(random);
        ElGamalKeyPair keyPair = keyGenerator.generateKeyPair(bitLength);

        serialize(keyPair);
    }

    private static void serialize(ElGamalKeyPair keyPair) {
        try (OutputStream out = Files.newOutputStream(keyStore);
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(keyPair);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ElGamalKeyPair deserialize() {
        try (InputStream in = Files.newInputStream(keyStore);
             ObjectInputStream ois = new ObjectInputStream(in)) {
            return (ElGamalKeyPair) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void print(byte[] values) {
        System.out.print("[");

        for (int i = 0; i < values.length; i++) {
            if (i != 0) {
                System.out.print(", ");
            }

            System.out.print(Integer.toHexString(values[i] & 0xFF));
        }

        System.out.println("]");
    }
}
