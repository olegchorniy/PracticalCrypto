package kpi.ipt.labs.practicalcrypto;

import kpi.ipt.labs.practicalcrypto.encryption.AsymmetricBlockCipher;
import kpi.ipt.labs.practicalcrypto.encryption.NoOpCipher;
import kpi.ipt.labs.practicalcrypto.encryption.padding.MGF1;
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

        AsymmetricBlockCipher cipher = new OAEPPadding(new NoOpCipher(50, 50), sha1, new MGF1(sha1), null);

        byte[] block = {1, 2, 4, 5};

        cipher.init(true, null);
        byte[] encoded = cipher.processBlock(block, 0, block.length);

        cipher.init(false, null);
        byte[] decoded = cipher.processBlock(encoded, 0, encoded.length);

        System.out.println(print(block));
        System.out.println(print(encoded));
        System.out.println(print(decoded));
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

        return builder.toString();
    }
}
