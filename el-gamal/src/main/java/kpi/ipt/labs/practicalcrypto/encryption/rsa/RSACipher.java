package kpi.ipt.labs.practicalcrypto.encryption.rsa;

import kpi.ipt.labs.practicalcrypto.encryption.AsymmetricBlockCipher;
import kpi.ipt.labs.practicalcrypto.encryption.CipherParameters;
import kpi.ipt.labs.practicalcrypto.encryption.EncryptionUtils;
import kpi.ipt.labs.practicalcrypto.rsa.key.RSAKey;
import kpi.ipt.labs.practicalcrypto.rsa.key.RSAPrivateKey;
import kpi.ipt.labs.practicalcrypto.rsa.key.RSAPublicKey;
import kpi.ipt.labs.practicalcrypto.utils.ConversionUtil;

import java.math.BigInteger;

public class RSACipher implements AsymmetricBlockCipher {

    private RSAKey key;
    private int bitSize;
    private boolean forEncryption;

    @Override
    public void init(boolean forEncryption, CipherParameters param) {
        RSACipherParameters egParams = (RSACipherParameters) param;

        this.key = egParams.getRsaKey();
        this.bitSize = this.key.getModulus().bitLength();
        this.forEncryption = forEncryption;

        if (this.forEncryption && !(this.key instanceof RSAPublicKey)) {
            throw new IllegalArgumentException("Key type doesn't match working mode");
        }
    }

    @Override
    public byte[] processBlock(byte[] block, int offset, int length) {
        EncryptionUtils.checkInputLengthLTE(getInputBlockSize(), length);

        if (forEncryption) {
            return encrypt(block, offset, length);
        } else {
            return decrypt(block, offset, length);
        }
    }


    private byte[] encrypt(byte[] data, int offset, int length) {
        RSAPublicKey publicKey = (RSAPublicKey) this.key;

        BigInteger m = ConversionUtil.fromUnsignedByteArray(data, offset, length);
        BigInteger cipherText = m.modPow(publicKey.getPublicExponent(), publicKey.getModulus());

        return ConversionUtil.packToByteArray(cipherText, getOutputBlockSize());
    }

    private byte[] decrypt(byte[] data, int offset, int length) {
        RSAPrivateKey privateKey = (RSAPrivateKey) this.key;

        BigInteger c = ConversionUtil.fromUnsignedByteArray(data, offset, length);
        BigInteger plainText = c.modPow(privateKey.getPrivateExponent(), privateKey.getModulus());

        return ConversionUtil.asUnsignedByteArray(plainText);
    }

    @Override
    public int getInputBlockSize() {
        if (forEncryption) {
            return (bitSize - 1) / 8;
        }

        return (bitSize + 7) / 8;
    }

    @Override
    public int getOutputBlockSize() {
        if (forEncryption) {
            return (bitSize + 7) / 8;
        }

        return (bitSize - 1) / 8;
    }
}
