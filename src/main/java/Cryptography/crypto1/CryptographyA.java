package Cryptography.crypto1;

// Rail Fence algorithm

import java.nio.charset.StandardCharsets;
import Cryptography.Cipher;

public class CryptographyA  implements Cipher {

    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return railFenceEncryption(input, (int)key);
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return railFenceDecryption(input, (int)key);
    }

    public byte[] railFenceEncryption(byte[] input, int key) {
        byte[][] encryptionMatrix = calculateEncryptionMatrix(input, key);
        byte[] encryptedSentence = new byte[input.length];
        int just_iterate = 0;

        for (int col = 0; col < encryptionMatrix[0].length; col++) {
            if (col % 2 == 0) {
                for (int row = 0; row < encryptionMatrix.length; row++) {
                    encryptionMatrix[row][col] = just_iterate < input.length ? input[just_iterate++] : 0;
                }
            } else {
                for (int row = encryptionMatrix.length - 2; row > 0; row--) {
                    encryptionMatrix[row][col] = just_iterate < input.length ? input[just_iterate++] : 0;
                }
            }
        }

        just_iterate = 0;

        for (int row = 0; row < encryptionMatrix.length; row++) {
            for (int col = 0; col < encryptionMatrix[0].length; col++) {
                if (encryptionMatrix[row][col] != 0) {
                    encryptedSentence[just_iterate++] = encryptionMatrix[row][col];
                }
            }
        }

        return encryptedSentence;
    }

    public byte[] railFenceDecryption(byte[] input, int key) {
        byte[][] decryptionMatrix = calculateEncryptionMatrix(input, key);
        byte[] decryptedSentence = new byte[input.length];
        int just_iterate = 0;

        for (int row = 0; row < decryptionMatrix.length; row++) {
            for (int col = 0; col < decryptionMatrix[0].length; col++) {
                if (row == 0 || row == decryptionMatrix.length - 1)
                {
                    if (col % 2 == 0)
                    {
                        decryptionMatrix[row][col] = input[just_iterate++];
                    }
                }
                else {
                    decryptionMatrix[row][col] = input[just_iterate++];
                }
            }
        }

        just_iterate = 0;

        for (int col = 0; col < decryptionMatrix[0].length; col++) {
            if (col % 2 == 0) {
                for (int row = 0; row < decryptionMatrix.length; row++) {
                    decryptedSentence[just_iterate++] = decryptionMatrix[row][col];
                }
            } else {
                for (int row = decryptionMatrix.length - 2; row > 0; row--) {
                    decryptedSentence[just_iterate++] = decryptionMatrix[row][col];
                }
            }
        }

        return decryptedSentence;
    }

    private int getDistance(int iteration, int row, int size) {
        if (size == 0 || size == 1)
            return 1;
        else if (row == 0 || row == size - 1)
            return (size - 1) * 2;
        else if (iteration % 2 == 0)
            return ((size - 1) - row) * 2;
        else return row * 2;
    }

    public  byte[][] calculateEncryptionMatrix(byte[] input, int key) {
        int matrix_length = getMatrixLength(input.length, key);

        return new byte[key][matrix_length];
    }

    private  int getMatrixLength(int input_length, int key) {
        return (int) Math.ceil((float) input_length / (key - 1));
    }

    public String cryptoString(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}
