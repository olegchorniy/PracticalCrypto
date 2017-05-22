package kpi.ipt.labs.practicalcrypto.encryption;

public class NoOpCipher implements AsymmetricBlockCipher {

    private final int inputLength;
    private final int outputLength;

    private boolean forEncryption;

    public NoOpCipher(int inputLength, int outputLength) {
        this.inputLength = inputLength;
        this.outputLength = outputLength;
    }

    @Override
    public void init(boolean forEncryption, CipherParameters param) {
        this.forEncryption = forEncryption;
    }

    @Override
    public byte[] processBlock(byte[] block, int offset, int length) {
        byte[] output = new byte[forEncryption ? inputLength : outputLength];

        System.arraycopy(block, offset, output, 0, Math.min(length, output.length));

        return output;
    }

    @Override
    public int getInputBlockSize() {
        return inputLength;
    }

    @Override
    public int getOutputBlockSize() {
        return outputLength;
    }
}