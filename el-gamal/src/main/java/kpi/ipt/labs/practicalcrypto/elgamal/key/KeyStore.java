package kpi.ipt.labs.practicalcrypto.elgamal.key;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class KeyStore {

    private static final Path keyPath = Paths.get("D:", "work_dir", "pract_crypt", "key.bin");

    public static ElGamalKeyPair getOrGenerateAndSave(int bitLength) throws IOException {
        if (!Files.exists(keyPath)) {
            return generateAndSave(bitLength);
        }

        ElGamalKeyPair keyPair = getKeyPair();
        if (keyPair.getPublicKey().getP().bitLength() == bitLength) {
            return keyPair;
        }

        return generateAndSave(bitLength);
    }

    public static ElGamalKeyPair generateAndSave(int bitLength) throws IOException {
        Random random = new Random();

        ElGamalKeyGenerator keyGenerator = new ElGamalKeyGenerator(random);
        ElGamalKeyPair keyPair = keyGenerator.generateKeyPair(bitLength);

        serialize(keyPair);

        return keyPair;
    }

    private static void serialize(ElGamalKeyPair keyPair) throws IOException {
        try (OutputStream out = Files.newOutputStream(keyPath);
             ObjectOutputStream oos = new ObjectOutputStream(out)) {

            oos.writeObject(keyPair);
        }
    }

    public static ElGamalKeyPair getKeyPair() throws IOException {
        try (InputStream in = Files.newInputStream(keyPath);
             ObjectInputStream ois = new ObjectInputStream(in)) {

            return (ElGamalKeyPair) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
