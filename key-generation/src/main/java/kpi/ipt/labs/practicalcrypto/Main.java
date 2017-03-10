package kpi.ipt.labs.practicalcrypto;

import kpi.ipt.labs.practicalcrypto.primes.MaurerAlgorithm;
import kpi.ipt.labs.practicalcrypto.random.BMRandom;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new BMRandom(Constants.BM_G, Constants.BM_P);
        MaurerAlgorithm maurerAlgorithm = new MaurerAlgorithm(new Random());

        BigInteger prime = maurerAlgorithm.generatePrime(1024);
        System.out.println(prime);
        System.out.println(prime.isProbablePrime(100));
    }
}
