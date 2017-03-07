package kpi.ipt.labs.practicalcrypto.random;

import java.math.BigInteger;

/**
 * @author Олег
 */
public class BMBitGenerator extends AbstractBM {

    private final BigInteger halfP;

    public BMBitGenerator(BigInteger g, BigInteger p) {
        super(g, p);
        this.halfP = p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
    }

    public int nextBit() {
        return nextValue().compareTo(halfP) < 0 ? 1 : 0;
    }

    public byte nextByte() {
        byte next = 0;

        for (int i = 0; i < 8; i++) {
            next |= (nextBit() << (7 - i));
        }

        return next;
    }

    @Override
    public String toString() {
        return "BM Bit: " + super.toString();
    }
}
