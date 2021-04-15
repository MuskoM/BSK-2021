package Cryptography.crypto2;

import Cryptography.Cipher;

import java.io.File;
import java.io.IOException;

public class VigenereCipher implements Cipher {

    private static byte[] genKey(byte[] input, byte[] key)
    {
        if(input.length == key.length)
            return key;

        byte[] new_key = new byte[input.length];
        int old_key_len = key.length;
        int old_key_iter = 0;

        for (int i = 0; i < input.length; i++) {
            new_key[i] = key[(old_key_iter++) % old_key_len];
        }

        return new_key;
    }

    public static byte[] vigenereEncrypt(byte[] input, byte[] k)
    {
        byte[] key = genKey(input, k);
        byte[] encrypted_input = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            // obsługa innych znaków niż litery
            if (!(input[i] >= 'A' && input[i] <= 'Z') && !(input[i] >= 'a' && input[i] <= 'z'))
            {
                encrypted_input[i] = input[i];
                continue;
            }

            // obsługa dużych i małych liter
            if (input[i] >= 'A' && input[i] <= 'Z') {
                byte x = (byte) ((input[i] + key[i]) % 26);
                encrypted_input[i] = (byte) (x + 'A'); //kod ASCII dla "A"
            } else if (input[i] >= 'a' && input[i] <= 'z') {
                byte x = (byte) ((input[i] + key[i] - 6) % 26);
                encrypted_input[i] = (byte) (x + 'a'); //kod ASCII dla "a"
            }
        }

        return encrypted_input;
    }

    public static byte[] vigenereDecrypt(byte[] input, byte[] k)
    {
        byte[] key = genKey(input, k);
        byte[] decrypted_input = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            // obsługa innych znaków niż litery
            if (!(input[i] >= 'A' && input[i] <= 'Z') && !(input[i] >= 'a' && input[i] <= 'z'))
            {
                decrypted_input[i] = input[i];
                continue;
            }

            // obsługa dużych i małych liter
            if (input[i] >= 'A' && input[i] <= 'Z') {
                byte x = (byte) ((input[i] - key[i] + 26) % 26);
                decrypted_input[i] = (byte) (x + 'A'); //kod ASCII dla "A"
            }
            else if (input[i] >= 'a' && input[i] <= 'z') {
                byte x = (byte) ((input[i] - key[i] + 20) % 26);
                decrypted_input[i] = (byte) (x + 'a'); //kod ASCII dla "a"
            }
        }

        return decrypted_input;
    }

    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return vigenereEncrypt(input, (byte[]) key);
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return vigenereDecrypt(input, (byte[]) key);
    }

    @Override
    public byte[] readFile(File file) throws IOException {
        return new byte[0];
    }

    @Override
    public void writeFile(File file, byte[] data) throws IOException {

    }
}
