package kpi.ipt.labs.practicalcrypto.encryption;

import java.util.Arrays;

public class Cipher {

    private static final byte[] EMPTY_BUFFER = new byte[0];

    private final AsymmetricBlockCipher engine;

    private byte[] buff;
    private int buffOffset;

    public Cipher(AsymmetricBlockCipher engine) {
        this.engine = engine;
    }

    public void init(boolean forEncryption, CipherParameters parameters) {
        this.engine.init(forEncryption, parameters);
        this.buff = new byte[this.engine.getInputBlockSize()];
    }

    public byte[] update(byte[] input) {
        return update(input, 0, input.length);
    }

    public byte[] update(byte[] input, int offset, int length) {
        final int inputBlockSize = engine.getInputBlockSize();
        final int outputBlockSize = engine.getOutputBlockSize();

        final int totalInputLen = length + buffOffset;

        if (totalInputLen < inputBlockSize) {
            copyToBuffer(input, offset, length);

            return EMPTY_BUFFER;
        }

        final int outputBlocks = totalInputLen / inputBlockSize;
        final int outputLength = outputBlocks * outputBlockSize;

        byte[] outputBuffer = new byte[outputLength];

        doUpdate(input, offset, length, outputBuffer, 0);

        return outputBuffer;
    }

    public int update(byte[] input, int offset, int length,
                      byte[] output, int outputOffset) {

        final int inputBlockSize = engine.getInputBlockSize();
        final int outputBlockSize = engine.getOutputBlockSize();

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

        doUpdate(input, offset, length, output, outputOffset);

        return outputLength;
    }

    private void doUpdate(byte[] input, int offset, int length,
                          byte[] output, int outputOffset) {

        final int inputBlockSize = engine.getInputBlockSize();
        final int totalInputLength = buffOffset + length;
        final int outputBlocks = totalInputLength / inputBlockSize;

        for (int i = 0; i < outputBlocks; i++) {

            byte[] processedBytes;

            if (buffOffset != 0) {
                offset += fillBuffer(input, offset);
                processedBytes = engine.processBlock(buff, 0, buff.length);

                cleanBuffer();
            } else {
                processedBytes = engine.processBlock(input, offset, inputBlockSize);
                offset += inputBlockSize;
            }

            System.arraycopy(processedBytes, 0, output, outputOffset, processedBytes.length);
            outputOffset += processedBytes.length;
        }

        int remainedBytes = totalInputLength - outputBlocks * inputBlockSize;
        copyToBuffer(input, offset, remainedBytes);
    }

    private int fillBuffer(byte[] input, int offset) {
        int bytesToCopy = engine.getInputBlockSize() - buffOffset;
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

        if (buffOffset == 0) {
            return EMPTY_BUFFER;
        }

        byte[] finalBlock = engine.processBlock(buff, 0, buffOffset);
        buffOffset = 0;

        return finalBlock;
    }
}
