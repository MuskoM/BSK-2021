package crypto1;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CryptographyC implements Cipher {

    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return calculateCrypto((String) key, input);
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return calculateUnCrypto((String) key, input);
    }

    private int[] calculatedKey(String key){
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

    private int rows(String key, byte[] sentence){
        int rows = 0;
        int temp = 0;
        int i = 0;
        int column;
        int[] tab_temp = calculatedKey(key);
        while(temp <= sentence.length){
            if(tab_temp[i] + 1 == 11){
                i = 0;
            }
            column = tab_temp[i] + 1;
            temp = temp + column;
            rows++;
            i++;
        }
        return rows;
    }

    private byte[] calculateCrypto(String key, byte[] sentence){
        int rows = rows(key,sentence);
        int columns = key.length();
        int[] keyTable = calculatedKey(key);
        byte[][] matrixCrypto = new byte[rows][columns];

        int x = 0;
        for(int i=0;i<rows;i++){
            int temp = keyTable[i % keyTable.length] + 1;
            for(int j=0;j<temp;j++){
                if(x < sentence.length){
                    matrixCrypto[i][j] = sentence[x];
                    //System.out.print(matrixCrypto[i][j] + " ");
                    x++;
                }
            }
            //System.out.println();
        }

        int y;
        List<Byte> list_crypto = new LinkedList<>();
        for(int i=0;i<columns;i++){
            y = keyTable[i];
            for(int j=0;j<rows;j++){
                if(matrixCrypto[j][y] != 0){
                    //System.out.println(matrixCrypto[j][y]);
                    list_crypto.add(matrixCrypto[j][y]);
                }
            }
        }

        byte[] byte_crypto = new byte[list_crypto.size()];
        for(int i=0;i<list_crypto.size();i++){
            byte_crypto[i] = list_crypto.get(i);
        }

        return byte_crypto;
    }

    private byte[] calculateUnCrypto(String key, byte[] sentence){
        int rows = rows(key,sentence);
        int columns = key.length();
        int[] keyTable = calculatedKey(key);
        boolean[][] truth_matrix = new boolean[rows][columns];

        int x = 0;
        for(int i=0;i<rows;i++){
            int temp = keyTable[i % keyTable.length] + 1;
            for(int j=0;j<temp;j++){
                if(x < sentence.length){
                    truth_matrix[i][j] = true;
                    x++;
                }
            }
        }

        int y = 0;
        int temp;
        byte[][] matrixunCrypto = new byte[rows][columns];
        for(int i=0;i<columns;i++){
            temp = keyTable[i];
            for(int j=0;j<rows;j++){
                if(y < sentence.length){
                    if(truth_matrix[j][temp]){
                        matrixunCrypto[j][temp] = sentence[y];
                        y++;
                    }
                }
            }
        }

        List<Byte> list_un_crypto = new LinkedList<>();
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(matrixunCrypto[i][j] != 0)
                    list_un_crypto.add(matrixunCrypto[i][j]);
            }
        }

        byte[] byte_un_crypto = new byte[list_un_crypto.size()];
        for(int i = 0; i < list_un_crypto.size() ; i++){
            byte_un_crypto[i] = list_un_crypto.get(i);
        }

        return  byte_un_crypto;

    }
}
