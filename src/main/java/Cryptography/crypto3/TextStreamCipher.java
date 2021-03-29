package Cryptography.crypto3;

import Cryptography.Cipher;

import java.util.Arrays;
import java.util.BitSet;

public class TextStreamCipher implements Cipher {

    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return cryptText(input, (String)key);
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return cryptText(input, (String)key);
    }

    private byte[] cryptText(byte[] input, String key) {
        BitSet calculatedByte, bigKey;
        Boolean[] temp = new Boolean[8];
        LFSR lfsr = new LFSR();
        lfsr.setUserPolynomialInput(key);
        lfsr.initialize();

        for (int i = 0; i < input.length; i++) {
            calculatedByte = BitSet.valueOf(new byte [] {input[i]});
            bigKey = new BitSet(8);
            temp = lfsr.algorithm();
            for (int j = 0; j < 8; j++) {
                bigKey.set(j, temp[j]);
            }

            calculatedByte.xor(bigKey);
            input[i] = calculatedByte.toByteArray()[0];
        }

        return input;
    }


}
