package Cryptography.crypto3;

import Cryptography.Cipher;

import java.io.File;
import java.io.IOException;

public class TextStreamCipher implements Cipher {

    LFSR lsf = new LFSR();
    byte[] key;

    @Override
    public byte[] encrypt(byte[] input, Object key) {
        byte[] returnValue = cryptText(input, (String)key);
        System.out.println("returnValue)");
        return returnValue;
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return cryptText(input, (String)key);
    }

    @Override
    public byte[] readFile(File file) throws IOException {
        return new byte[0];
    }

    @Override
    public void writeFile(File file, byte[] data) throws IOException {

    }

    private byte[] cryptText(byte[] input, String userKey) {
        lsf.setUserPolynomialInput(userKey);
        if(key==null){
            lsf.initialize();
        }
        key = toBytes(lsf.algorithm(input.length*8));
        byte[] cipherBytes = new byte[input.length];
        for(int i = 0; i < input.length; i++) {
            cipherBytes[i] = (byte) (input[i] ^ key[i]);
        }
        return cipherBytes;
    }


    private byte[] toBytes(Boolean[] input) {
        byte[] toReturn = new byte[input.length / 8];
        for (int entry = 0; entry < toReturn.length; entry++) {
            for (int bit = 0; bit < 8; bit++) {
                if (input[entry * 8 + bit]) {
                    toReturn[entry] |= (128 >> bit);
                }
            }
        }

        return toReturn;
    }

}
