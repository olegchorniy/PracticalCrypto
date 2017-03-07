package kpi.ipt.labs.practicalcrypto.random;

import java.math.BigInteger;

/**
 * @author Олег
 */
public class BMByteGenerator extends AbstractBM {

    private static BigInteger MULT = BigInteger.valueOf(256);
    private final BigInteger pSubOne;

    public BMByteGenerator(BigInteger g, BigInteger p) {
        super(g, p);
        this.pSubOne = p.subtract(BigInteger.ONE);
    }

    public byte nextByte() {
        BigInteger next = nextValue();
        BigInteger[] divResult = next.multiply(MULT).divideAndRemainder(pSubOne);
        BigInteger k = divResult[0];
        if (divResult[1].equals(BigInteger.ZERO)) {
            k = k.subtract(BigInteger.ONE);
        }

        return k.byteValue();
    }

    @Override
    public String toString() {
        return "BM Byte: " + super.toString();
    }
}
