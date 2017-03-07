package kpi.ipt.labs.practicalcrypto.random;

import java.math.BigInteger;
import java.util.Formatter;

abstract public class AbstractBM {

    protected final BigInteger g;
    protected final BigInteger p;
    private BigInteger current;

    public AbstractBM(BigInteger g, BigInteger p) {
        this(BigInteger.valueOf(System.currentTimeMillis()), g, p);
    }

    public AbstractBM(BigInteger seed, BigInteger g, BigInteger p) {
        this.g = g;
        this.p = p;
        this.current = seed.mod(p);
    }

    protected final BigInteger nextValue() {
        current = g.modPow(current, p);
        return current;
    }

    @Override
    public String toString() {
        return new Formatter()
                .format("(p = %s, g = %s)", p.toString(16), g.toString(16))
                .toString();
    }
}
