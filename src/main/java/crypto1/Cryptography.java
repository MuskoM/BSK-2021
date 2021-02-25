package crypto1;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.OptionalInt;

public class Cryptography {

    public static byte offsetMatrixEncryption(){


        return 0  ;
    }

    public static String offsetMatrixEncryptionString(byte[] input, int[] key){

        int x_max = Arrays.stream(key).max().getAsInt();

        System.out.printf("Max key value: " + x_max);
        int y_max = calculateRowNumber(input,x_max);
        System.out.printf("Max row value: " + y_max);

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

        System.out.println("DONE!");

        return "0";
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
