package kpi.ipt.labs.practicalcrypto;

import java.math.BigInteger;

public final class Constants {

    private Constants() {
    }

    public static final BigInteger BM_P
            = new BigInteger("CEA42B987C44FA642D80AD9F51F10457690DEF10C83D0BC1BCEE12FC3B6093E3", 16);
    public static final BigInteger BM_G
            = new BigInteger("5B88C41246790891C095E2878880342E88C79974303BD0400B090FE38A688356", 16);
}
