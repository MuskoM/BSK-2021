package Cryptography.DES;

import Cryptography.Cipher;
import Cryptography.crypto4.DES;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class ReadWriteTest {

    Cipher des = new DES();

    @Test
    public void ReadDataTest() throws IOException {

        File f = new File(getClass().getClassLoader().getResource("test_5_bytes.txt").getFile());

        byte[] text = des.readFile(f);

        for (byte b: text
             ) {
            System.out.print(" " + String.format("%02X",b));
        }

    }

}
