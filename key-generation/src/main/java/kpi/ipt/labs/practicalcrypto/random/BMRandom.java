package kpi.ipt.labs.practicalcrypto.random;

import java.math.BigInteger;
import java.util.Random;

public class BMRandom extends Random {

    private final BMBitGenerator bmGenerator;

    public BMRandom(BigInteger g, BigInteger p) {
        this.bmGenerator = new BMBitGenerator(g, p);
    }

    public BMRandom(BMBitGenerator bmGenerator) {
        this.bmGenerator = bmGenerator;
    }

    @Override
    protected int next(int bits) {
        int result = 0;
        for (int i = 0; i < bits; i++) {
            result |= (bmGenerator.nextBit() << i);
        }

        return result;
    }

    @Override
    public void nextBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) next(8);
        }
    }
}
