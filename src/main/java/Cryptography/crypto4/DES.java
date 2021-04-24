package Cryptography.crypto4;

import Cryptography.Cipher;
import Cryptography.crypto3.LFSR;
import utils.Bits;

import javax.naming.InsufficientResourcesException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static Cryptography.crypto4.TabularData.*;

public class DES implements Cipher {



    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return new byte[0];
    }

    public Map dk(){
        Bits generatedKey = generateBaseKey();
        Map <String,Bits> map = divideKey(generatedKey);
        return map;
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return new byte[0];
    }

    public Map<Integer,Bits> generateKeys(){
        Bits baseKey = generateBaseKey();
        Map<String,Bits> dividedKey = divideKey(baseKey);
        Map<Integer,Bits> keys = generate16Keys(dividedKey);
        return keys;
    }

    public Bits generateBaseKey(){
        Bits key;
        LFSR lfsr = new LFSR();
        lfsr.setUserPolynomialInput("4,8,3");
        lfsr.initialize();
        Boolean[] lfsrGeneneratedKey = lfsr.algorithm(64);
        System.out.println("lfsrGeneneratedKey.length: " + lfsrGeneneratedKey.length);
        key = Bits.boolToBitSet(lfsrGeneneratedKey);
        System.out.println("Key:  " + lfsrGeneneratedKey.length);
        return key;
    }

    public Map<Integer,Bits> generate16Keys(Map keyHalfs){
        Map<Integer,Bits> keys = new HashMap<>();
        for (int i = 0; i < 16 ; i++) {
            Bits offsetedLeft = offsetBits((Bits) keyHalfs.get("L"),TabularData.offsetTable[i]);
            Bits offsetedRight = offsetBits((Bits) keyHalfs.get("R"),TabularData.offsetTable[i]);
            Bits wholeKey = get48BitKeyByPC2(Bits.concatBits(offsetedLeft,offsetedRight));
            keys.put(keys.size(),wholeKey);
        }


        return keys;
    }

    public Bits get48BitKeyByPC2(Bits key){
        Bits permutedKey = new Bits(48);
        int counter = 0;
        Boolean b;

        for (int i=0; i < TabularData.PC_2.length;i++){
            for (int j = 0; j < TabularData.PC_2[i].length; j++) {
                b = key.get(TabularData.PC_1[i][j]-1);
                permutedKey.set(counter,b);
                counter++;
            }
        }
        return  permutedKey;
    }

    public String stringfrom8bytes(int[] block){
        StringBuilder temp = new StringBuilder();
        for(int i=0;i<8;i++){
            temp.append(String.format("%8s", Integer.toBinaryString(block[i] & 0xFF)).replace(' ', '0'));
        }
        return temp.toString();
    }

    public int [] leftStart(int[] block){
        int [] leftSide = new int[32];
        String temp = stringfrom8bytes(block);
        for(int i=0;i<block.length;i++){
            if(i<32){
                leftSide[i] = Character.getNumericValue(temp.charAt(i));
            }
        }
        return leftSide;
    }

    public int [] rightStart(int[] block){
        int [] rightSide = new int[32];
        String temp = stringfrom8bytes(block);
        for(int i=0;i<block.length;i++){
            if(i<32){
                rightSide[i] = Character.getNumericValue(temp.charAt(32 + i));
            }
        }
        return rightSide;
    }

    public int [] xorLeftWithRight(int[] left,int[] right){
        int [] permuted = new int[32];
        for(int i=0;i<32;i++){
            permuted[i] = left[i] ^ right[i];
        }
        //System.out.println(Arrays.toString(left));
        //System.out.println(Arrays.toString(right));
        //System.out.println(Arrays.toString(permuted));
        return permuted;
    }

    public int [] permutedRightSide(int[] table){
        int [] permuted = new int[48];
        int k=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<12;j++){
                if(k<48){
                    int place = E_MATRIX[i][j];
                    permuted[k] = table[place - 1];
                    if(permuted[k] == 48 || permuted[k] == 49){
                        permuted[k] = Character.getNumericValue(permuted[k]);
                    }
                    k++;
                }
            }
        }
        //System.out.println(Arrays.toString(permuted));
        return permuted;
    }

    public int [] xorRightWithKey(Map keyHalfs, int iteration, int[] rightSide, boolean MODE){
        Map<Integer,Bits> keys = generate16Keys(keyHalfs);
        rightSide = permutedRightSide(rightSide);
        int [] result = new int[48];
        if(MODE){//kiedy szyfrujemy
            Bits key = keys.get(iteration);
            String keyTab = key.toString();
            for(int i=0;i<48;i++){
                int val = rightSide[i] ^ keyTab.charAt(i);
                //System.out.println(val+"|"+ rightSide[i]+"|"+keyTab.charAt(i));
                if(val == 48 || val == 49){
                    result[i] = Character.getNumericValue(val);
                }else{
                    result[i] = val;
                }
            }
        }else{//kiedy odszyfrowujemy
            Bits key = keys.get(iteration + 15 - (2 * iteration)); //idziemy z kluczami od tyłu
            String keyTab = key.toString();
            for(int i=0;i<48;i++){
                int val = rightSide[i] ^ keyTab.charAt(i);
                //System.out.println(val);
                if(val == 48 || val == 49){
                    result[i] = Character.getNumericValue(val);
                }else{
                    result[i] = val;
                }
            }
        }

        return result;
    }

    public int [] bitsOf6multiple8(Map keyHalfs, int iteration, int[] rightSide,boolean MODE){
        int [] result = xorRightWithKey(keyHalfs,iteration,rightSide,MODE);
        //System.out.println(Arrays.toString(result));
        Map<Integer,int[]> mapa = new HashMap<>();
        Map<Integer,int[][]> mapSbloks = new HashMap<>();
        mapSbloks.put(0,s1_block);
        mapSbloks.put(1,s2_block);
        mapSbloks.put(2,s3_block);
        mapSbloks.put(3,s4_block);
        mapSbloks.put(4,s5_block);
        mapSbloks.put(5,s6_block);
        mapSbloks.put(6,s7_block);
        mapSbloks.put(7,s8_block);
        int [] block1 = new int[6];
        int [] block2 = new int[6];
        int [] block3 = new int[6];
        int [] block4 = new int[6];
        int [] block5 = new int[6];
        int [] block6 = new int[6];
        int [] block7 = new int[6];
        int [] block8 = new int[6];
        for(int i=0;i<result.length;i++){
            if(i < 6){
                block1[i] = result[i];
            }else if(i < 12){
                block2[i - 6] = result[i];
            }else if(i < 18){
                block3[i - 12] = result[i];
            }else if(i < 24){
                block4[i - 18] = result[i];
            }else if(i < 30){
                block5[i - 24] = result[i];
            }else if(i < 36){
                block6[i - 30] = result[i];
            }else if(i < 42){
                block7[i - 36] = result[i];
            }else if(i < 48){
                block8[i - 42] = result[i];
            }
        }
        int [] bits32 = new int[32];
        mapa.put(0,block1);
        mapa.put(1,block2);
        mapa.put(2,block3);
        mapa.put(3,block4);
        mapa.put(4,block5);
        mapa.put(5,block6);
        mapa.put(6,block7);
        mapa.put(7,block8);
        StringBuilder temp = new StringBuilder();
        for(int i=0;i<8;i++){
            int [] tab = mapa.get(i);
            String wiersz = "" + tab[0] + tab[5];
            String kolumna = "" + tab[1] + tab[2] + tab[3] + tab[4];
            int row = Integer.parseInt(wiersz,2);
            int column = Integer.parseInt(kolumna,2);
            int [][] currentMap = mapSbloks.get(i);
            String value = String.format("%4s",
                    Integer.toBinaryString(currentMap[row][column]))
                    .replace(' ','0');
            temp.append(value);
        }
        for(int i=0;i<32;i++){
            bits32[i] = temp.charAt(i);
        }

        //permutacja P
        int [] permutedBits = new int[32];
        int k=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<8;j++){
                if(k<32){
                    int place = P[i][j];
                    permutedBits[k] = bits32[place - 1];
                    if(permutedBits[k] == 48 || permutedBits[k] == 49){
                        result[i] = Character.getNumericValue(permutedBits[k]);
                    }
                    k++;
                }
            }
        }
        //System.out.println(Arrays.toString(permutedBits));
        return permutedBits;
    }

    public int[] permuteBlock(byte[] x) throws IOException {
        int [] block = new int[x.length * Byte.SIZE];
        int [] permutedBlock = new int[x.length * Byte.SIZE];
        String temp = "";
        for(int i=0;i<8;i++){
            temp += String.format("%8s", Integer.toBinaryString(x[i] & 0xFF)).replace(' ', '0');
        }
        for(int i=0;i<temp.length();i++){
            block[i] = temp.charAt(i);
        }
        int k=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<16;j++){
                if(k<64){
                    int place = IP_MATIX[i][j];
                    permutedBlock[k] = block[place - 1];
                    k++;
                }
            }
        }
        return permutedBlock;
    }

    //block reprezentuje 64 bitowy ciąg 0 i 1 w tablicy intów już po pierwszej permutacji
    public int[] encryptBlock64bits(Map keyHalfs, byte[] block) throws IOException {
        int [] readBlock = permuteBlock(block);
        boolean MODE = true;
        int [] Ln = leftStart(readBlock);
        int [] Rn = permutedRightSide(rightStart(readBlock));
        //System.out.println(Arrays.toString(Rn));
        int [] Rn1;
        for(int i=0;i<16;i++){
            Rn1 = bitsOf6multiple8(keyHalfs,i,Rn,MODE);//previous right
            //System.out.println(Arrays.toString(Rn1));
            Rn = xorLeftWithRight(Ln,Rn1);//next right
            Ln = Rn1;//next left
        }
        int [] result = new int[64];
        for(int i=0;i<64;i++){
            if(i<32){
                result[i] = Rn[i];
            }else{
                result[i] = Ln[i - 32];
            }
        }
        int k=0;
        int [] finalResult = new int[64];

        //System.out.println("Wynik szyfrowania: ");
        for(int i=0;i<4;i++) {
            for (int j = 0; j < 16; j++) {
                if (k < 64) {
                    int place = IP_1_MATRIX[i][j];
                    int val = result[place - 1];
                    if (val == 48 || val == 49) {
                        finalResult[k] = Character.getNumericValue(val);
                    } else {
                        finalResult[k] = val;
                    }
                    k++;
                }
            }
        }
        return finalResult;
    }

    public int[] decryptBlock64Bits(Map keyHalfs, byte[] block) throws IOException {
        int [] readBlock = permuteBlock(block);
        boolean MODE = false;
        int [] Ln = leftStart(readBlock);
        int [] Rn = permutedRightSide(rightStart(readBlock));
        int [] Rn1;
        for(int i=0;i<16;i++){
            Rn1 = bitsOf6multiple8(keyHalfs,i,Rn,MODE);//previous right
            Rn = xorLeftWithRight(Ln,Rn1);//next right
            Ln = Rn1;//next left
        }
        int [] result = new int[64];
        for(int i=0;i<64;i++){
            if(i<32){
                result[i] = Rn[i];
            }else{
                result[i] = Ln[i - 32];
            }
        }
        int k=0;
        int [] finalResult = new int[64];

        for(int i=0;i<4;i++) {
            for (int j = 0; j < 16; j++) {
                if (k < 64) {
                    int place = IP_1_MATRIX[i][j];
                    int val = result[place - 1];
                    if (val == 48 || val == 49) {
                        finalResult[k] = Character.getNumericValue(val);
                    } else {
                        finalResult[k] = val;
                    }
                    k++;
                }
            }
        }
        return finalResult;
    }

    public Map<Integer, byte[]> encryptDES(Map keyHalfs, File file) throws IOException {
        byte[] bytes = readFile(file);

        Map<Integer,byte[]> blocks = new HashMap<>();
        Map<Integer,byte[]> results = new HashMap<>();
        int k = 0;
        for(int i=0;i< bytes.length/8;i++){
            byte [] temp = new byte[8];
            for(int j=0;j<8;j++){
                if(k< bytes.length){
                    temp[j] = bytes[k];
                    k++;
                }
            }
            blocks.put(i,temp);
        }

        for(int i=0;i< blocks.size();i++){
            System.out.println("Przekazuje to do zaszyfrowania: " + Arrays.toString(blocks.get(i)));
        }

        for(int i=0;i<bytes.length/8;i++){
            int [] pomoc;
            byte[] result = new byte[8];
            pomoc = binToString(encryptBlock64bits(keyHalfs,blocks.get(i)));
            for(int j=0;j<8;j++){
                result[j] = (byte) pomoc[j];
            }
            results.put(i,result);
        }
        return results;
    }

    public Map<Integer, byte[]> decryptDES(Map keyHalfs,File file, Map<Integer,byte[]> testMap) throws IOException {
        //byte[] bytes = readFile(file);
        byte[] bytes = testMap.get(0); //na potrzeby testów

        Map<Integer,byte[]> blocks = new HashMap<>();
        Map<Integer,byte[]> results = new HashMap<>();
        int k = 0;
        for(int i=0;i< bytes.length/8;i++){
            byte [] temp = new byte[8];
            for(int j=0;j<8;j++){
                if(k< bytes.length){
                    temp[j] = bytes[k];
                    k++;
                }
            }
            blocks.put(i,temp);
        }

        for(int i=0;i< blocks.size();i++){
            System.out.println("Przekazuje to do odszyfrowania: " + Arrays.toString(blocks.get(i)));
        }

        for(int i=0;i<bytes.length/8;i++){
            int [] pomoc;
            byte[] result = new byte[8];
            pomoc = binToString(decryptBlock64Bits(keyHalfs,blocks.get(i)));

            for(int j=0;j<8;j++){
                result[j] = (byte) pomoc[j];
            }
            results.put(i,result);
        }
        return results;
    }

    //proszę się z tego nie śmiać
    public int[] binToString(int [] x){
        int[] wynik = new int[64/8];
        StringBuilder temp1 = new StringBuilder();
        StringBuilder temp2 = new StringBuilder();
        StringBuilder temp3 = new StringBuilder();
        StringBuilder temp4 = new StringBuilder();
        StringBuilder temp5 = new StringBuilder();
        StringBuilder temp6 = new StringBuilder();
        StringBuilder temp7 = new StringBuilder();
        StringBuilder temp8 = new StringBuilder();
        for(int i=0;i<64;i++){
            if(i<8){
                temp1.append("").append(x[i]);
            }else if(i<16){
                temp2.append("").append(x[i]);
            }else if(i<24){
                temp3.append("").append(x[i]);
            }else if(i<32){
                temp4.append("").append(x[i]);
            }else if(i<40){
                temp5.append("").append(x[i]);
            }else if(i<48){
                temp6.append("").append(x[i]);
            }else if(i<56){
                temp7.append("").append(x[i]);
            }else {
                temp8.append("").append(x[i]);
            }
        }
        wynik[0] = Integer.parseInt(temp1.toString(),2);
        wynik[1] = Integer.parseInt(temp2.toString(),2);
        wynik[2] = Integer.parseInt(temp3.toString(),2);
        wynik[3] = Integer.parseInt(temp4.toString(),2);
        wynik[4] = Integer.parseInt(temp5.toString(),2);
        wynik[5] = Integer.parseInt(temp6.toString(),2);
        wynik[6] = Integer.parseInt(temp7.toString(),2);
        wynik[7] = Integer.parseInt(temp8.toString(),2);
        return wynik;
    }

    public Bits offsetBits(Bits bits, int offset){
        Bits offsetBits = new Bits(bits.length());

        for (int i = 0; i < bits.length(); i++) {
            offsetBits.set((i+offset)%bits.length(),bits.get(i));
        }

        return offsetBits;
    }

    public Map divideKey(Bits key){
        Map keyHalfs = new HashMap<String,Bits>();
        Bits leftHalf = new Bits(28);
        Bits rightHalf = new Bits(28);
        int counter = 0;
        Boolean b;

        for (int i = 0; i < TabularData.PC_1.length ; i++) {
            for (int j = 0; j < TabularData.PC_1[i].length ; j++) {
                if(i<4){ //LEFT HALF OF THE KEY
                    b = key.get(TabularData.PC_1[i][j]-1);
                    leftHalf.set(counter,b);
                    counter++;
                    if(counter==28)
                        counter = 0;
                }else{ //RIGHT HALF OF THE KEY
                    b = key.get(TabularData.PC_1[i][j]-1);
                    rightHalf.set(counter,b);
                    counter++;
                }
            }
        }
        keyHalfs.put("L",leftHalf);
        keyHalfs.put("R",rightHalf);
        keyHalfs.put("Whole",key);
        return keyHalfs;
    }

    public byte[] readFile(File file) throws IOException {
        byte[] text = Files.readAllBytes(file.toPath());
        if(text.length%8==0){
            return  text;
        }else{
            int fillLenght = 8-text.length%8;
            byte[] filledText = new byte[text.length + fillLenght];
            for(int i = 0; i<filledText.length;i++){
                if(i<text.length){
                    filledText[i] = text[i];
                }else if(i<filledText.length-1){
                    filledText[i] = (byte) 0x00;
                }else {
                    filledText[i] = Integer.valueOf(fillLenght).byteValue();
                }
            }
            return filledText;
        }
    }

    public void writeFile(File file, byte[] data)throws IOException{
        Files.write(file.toPath(),data);
    }

}
