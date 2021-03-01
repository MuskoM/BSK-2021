package crypto1;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CryptographyB {

    private static int[] calculatedKey(String key){
        int[] calculatedKey = new int[key.length()];

        char[] chars = key.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);

        int[] calculatedKeyRepeat = new int[key.length()];
        for(int i=0;i<key.length();i++){
            for(int j=0;j<key.length();j++){
                if(key.charAt(i) == sorted.charAt(j)){
                    calculatedKeyRepeat[i] = j + 1;
                    break;
                }
            }
        }

        for(int i=0;i<key.length();i++){
            int temp = calculatedKeyRepeat[i];
            for(int j=0;j<key.length();j++){
                if(calculatedKeyRepeat[j] == temp && i != j){
                    int temp2 = calculatedKeyRepeat[i];
                    calculatedKeyRepeat[j] = temp2 + 1;
                }
            }
        }

        for(int i=0;i<key.length();i++){
            calculatedKey[calculatedKeyRepeat[i] - 1] = i;
        }

        return calculatedKey;
    }

    private static int rows(String key, byte[] sentence){
        int rows = 1;
        int keyLenght = key.length();
        int sentenceLenght = sentence.length;
        while(sentenceLenght > keyLenght){
            sentenceLenght = sentenceLenght - keyLenght;
            rows++;
        }
        return rows;
    }

    public static byte[] calculateCrypto(String key, byte[] sentence){

        int rows = rows(key,sentence);
        int columns = key.length();
        byte[][] matrixCrypto = new byte[rows][columns];
        int x = 0;
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(x < sentence.length){
                    matrixCrypto[i][j] = sentence[x];
                    x++;
                }
            }
        }

        int[] keyTable = calculatedKey(key);
        List<Byte> list_crypto = new LinkedList<>();
        int z;
        for(int i=0;i<columns;i++){
            z = keyTable[i];
            for(int j=0;j<rows;j++){
                list_crypto.add(matrixCrypto[j][z]);
            }
        }

        byte[] byte_crypto = new byte[list_crypto.size()];
        for(int i=0;i<list_crypto.size();i++){
            byte_crypto[i] = list_crypto.get(i);
        }

        return byte_crypto;
    }

    public static byte[] calculateUnCrypto(String key, byte[] sentence){

        int rows = rows(key,sentence);
        int columns = key.length();
        byte[][] matrixUnCrypto = new byte[rows][columns];
        int[] keyTable = calculatedKey(key);
        int x;
        int z = 0;

        for(int i=0;i<columns;i++){
            x = keyTable[i];
            for(int j=0;j<rows;j++){
                if(z < sentence.length) {
                    matrixUnCrypto[j][x] = sentence[z];
                    z++;
                }
            }
        }

        List<Byte> list_crypto = new LinkedList<>();
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                list_crypto.add(matrixUnCrypto[i][j]);
            }
        }

        byte[] byte_crypto = new byte[list_crypto.size()];
        for(int i=0;i<list_crypto.size();i++){
            byte_crypto[i] = list_crypto.get(i);
        }

        return byte_crypto;
    }

    public static String cryptoString(byte[] cryptoData){
        return new String(cryptoData, StandardCharsets.UTF_8);
    }
}