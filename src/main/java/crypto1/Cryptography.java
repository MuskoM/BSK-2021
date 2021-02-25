package crypto1;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Cryptography {

    public static List<Byte> offsetMatrixEncryption(byte[] input, int[] key){
        int maxKeyValue = Arrays.stream(key).max().getAsInt();
        int[] recalculatedKey = Arrays.stream(key).map(x-> x-1).toArray();
        Byte[][] encryptionMatrix = calcualteByteEncryptionMatrix(input,maxKeyValue);

        int currentKeyPos = 0;
        List<Byte> encrypted = new ArrayList<>();

        for(int y = 0; y<encryptionMatrix.length;y++){
            for (int x = 0; x<maxKeyValue;x++){
                    currentKeyPos = recalculatedKey[x];
                    if(encryptionMatrix[y][currentKeyPos] != null){
                        encrypted.add(encryptionMatrix[y][currentKeyPos]);
                    }
            }
        };
        return encrypted;
    }

    public static Byte[][] calcualteByteEncryptionMatrix(byte[] input, int x_max){

        int y_max = calculateRowNumber(input,x_max);

        Byte[][] matrix = new Byte[y_max][x_max];
        int counter = 0;

        for(int y = 0; y<y_max;y++){
            for (int x = 0; x<x_max;x++){
                if(counter < input.length){
                    matrix[y][x] = input[counter];
                    counter++;
                }
            }
        }
        System.out.printf("'");
        return matrix;
    }

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
