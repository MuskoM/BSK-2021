package Cryptography.DES;

import Cryptography.Cipher;
import Cryptography.crypto4.DES;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Bits;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class ReadWriteTest {

    DES des = new DES();

    @Test
    public void ReadDataTest() throws IOException {

        File f = new File(getClass().getClassLoader().getResource("test_5_bytes.txt").getFile());

        byte[] text = des.readFile(f);

        for (byte b: text
             ) {
            System.out.print(" " + String.format("%02X",b));
        }

    }

    @Test
    public void divideKeyTest(){
        Map map = des.divideKey(des.generateBaseKey());
        Bits leftHalf = (Bits) map.get("L");
        Bits rightHalf = (Bits) map.get("R");
        Bits wholeKey = (Bits) map.get("Whole");

        System.out.println("Left Key Half: " + leftHalf);
        System.out.println("Right Key Half: " + rightHalf);
        System.out.println("Whole Key: " + wholeKey);

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

}
