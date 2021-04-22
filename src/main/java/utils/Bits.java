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

        System.out.println("Bits > Bitset.length " + bits.length);
        return bitset;
    }

    public static Bits concatBits(Bits b1,Bits b2){
        Bits joinedBits = new Bits(b1.length() + b2.length());
        int counter = 0;

        for (int i = 0; i < joinedBits.length();i++){

            if(i < b1.length()){
                joinedBits.set(i,b1.get(counter));
                counter++;
            }
            if(counter == b1.length()){
                counter = 0;
            }
            if(i >= b1.length()){
                joinedBits.set(i,b2.get(counter));
                counter++;
            }

        }

        return joinedBits;
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

    @Override
    public int length() {
        return noBits;
    }
}
