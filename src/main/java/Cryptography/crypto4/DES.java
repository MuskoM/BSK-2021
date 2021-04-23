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

        return blocks;
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
        //System.out.println("leftstart:" + temp);
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
                //System.out.print(rightSide[i]);
            }
        }
        //System.out.println("right side");
        return rightSide;
    }

    public int [] xorLeftWithRight(int[] left,int[] right){
        int [] permuted = new int[32];
        for(int i=0;i<32;i++){
            permuted[i] = left[i] ^ right[i];
        }
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
                    k++;
                }
            }
        }
        return permuted;
    }

    public int [] xorRightWithKey(Map keyHalfs, int iteration, int[] rightSide){
        Map<Integer,Bits> keys = generate16Keys(keyHalfs);
        //System.out.println("iteracja: "+iteration);
        Bits key = keys.get(iteration);
        //System.out.println("klucz"+key);
        String keyTab = key.toString();
        rightSide = permutedRightSide(rightSide);
        //for(int i=0;i<rightSide.length;i++){
        //    System.out.print(rightSide[i]);
        //}
        //System.out.println("right side do gory");
        //System.out.println(" rightside.lenght " + rightSide.length);
        //System.out.println(" key " + key.toString());
        //System.out.println("_____" + keyTab.length());
        int [] result = new int[48];
        //System.out.println("result: ");
        for(int i=0;i<48;i++){
            int val = rightSide[i] ^ keyTab.charAt(i);
            if(val == 48 || val == 49){
                result[i] = Character.getNumericValue(val);
            }else{
                result[i] = val;
            }
            //System.out.println(val+"|"+rightSide[i]+"|"+keyTab.charAt(i));
        }
        return result;
    }

    public int [] bitsOf6multiple8(Map keyHalfs, int iteration, int[] rightSide){
        int [] result = xorRightWithKey(keyHalfs,iteration,rightSide);
        //System.out.println();
        //for(int i=0;i<result.length;i++){
        //    System.out.print(result[i]);
        //}
        //System.out.println();
        //System.out.println("result dogóry");
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
            //System.out.println("tak: "+tab.length);
            String wiersz = "" + tab[0] + tab[5];
            String kolumna = "" + tab[1] + tab[2] + tab[3] + tab[4];
            //System.out.println(tab[0] + "|" + tab[5]);
            //ystem.out.println(wiersz + "|" + kolumna);
            int row = Integer.parseInt(wiersz,2);
            int column = Integer.parseInt(kolumna,2);
            //System.out.println("rows and columns" + row + "|" + column);
            int [][] currentMap = mapSbloks.get(i);
            String value = String.format("%4s", Integer.toBinaryString(currentMap[row][column])).replace(' ','0');
            //System.out.println("wartosc: " + value);
            temp.append(value);
            //System.out.println("binary: " + value);
        }
        //System.out.println("dlugosc temp:" + temp.toString().length());
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

    public int[] permuteBlock(File file) throws IOException {
        byte [] blocktemp = readFile(file);
        int [] block = new int[blocktemp.length * Byte.SIZE];
        int [] permutedBlock = new int[blocktemp.length * Byte.SIZE];
        String temp = "";
        //System.out.println("Dlugosc blockuz pliku:" + block.length + " SB: " + block.length * Byte.SIZE);
        for(int i=0;i<8;i++){
            temp += String.format("%8s", Integer.toBinaryString(blocktemp[i] & 0xFF)).replace(' ', '0');
        }
        //System.out.println();
        for(int i=0;i<temp.length();i++){
            block[i] = temp.charAt(i);
            //System.out.print(temp.charAt(i));
        }
        //System.out.println();
        //String asd = Integer.toHexString(temp);
        //for(int i=0;i<temp.length();i++){
        //    System.out.print(temp.charAt(i));
        // }


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
    public int[] encryptBlock64bits(Map keyHalfs, File file) throws IOException {
        int [] readBlock = permuteBlock(file);

        //Map<Integer,Bits> keys = generate16Keys(keyHalfs);
        int [] Ln = leftStart(readBlock);
        int [] Rn = permutedRightSide(rightStart(readBlock));
        int [] Rn1;
        for(int i=0;i<16;i++){
            Rn1 = bitsOf6multiple8(keyHalfs,i,Rn);
            Rn = xorLeftWithRight(Ln,Rn1);
            Ln = Rn1;
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
        List<Byte> list = new LinkedList<>();
        for(int i=0;i<4;i++){
            for(int j=0;j<16;j++){
                if(k<64){
                    int place = IP_1_MATRIX[i][j];
                    int val = result[place - 1];
                    if(val == 48 || val == 49){
                        finalResult[k] = Character.getNumericValue(val);
                    }else{
                        finalResult[k] = val;
                    }
                    System.out.print(finalResult[k]);
                    k++;
                }
            }
        }
        return finalResult;
    }

    //11100011 00111100 01011111 01110100 10111001 00110100 01111000 00010010
    //4E752E0000000005
    //E33C5F74B9347812
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
