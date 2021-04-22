package Cryptography.crypto4;

import Cryptography.Cipher;
import Cryptography.crypto3.LFSR;
import utils.Bits;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

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
