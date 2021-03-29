package Cryptography.crypto3;

import Cryptography.Cipher;

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
        boolean[] calculatedByte;
        boolean[] bigKey;
        byte[] result = new byte[input.length];;
        LFSR lfsr = new LFSR();
        lfsr.setUserPolynomialInput(key);
        lfsr.initialize();

        for (int i = 0; i < input.length; i++) {
            calculatedByte = convertByteToBooleanArray(input[i]);
            bigKey = new boolean[8];
            bigKey = convertObjectShitToOldschoolArray(lfsr.algorithm());
            for (int j = 0; j < 8; j++) {
                calculatedByte[j] = calculatedByte[j] ^ bigKey[j];
            }
            result[i] = convertBooleanArrayToByte(calculatedByte);
        }

        return result;
    }

    private boolean[] convertByteToBooleanArray(byte input) {
        boolean[] result = new boolean[8];

        //holy fuck
        result[7] = ((input & 0x01) != 0);
        result[6] = ((input & 0x02) != 0);
        result[5] = ((input & 0x04) != 0);
        result[4] = ((input & 0x08) != 0);
        result[3] = ((input & 0x10) != 0);
        result[2] = ((input & 0x20) != 0);
        result[1] = ((input & 0x40) != 0);
        result[0] = ((input & 0x80) != 0);

        return result;
    }

    private byte convertBooleanArrayToByte(boolean[] input) {
        return (byte)((input[0]?1<<7:0) + (input[1]?1<<6:0) + (input[2]?1<<5:0) + (input[3]?1<<4:0) + (input[4]?1<<3:0)
                + (input[5]?1<<2:0) + (input[6]?1<<1:0) + (input[7]?1:0));
    }

    private boolean[] convertObjectShitToOldschoolArray(Boolean[] input) {
        boolean[] result = new boolean[8];
        for (int i = 0; i < 8; i++) {
            result[i] = input[i].booleanValue();
        }
        return result;
    }

}
