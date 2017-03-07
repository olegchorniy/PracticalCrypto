package kpi.ipt.labs.practicalcrypto;

import kpi.ipt.labs.practicalcrypto.primes.MaurerAlgorithm;
import kpi.ipt.labs.practicalcrypto.random.BMRandom;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new BMRandom(Constants.BM_G, Constants.BM_P);
        BigInteger p = new MaurerAlgorithm(random).provablePrime(256);

        System.out.println(p);
        System.out.println("Really prime: " + p.isProbablePrime(100));
    }
}
