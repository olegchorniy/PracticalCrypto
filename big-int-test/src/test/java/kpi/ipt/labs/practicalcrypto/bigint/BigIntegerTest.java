package kpi.ipt.labs.practicalcrypto.bigint;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class BigIntegerTest {

    @Test
    public void arithmeticOperationsTest() {
        BigInteger leftValue = BigInteger.valueOf(3334);
        BigInteger rightValue = BigInteger.valueOf(1111);

        /* Addition */
        BigInteger expectedSum = BigInteger.valueOf(4445);
        BigInteger actualSum = leftValue.add(rightValue);

        Assert.assertEquals(expectedSum, actualSum);

        /* Subtraction */
        BigInteger expectedDifference = BigInteger.valueOf(2223);
        BigInteger actualDifference = leftValue.subtract(rightValue);

        Assert.assertEquals(expectedDifference, actualDifference);

        /* Multiplication */
        BigInteger expectedProduct = BigInteger.valueOf(3704074);
        BigInteger actualProduct = leftValue.multiply(rightValue);

        Assert.assertEquals(expectedProduct, actualProduct);

        /* Division with remainder */
        BigInteger[] expectedQuotientAndRemainder = new BigInteger[]{
                BigInteger.valueOf(3), //quotient
                BigInteger.valueOf(1)  //remainder
        };
        BigInteger[] actualQuotientAndRemainder = leftValue.divideAndRemainder(rightValue);

        Assert.assertArrayEquals(expectedQuotientAndRemainder, actualQuotientAndRemainder);
    }

    @Test
    public void modularOperationsTest() {
        /* Modular exponentiation */
        BigInteger base = BigInteger.valueOf(3);
        BigInteger exp = BigInteger.valueOf(65536);
        BigInteger mod = BigInteger.valueOf(65537); //prime

        BigInteger expectedPowMod = BigInteger.valueOf(1); //according to the Fermat's little theorem
        BigInteger actualPowMod = base.modPow(exp, mod);

        Assert.assertEquals(expectedPowMod, actualPowMod);

        /* GCD */
        BigInteger first = BigInteger.valueOf(300);
        BigInteger second = BigInteger.valueOf(450);

        BigInteger expectedGcd = BigInteger.valueOf(150);
        BigInteger actualGcd = first.gcd(second);

        Assert.assertEquals(expectedGcd, actualGcd);

        /* Mod inverse */
        BigInteger value = BigInteger.valueOf(12345);
        BigInteger invMod = BigInteger.valueOf(65537);

        BigInteger expectedInverse = BigInteger.valueOf(31651);
        BigInteger actualInverse = value.modInverse(invMod);

        Assert.assertEquals(expectedInverse, actualInverse);
        Assert.assertEquals(value.multiply(actualInverse).mod(invMod), BigInteger.ONE);
    }

    @Test
    public void bitwiseOperationsTest() {
        BigInteger leftValue = BigInteger.valueOf(0b1111_0000);
        BigInteger rightValue = BigInteger.valueOf(0b0101_1010);

        /* Set bit */
        BigInteger expectedResult = BigInteger.valueOf(0b1111_0010);
        BigInteger actualResult = leftValue.setBit(1);

        Assert.assertEquals(expectedResult, actualResult);

        /* Test bit */
        Assert.assertEquals(leftValue.testBit(1), false);
        Assert.assertEquals(leftValue.testBit(5), true);

        /* Shift left */
        BigInteger expectedLeftShift = BigInteger.valueOf(0b1111_0000_00);
        BigInteger actualLeftShift = leftValue.shiftLeft(2);

        Assert.assertEquals(expectedLeftShift, actualLeftShift);

        /* Shift right */
        BigInteger expectedRightShift = BigInteger.valueOf(0b1111_00);
        BigInteger actualRightShift = leftValue.shiftRight(2);

        Assert.assertEquals(expectedRightShift, actualRightShift);

        /* And */
        BigInteger expectedAnd = BigInteger.valueOf(0b0101_0000);
        BigInteger actualAnd = leftValue.and(rightValue);

        Assert.assertEquals(expectedAnd, actualAnd);

        /* Or */
        BigInteger expectedOr = BigInteger.valueOf(0b1111_1010);
        BigInteger actualOr = leftValue.or(rightValue);

        Assert.assertEquals(expectedOr, actualOr);

        /* Xor */
        BigInteger expectedXor = BigInteger.valueOf(0b1010_1010);
        BigInteger actualXor = leftValue.xor(rightValue);

        Assert.assertEquals(expectedXor, actualXor);
    }
}