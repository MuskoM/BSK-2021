package Cryptography.crypto2;

import Cryptography.Cipher;

import java.io.File;
import java.io.IOException;

public class CesarCipher implements Cipher {

    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return encryptWithCesarCipher(input,(int)key);
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return decryptWithCesarCipher(input,(int)key);
    }

    @Override
    public byte[] readFile(File file) throws IOException {
        return new byte[0];
    }

    @Override
    public void writeFile(File file, byte[] data) throws IOException {

    }

    private byte[] encryptWithCesarCipher(byte[] input, int key){
        byte[] output = new byte[input.length];

        byte temp;

        for(int i = 0; i < input.length; i++){

            if(input[i]<='z' && input[i]>='a'){
                temp = (byte)(input[i] - 'a');
                temp = (byte)((temp + key)%26);
                output[i] = (byte) ('a'+temp);
            }else if(input[i]<='Z' && input[i]>='A'){
                temp = (byte)(input[i] - 'A');
                temp = (byte)((temp + key)%26);
                output[i] = (byte) ('A'+temp);
            }else {
                output[i] = input[i];
            }

        }

        return output;
    }

    private byte[] decryptWithCesarCipher(byte[] input, int key){
        byte[] output = new byte[input.length];

        byte temp;
        for(int i = 0; i < input.length; i++){

            if(input[i]<='z' && input[i]>='a'){
                temp = (byte)(input[i] - 'a');
                temp = (byte)((temp + (26-key))%26);
                output[i] = (byte) ('a'+temp);
            }else if(input[i]<='Z' && input[i]>='A'){
                temp = (byte)(input[i] - 'A');
                temp = (byte)((temp + (26-key))%26);
                output[i] = (byte) ('A'+temp);
            }else {
                output[i] = input[i];
            }

        }

        return output;
    }

}
