package kpi.ipt.labs.practicalcrypto.elgamal.signature;


import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKeyGenerator;
import kpi.ipt.labs.practicalcrypto.elgamal.key.ElGamalKeyPair;
import kpi.ipt.labs.practicalcrypto.elgamal.utils.SignatureUtils;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5)
@Measurement(iterations = 10)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ElGamalSignatureBenchmark {

    @State(Scope.Benchmark)
    public static class GenerationState {
        @Param({"512", "1024"})
        public int keyLength;

        public ElGamalKeyGenerator keyGenerator = new ElGamalKeyGenerator(new Random());
    }

    @Benchmark
    public ElGamalKeyPair keyPairBenchmark(GenerationState generationState) {
        return generationState.keyGenerator.generateKeyPair(generationState.keyLength);
    }

    @State(Scope.Benchmark)
    public static class SigningState {

        @Param({"512", "1024"})
        public int keyLength;

        public ElGamalKeyPair keyPair;

        private ElGamalKeyGenerator generator = new ElGamalKeyGenerator(new Random());

        @Setup
        public void setup() {
            keyPair = generator.generateKeyPair(keyLength);
        }
    }

    @Benchmark
    public byte[] signatureBenchmark(SigningState signingState) {
        try {
            return SignatureUtils.sign(signingState.keyPair.getPrivateKey(), SignatureMain.testFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @State(Scope.Benchmark)
    public static class VerificationState {

        @Param({"512", "1024"})
        public int keyLength;

        private ElGamalKeyGenerator generator = new ElGamalKeyGenerator(new Random());

        public ElGamalKeyPair keyPair;
        public byte[] signature;

        @Setup
        public void setup() {
            keyPair = generator.generateKeyPair(keyLength);
            try {
                signature = SignatureUtils.sign(
                        keyPair.getPrivateKey(),
                        SignatureMain.testFile
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Benchmark
    public boolean verificationBenchmark(VerificationState verificationState) {
        try {
            return SignatureUtils.verify(
                    verificationState.keyPair.getPublicKey(),
                    verificationState.signature,
                    SignatureMain.testFile
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
