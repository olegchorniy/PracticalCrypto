package kpi.ipt.labs.practicalcrypto.encryption;

import java.util.Arrays;

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

            byte[] processedBytes;

            if (buffOffset != 0) {
                offset += fillBuffer(input, offset);
                processedBytes = delegate.processBlock(buff, 0, buff.length);

                cleanBuffer();
            } else {
                processedBytes = delegate.processBlock(input, offset, inputBlockSize);
                offset += inputBlockSize;
            }

            System.arraycopy(processedBytes, 0, output, outputOffset, processedBytes.length);
            outputOffset += processedBytes.length;
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

    private void cleanBuffer() {
        Arrays.fill(buff, (byte) 0);
        buffOffset = 0;
    }

    public byte[] doFinal() {
        return null;
    }
}
