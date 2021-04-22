package Cryptography.crypto4;

import Cryptography.Cipher;
import Cryptography.crypto3.LFSR;
import utils.Bits;

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

    public byte [] encrypted(){
        byte[] bytes = new byte[64];
        return bytes;
    }

    public Map<Integer,Bits> generate64bitsBlock(File file) throws IOException {
        byte[] byteBlocks = readFile(file);
        Map<Integer,Bits> blocks = new HashMap<>();
        for(int i=0;i<8;i++){

        }
        return blocks;
    }

    public String stringfrom8bytes(byte[] block){
        String temp = "";
        for(int i=0;i<8;i++){
            temp += String.format("%8s", Integer.toBinaryString(block[i] & 0xFF)).replace(' ', '0');
        }
        return temp;
    }

    public int [] leftStart(byte[] block){
        int [] leftSide = new int[32];
        String temp = stringfrom8bytes(block);
        for(int i=0;i<temp.length();i++){
            if(i<32){
                leftSide[i] = Character.getNumericValue(temp.charAt(i));
            }
        }
        return leftSide;
    }

    public int [] rightStart(byte[] block){
        int [] rightSide = new int[32];
        String temp = stringfrom8bytes(block);
        for(int i=0;i<temp.length();i++){
            if(i<32){
                rightSide[i] = Character.getNumericValue(temp.charAt(32 + i));
            }
        }
        return rightSide;
    }

    public int [] permutedLeftSide(byte[] block){
        int [] permuted = new int[48];
        int [] leftText = leftStart(block);
        int k=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<12;j++){
                if(k<48){
                    int place = E_MATRIX[i][j];
                    permuted[k] = leftText[place - 1];
                    k++;
                }
            }
        }
        return permuted;
    }

    public int [] permutedRightSide(byte[] block){
        int [] permuted = new int[48];
        int [] rightText = rightStart(block);
        int k=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<12;j++){
                if(k<48){
                    int place = E_MATRIX[i][j];
                    permuted[k] = rightText[place - 1];
                    k++;
                }
            }
        }
        return permuted;
    }

    public int [] xorRightWithKey(Map keyHalfs, int iteration, byte[] block){
        Map<Integer,Bits> keys = generate16Keys(keyHalfs);
        Bits key = keys.get(iteration);
        int [] keyTab = key.stream().toArray();
        int [] permutedRight = permutedRightSide(block);
        int [] result = new int[48];
        for(int i=0;i<48;i++){
            result[i] = permutedRight[i] ^ keyTab[i];
        }
        return result;
    }

    public int [] bitsOf6multiple8(Map keyHalfs, int iteration, byte[] block){
        int [] result = xorRightWithKey(keyHalfs,iteration,block);
        Map<Integer,int[]> mapa = new HashMap<>();
        int [] block1 = new int[6];
        int [] block2 = new int[6];
        int [] block3 = new int[6];
        int [] block4 = new int[6];
        int [] block5 = new int[6];
        int [] block6 = new int[6];
        int [] block7 = new int[6];
        int [] block8 = new int[6];
        for(int i=0;i<result.length;i++){
            if(i >= 0 && i < 6){
                block1[i] = result[i];
            }else if(i >= 6 && i < 12){
                block2[i - 6] = result[i];
            }else if(i >= 12 && i < 18){
                block3[i - 12] = result[i];
            }else if(i >= 18 && i < 24){
                block4[i - 18] = result[i];
            }else if(i >= 24 && i < 30){
                block5[i - 24] = result[i];
            }else if(i >= 30 && i < 36){
                block6[i - 30] = result[i];
            }else if(i >= 36 && i < 42){
                block7[i - 36] = result[i];
            }else if(i >= 42 && i < 48){
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
            String wiersz = tab[0] + tab[7] + "";
            String kolumna = tab[1] + tab[2] + tab[3] + tab[4] + tab[5] + tab[6] + "";
            int row = Integer.parseInt(wiersz,2);
            int column = Integer.parseInt(kolumna,2);
            int value = s1_block[row][column];
            temp.append(Integer.toBinaryString(value));
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
                    k++;
                }
            }
        }

        return permutedBits;
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
