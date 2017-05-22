package kpi.ipt.labs.practicalcrypto;

import kpi.ipt.labs.practicalcrypto.encryption.AsymmetricBlockCipher;
import kpi.ipt.labs.practicalcrypto.encryption.NoOpCipher;
import kpi.ipt.labs.practicalcrypto.encryption.padding.OAEPPadding;
import kpi.ipt.labs.practicalcrypto.utils.DigestFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

public class EncryptionMain {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        //AuthProvider provider = new SunPKCS11("");

        //System.out.println(instance);

        MessageDigest md5 = DigestFactory.createMD5();
        MessageDigest sha1 = DigestFactory.createSHA1();

        AsymmetricBlockCipher cipher = new OAEPPadding(new NoOpCipher(36, 36), sha1, md5, 10);

        byte[] block = {1, 2, 3, 4, 5};

        cipher.init(true, null);
        byte[] encoded = cipher.processBlock(block, 0, block.length);

        cipher.init(false, null);
        byte[] decoded = cipher.processBlock(encoded, 0, encoded.length);

        System.out.println(Arrays.toString(block));
        System.out.println(Arrays.toString(encoded));
        System.out.println(Arrays.toString(decoded));
    }
}
