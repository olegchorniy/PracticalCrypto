package kpi.ipt.labs.practicalcrypto.encryption;

public class Cipher {

    private final AsymmetricBlockCipher delegate;

    private final byte[] buff;
    private int buffOffset;

    public Cipher(AsymmetricBlockCipher delegate) {
        this.delegate = delegate;
        this.buff = new byte[delegate.getInputBlockSize()];
    }

    public int update(byte[] input, int offset, int length,
                      byte[] output, int outputOffset) {

        final int inputBlockSize = delegate.getInputBlockSize();
        final int outputBlockSize = delegate.getOutputBlockSize();

        final int totalInputLen = length + buffOffset;

        if (totalInputLen < inputBlockSize) {
            copyToBuffer(input, offset, length);

            return 0;
        }

        final int outputBlocks = totalInputLen / inputBlockSize;
        final int outputLength = outputBlocks * outputBlockSize;

        if (outputLength > output.length - outputOffset) {
            throw new IllegalStateException("Output buffer doesn't have enough space");
        }

        for (int i = 0; i < outputBlocks; i++) {
            offset += fillBuffer(input, offset);

            //TODO: we can avoid unnecessary copying from input buffer to internal buffer

            byte[] processed = delegate.processBlock(buff, 0, buff.length);
            System.arraycopy(processed, 0, output, outputOffset, processed.length);

            buffOffset = 0;
            outputOffset += processed.length;
        }

        int remainedBytes = totalInputLen - outputBlocks * inputBlockSize;
        copyToBuffer(input, offset, remainedBytes);

        return outputLength;
    }

    private int fillBuffer(byte[] input, int offset) {
        int bytesToCopy = delegate.getInputBlockSize() - buffOffset;
        copyToBuffer(input, offset, bytesToCopy);

        return bytesToCopy;
    }

    private void copyToBuffer(byte[] input, int offset, int length) {
        System.arraycopy(input, offset, buff, buffOffset, length);
        buffOffset += length;
    }

    public byte[] doFinal() {
        return null;
    }
}
