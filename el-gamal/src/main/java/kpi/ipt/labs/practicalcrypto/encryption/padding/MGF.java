package kpi.ipt.labs.practicalcrypto.encryption.padding;

public interface MGF {

    byte[] mask(byte[] seed, int seedOffset, int seedLength,
                int targetLength);

    default byte[] mask(byte[] seed, int length) {
        return mask(seed, 0, seed.length, length);
    }
}
