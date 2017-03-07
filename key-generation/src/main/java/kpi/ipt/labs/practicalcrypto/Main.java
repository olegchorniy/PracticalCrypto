package kpi.ipt.labs.practicalcrypto;

import kpi.ipt.labs.practicalcrypto.primes.MaurerAlgorithm;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        BigInteger p = MaurerAlgorithm.provablePrime(1024);

        System.out.println(p);
        System.out.println(p.isProbablePrime(100));
    }
}
