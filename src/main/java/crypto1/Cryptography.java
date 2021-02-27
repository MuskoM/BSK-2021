package crypto1;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Cryptography {

    //Encrypting function using offsetMatrixEncryption
    public static byte[] offsetMatrixEncryption(byte[] input, int[] key){
        int maxKeyValue = Arrays.stream(key).max().getAsInt();
        int[] recalculatedKey = Arrays.stream(key).map(x-> x-1).toArray();
        byte[][] encryptionMatrix = calcualteByteEncryptionMatrix(input,maxKeyValue);

        int currentKeyPos = 0;
        List<Byte> encrypted = new LinkedList<>();

        for(int y = 0; y<encryptionMatrix.length;y++){
            for (int x = 0; x<maxKeyValue;x++){
                    currentKeyPos = recalculatedKey[x];
                    if(encryptionMatrix[y][currentKeyPos] != 0){
                        encrypted.add(encryptionMatrix[y][currentKeyPos]);
                    }
            }
        };
        byte[] encryptedMessage  = new byte[encrypted.size()];
        for(int i =0; i<encrypted.size();i++){
            encryptedMessage[i] = encrypted.get(i);
        }
        return encryptedMessage;
    }

    public static byte[] offsetMatrixDecryption(byte[] input, int[] key){

        int x_max = Arrays.stream(key).max().getAsInt();
        byte[][] decryptionMatrix = calculateByteDecryptionMatrix(input,key);

        List<Byte> decryptedData = new LinkedList<>();
        for (int y = 0; y<decryptionMatrix.length; y++){
            for (int x = 0; x<x_max;x++){
                if(decryptionMatrix[y][x] != 0){
                    decryptedData.add(decryptionMatrix[y][x]);
                }
            }
        }

        byte[] decryptedMessage  = new byte[decryptedData.size()];
        for(int i =0; i<decryptedData.size();i++){
            decryptedMessage[i] = decryptedData.get(i);
        }
        return decryptedMessage;
    }

    public static String encryptedString(byte[] encryptedData){
        return new String(encryptedData,StandardCharsets.UTF_8);
    }

    private static byte[][] calculateByteDecryptionMatrix(byte[] input, int[] key){
        int x_max = Arrays.stream(key).max().getAsInt();
        int y_max = calculateRowNumber(input,x_max);
        int[] recalculatedKey = Arrays.stream(key).map(x-> x-1).toArray();
        byte[][] matrix = new byte[y_max][x_max];
        int currentKeyVal = 0;
        int counter = 0;

        for (int y=0; y<y_max;y++){
            for (int x = 0; x<x_max; x++){
                if(counter<input.length) {
                    currentKeyVal = recalculatedKey[x];
                    matrix[y][currentKeyVal] = input[counter];
                    counter++;
                }
            }
        }

        return matrix;
    }

    private static byte[][] calcualteByteEncryptionMatrix(byte[] input, int x_max){

        int y_max = calculateRowNumber(input,x_max);

        byte[][] matrix = new byte[y_max][x_max];
        int counter = 0;

        for(int y = 0; y<y_max;y++){
            for (int x = 0; x<x_max;x++){
                if(counter < input.length){
                    matrix[y][x] = input[counter];
                    counter++;
                }
            }
        }
        return matrix;
    }


    //Calculates how many rows will the encryption/decryption matrix have
    private static int calculateRowNumber(byte[] data, int keyMaxVal){
        int rows = 0;
        int ln = data.length;

        while(ln > 4){
            rows++;
            ln =  ln-(ln%keyMaxVal+1);
        }

        if(ln!=0)
            rows++;

        return rows;
    }

}
