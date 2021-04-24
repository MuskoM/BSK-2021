package Cryptography.DES;

import Cryptography.Cipher;
import Cryptography.crypto3.LFSR;
import Cryptography.crypto4.DES;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Bits;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;


public class ReadWriteTest {

    DES des = new DES();

    @Test
    public void ReadDataTest() throws IOException {

        File f = new File(getClass().getClassLoader().getResource("test_5_bytes.txt").getFile());
        File fd = new File(getClass().getClassLoader().getResource("test_5_bytes_to_decrypt.txt").getFile());

        byte[] text = des.readFile(f);

        for (byte b: text) {
            System.out.print(" " + String.format("%02X",b));
        }
        System.out.println();
        System.out.println("Do zaszyfrowania:");
        for (byte value : text) {
            System.out.print(" " + value);
        }

        Bits baseKey =des.generateBaseKey();
        Map<String,Bits> dividedKey = des.divideKey(baseKey);
        Map<Integer, byte[]> wynik = des.encryptDES(dividedKey,f);
        System.out.println("Zaszyfrowane:");
        for(int i=0;i< wynik.size();i++){
            System.out.println(Arrays.toString(wynik.get(i)));
        }

        Map<Integer, byte[]> decrypt = des.decryptDES(dividedKey,fd,wynik);
        System.out.println("Odszyfrowane:");
        for(int i=0;i< decrypt.size();i++){
            System.out.println(Arrays.toString(decrypt.get(i)));
        }
    }

    @Test
    public void generateBaseKeyTest(){
        Bits b = des.generateBaseKey();
        System.out.println(b + " " + b.size());
    }

    @Test
    public void divideKeyTest(){
        Map map = des.divideKey(des.generateBaseKey());
        Bits leftHalf = (Bits) map.get("L");
        Bits rightHalf = (Bits) map.get("R");
        Bits wholeKey = (Bits) map.get("Whole");

        System.out.println("Left Key Half: " + leftHalf);
        System.out.println("Right Key Half: " + rightHalf);
        System.out.println("Whole Key: " + wholeKey + " Len: " + wholeKey.size());

        Assertions.assertEquals(leftHalf.get(0),wholeKey.get(56));
        Assertions.assertEquals(rightHalf.get(0),wholeKey.get(62));
        Assertions.assertEquals(rightHalf.get(27),wholeKey.get(3));

    }

    @Test
    public void generate16KeysTest(){
        Map map = des.divideKey(des.generateBaseKey());
        Map keys16 = des.generate16Keys(map);

        keys16.forEach((o, o2) -> System.out.println(o + " " + o2));

    }

    @Test
    public void testConcatBits(){
        Bits key;
        LFSR lfsr = new LFSR();
        lfsr.setUserPolynomialInput("4,8,3");
        lfsr.initialize();
        Boolean[] lfsrGeneneratedKey = lfsr.algorithm(4);
        System.out.println("lfsrGeneneratedKey.length: " + lfsrGeneneratedKey.length);
        key = Bits.boolToBitSet(lfsrGeneneratedKey);

        System.out.println(key);

        Bits concBits = Bits.concatBits(key,key);

        System.out.println(concBits);

    }

    @Test
    public void testget48BitKeyByPC2(){
        Map map = des.divideKey(des.generateBaseKey());
        Map keys16 = des.generate16Keys(map);
        Bits bitKey =   des.get48BitKeyByPC2((Bits) keys16.get(1));
        System.out.println(bitKey);

    }

    @Test
    public void testGenerate48BitKeysForDESAlgorithm(){
        Map map = des.generateKeys();
        map.forEach((o, o2) -> System.out.println("Nr: " + o + "Key: " + o2));
    }

}
