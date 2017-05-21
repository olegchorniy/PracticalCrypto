package kpi.ipt.labs.practicalcrypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Arrays;

public class EncryptionMain {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //AuthProvider provider = new SunPKCS11("");

        //System.out.println(instance);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("ELGAMAL");

        generator.initialize(127);
        KeyPair keyPair = generator.generateKeyPair();

        Cipher instance = Cipher.getInstance("ELGAMAL/NONE/OAEPPadding");
        instance.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());

        System.out.println(Arrays.toString(instance.update(new byte[]{1,})));
        System.out.println(Arrays.toString(instance.update(new byte[]{1, 2})));
        System.out.println(Arrays.toString(instance.update(new byte[]{1, 2, 3})));
        System.out.println(Arrays.toString(instance.doFinal()));
    }
}
