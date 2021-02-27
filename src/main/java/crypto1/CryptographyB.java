package crypto1;

import java.util.Arrays;

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

    private static int rows(String key, String sentence){
        int rows = 1;
        int keyLenght = key.length();
        int sentenceLenght = sentence.length();
        while(sentenceLenght > keyLenght){
            sentenceLenght = sentenceLenght - keyLenght;
            rows++;
        }
        return rows;
    }

    public static String calculateCrypto(String key, String sentence){

        StringBuilder cryptoSentence = new StringBuilder();
        int rows = rows(key,sentence);
        int columns = key.length();
        char[][] matrixCrypto = new char[rows][columns];
        int x = 0;
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(x < sentence.length()){
                    matrixCrypto[i][j] = sentence.charAt(x);
                    x++;
                }
            }
        }

        int[] keyTable = calculatedKey(key);
        int z;
        for(int i=0;i<columns;i++){
            z = keyTable[i];
            for(int j=0;j<rows;j++){
                cryptoSentence.append(matrixCrypto[j][z]);
            }
        }

        return cryptoSentence.toString();
    }

    public static String calculateUnCrypto(String key, String sentence){
        StringBuilder unCryptoSentence = new StringBuilder();
        int rows = rows(key,sentence);
        int columns = key.length();
        char[][] matrixUnCrypto = new char[rows][columns];
        int[] keyTable = calculatedKey(key);
        int x;
        int z = 0;

        for(int i=0;i<columns;i++){
            x = keyTable[i];
            for(int j=0;j<rows;j++){
                if(z < sentence.length()) {
                    matrixUnCrypto[j][x] = sentence.charAt(z);
                    z++;
                }
            }
        }

        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                unCryptoSentence.append(matrixUnCrypto[i][j]);
            }
        }

        return unCryptoSentence.toString();
    }
}