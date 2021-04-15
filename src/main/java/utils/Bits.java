package utils;

import java.util.BitSet;
import java.util.stream.IntStream;

public class Bits extends BitSet {

    private int noBits;

    public Bits(int nbits) {
        super(nbits);
        this.noBits = nbits;
    }

    public static Bits boolToBitSet(Boolean[] bits) {
        Bits bitset = new Bits(bits.length);
        Boolean b;
        for (int i = 0; i < bits.length; i++) {
            b= bits[i];
            bitset.set(i,b);
        }

        return bitset;
    }

    @Override
    public String toString() {
        return IntStream
                .range(0, this.noBits)
                .mapToObj(i -> get(i) ? '1' : '0')
                .collect(
                        () -> new StringBuilder(this.noBits),
                        (buffer, characterToAdd) -> buffer.append(characterToAdd),
                        StringBuilder::append
                )
                .toString();
    }
}
