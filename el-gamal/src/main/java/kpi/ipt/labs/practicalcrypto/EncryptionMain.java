package kpi.ipt.labs.practicalcrypto;

import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKeyGenerator;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKeyPair;
import kpi.ipt.labs.practicalcrypto.encryption.Cipher;
import kpi.ipt.labs.practicalcrypto.encryption.elgamal.ElGamalCipher;
import kpi.ipt.labs.practicalcrypto.encryption.elgamal.ElGamalCipherParameters;
import kpi.ipt.labs.practicalcrypto.encryption.padding.OAEPPadding;
import kpi.ipt.labs.practicalcrypto.utils.DigestFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;

public class EncryptionMain {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        //AuthProvider provider = new SunPKCS11("");

        //System.out.println(instance);

        MessageDigest md5 = DigestFactory.createMD5();
        MessageDigest sha1 = DigestFactory.createSHA1();

        //AsymmetricBlockCipher cipher = new OAEPPadding(new NoOpCipher(50, 50), sha1, new MGF1(sha1), null);
        ElGamalKeyGenerator keyGen = new ElGamalKeyGenerator();
        ElGamalKeyPair keyPair = keyGen.generateKeyPair(129);

        Cipher cipher = new Cipher(new OAEPPadding(new ElGamalCipher()));

        byte[] block = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16/*,
                17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32*/
        };

        cipher.init(true, new ElGamalCipherParameters(keyPair.getPublicKey()));
        byte[] e1 = cipher.update(block);
        byte[] e2 = cipher.doFinal();

        cipher.init(false, new ElGamalCipherParameters(keyPair.getPrivateKey()));
        byte[] d1 = cipher.update(e1);
        byte[] d2 = cipher.update(e2);
        byte[] d3 = cipher.doFinal();

        System.out.println(print(block));
        System.out.println();

        System.out.println(print(e1));
        System.out.println(print(e2));
        System.out.println();

        System.out.println(print(d1));
        System.out.println(print(d2));
        System.out.println(print(d3));
    }

    public static String print(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(Byte.toUnsignedInt(bytes[i]));

            if (hex.length() < 2) {
                builder.append('0');
            }
            builder.append(hex);

            if (i != bytes.length - 1) {
                builder.append(' ');
            }
        }

        return builder.length() == 0 ? "[empty]" : builder.toString();
    }
}
