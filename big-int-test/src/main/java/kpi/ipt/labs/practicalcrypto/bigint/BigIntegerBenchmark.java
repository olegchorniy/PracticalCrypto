package kpi.ipt.labs.practicalcrypto.bigint;

import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5)
@Measurement(iterations = 10)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BigIntegerBenchmark {

    private static final BigInteger POW = BigInteger.valueOf(65537);

    @State(Scope.Benchmark)
    public static class EqualLengthPair {

        @Param({"128", "256", "512", "1024", "2048", "4096"})
        int bitLength;

        BigInteger first;
        BigInteger second;

        @Setup
        public void setup() {
            this.first = exactLengthRandomBigInteger(this.bitLength);
            this.second = exactLengthRandomBigInteger(this.bitLength);
        }
    }

    @State(Scope.Benchmark)
    public static class DifferentLengthPair {

        @Param({"128", "256", "512", "1024", "2048", "4096"})
        int longerBitLength;

        BigInteger shorter;
        BigInteger longer;

        @Setup
        public void setup() {
            this.shorter = exactLengthRandomBigInteger(this.longerBitLength / 2);
            this.longer = exactLengthRandomBigInteger(this.longerBitLength);
        }
    }

    @State(Scope.Benchmark)
    public static class DifferentLengthTriple extends DifferentLengthPair {

        BigInteger secondShorter;

        @Override
        @Setup
        public void setup() {
            super.setup();
            this.secondShorter = exactLengthRandomBigInteger(this.longerBitLength / 2);
        }
    }

    @State(Scope.Benchmark)
    public static class PrimeModulusPair {

        @Param({"128", "256", "512", "1024", "2048", "4096"})
        int longerBitLength;

        BigInteger base;
        BigInteger primeMod;

        @Setup
        public void setup() {
            this.base = exactLengthRandomBigInteger(this.longerBitLength / 2);
            this.primeMod = BigInteger.probablePrime(this.longerBitLength, new Random());
        }
    }

    private static BigInteger exactLengthRandomBigInteger(int length) {
        return new BigInteger(length, new Random()).setBit(length - 1);
    }

    @Benchmark
    public BigInteger measureAddition(EqualLengthPair pair) {
        return pair.first.add(pair.second);
    }

    @Benchmark
    public BigInteger measureMultiplication(EqualLengthPair pair) {
        return pair.first.multiply(pair.second);
    }

    @Benchmark
    public BigInteger[] measureDivision(DifferentLengthPair pair) {
        return pair.longer.divideAndRemainder(pair.shorter);
    }

    @Benchmark
    public BigInteger measureFixedPowModularExponentiation(DifferentLengthPair pair) {
        return pair.shorter.modPow(POW, pair.longer);
    }

    @Benchmark
    public BigInteger measureVariableLengthModularExponentiation(DifferentLengthTriple triple) {
        return triple.shorter.modPow(triple.secondShorter, triple.longer);
    }

    @Benchmark
    public BigInteger measureModInverse(PrimeModulusPair pair) {
        return pair.base.modInverse(pair.primeMod);
    }
}