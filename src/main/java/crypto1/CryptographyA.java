package crypto1;

// Rail Fence algorithm

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

public class CryptographyA {
    public static byte[] railFenceEncryption(byte[] input, int key)
    {
        int matrix_length = getMatrixLength(input.length, key);
//        byte[][] encryptionMatrix = calculateEncryptionMatrix(input, key);
        byte[][] encryptionMatrix = new byte[key][input.length];
        byte[] encryptedSentence = new byte[input.length];
        int just_iterate = 0;
        boolean which_way = true;

        if (key < 1)
            throw new ArithmeticException("The key is a non-positive value.");

        for (int r = 0; r < key; r++)
        {
            int i = 0;
            for (int j = r; j < input.length; j += getDistance(i++, r, key))
            {
                encryptedSentence[just_iterate] = input[j];
                just_iterate++;
            }
        }

//      DAMMIT IT DOESN"T WORK YHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
//        for (int i = 0; i < input.length; i++) {
//            encryptionMatrix[just_iterate][i] = input[i];
//
//            if (which_way)
//                just_iterate++;
//            else
//                just_iterate--;
//
//            if (just_iterate == 0)
//            {
//                which_way = true;
//            }
//            else if (just_iterate == input.length - 1)
//            {
//                which_way = false;
//            }
//        }
//
//        for (int i = 0; i < input.length; i++) {
//            for (int j = 0; j < encryptionMatrix.length; j++) {
//                if (encryptionMatrix[i][j] != 0)
//                {
//                    encryptedSentence[just_iterate++] = encryptionMatrix[i][j];
//                }
//            }
//        }


//          ONE DAY... IT'LL BE THE MORE MEMORY EFFICIENT VERSION
//        for (int i = 0; i < encryptionMatrix.length; i++) {
//            if (i % 2 == 0)
//            {
//                for (int j = 0; j < matrix_length; j++) {
//                    encryptionMatrix[i][j] = input[just_iterate++];
//                }
//            }
//            else
//            {
//                for (int j = matrix_length - 1; j > 0; j--)
//                {
//                    encryptionMatrix[i][j] = input[just_iterate++];
//                }
//            }
//        }
//
//        just_iterate = 0;
//
//        for (int i = 0; i < matrix_length; i++) {
//            for (int j = 0; j < encryptionMatrix.length; j++) {
//                if (encryptionMatrix[i][j] != 0)
//                {
//                    encryptedSentence[just_iterate++] = encryptionMatrix[i][j];
//                }
//            }
//        }

        return encryptedSentence;
    }

    public static byte[] railFenceDecryption(byte[] input, int key)
    {
        if (key < 1)
            throw new ArithmeticException("The key is non-positive value.");

        byte[] decryptedSentence = new byte[input.length];
        int just_iterate = 0;

        for (int r = 0; r < key; r++)
        {
            int i = 0;
            for (int j = r; j < input.length; j += getDistance(i, r, key))
                decryptedSentence[j] = input[just_iterate++];
        }

        return decryptedSentence;
    }

    private static int getDistance(int iteration, int row, int size)
    {
        if (size == 0 || size == 1)
            return 1;
        else if (row == 0 || row == size - 1)
            return (size - 1) * 2;
        else if (iteration % 2 == 0)
            return ((size - 1) - row) * 2;
        else return row * 2;
    }

    public static byte[][] calculateEncryptionMatrix(byte[] input, int key)
    {
        int matrix_length = getMatrixLength(input.length, key);

        return new byte[key][matrix_length];
    }

    private static int getMatrixLength(int input_length, int key)
    {
        return (int)Math.ceil((float)input_length / ((key * 2) - 2));
    }

    public static String cryptoString(byte[] data)
    {
        return new String(data, StandardCharsets.UTF_8);
    }
}
