package kpi.ipt.labs.practicalcrypto.benchmarks;

import kpi.ipt.labs.practicalcrypto.primes.MaurerAlgorithm;
import org.openjdk.jmh.annotations.*;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5)
@Measurement(iterations = 10)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PrimeGenerationBenchmark {

    @State(Scope.Benchmark)
    public static class PrimeState {
        @Param({"256", "512", "1024", "2048"})
        int bitLength;

        Random random = new Random();
        MaurerAlgorithm maurerAlgorithm = new MaurerAlgorithm(this.random);
    }

    @Benchmark
    public BigInteger measureProbablePrime(PrimeState state) {
        return BigInteger.probablePrime(state.bitLength, state.random);
    }

    @Benchmark
    public BigInteger measureProvablePrime(PrimeState state) {
        return state.maurerAlgorithm.generatePrime(state.bitLength);
    }
}
