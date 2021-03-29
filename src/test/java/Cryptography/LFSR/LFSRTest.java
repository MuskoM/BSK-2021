package Cryptography.LFSR;

import Cryptography.crypto3.LFSR;
import Cryptography.crypto3.TextStreamCipher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Struct;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LFSRTest {
    LFSR lfsr = new LFSR();

    @Test
    void checkUserInputConversionCorrect(){
        Assertions.assertArrayEquals(lfsr.convertUserInput("1"),new Integer[]{1});
    }

    @Test
    void checkCallable(){
        lfsr.setUserPolynomialInput("3,7,5");
        lfsr.initialize();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean[]> score = executorService.submit(lfsr);
        Boolean[] futureNum;
        try {
            futureNum = score.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void seedInitializationTest(){
        lfsr.setUserPolynomialInput("3,7,5");
        lfsr.initialize();
        lfsr.algorithm(12);
    }

    @Test
    void textEncryptionTest() {
        TextStreamCipher txtcipher = new TextStreamCipher();
        byte[] input = "In Poland we say Mr. Keyboard and I think it's beautiful.".getBytes();
        String key = "3,5,7"; //najlepszy radiowy adres na świecie
        byte[] encrypted = txtcipher.encrypt(input, key);
        byte[] decrypted = txtcipher.decrypt(encrypted, key);
        System.out.println("Inp: " + Arrays.toString(input));
        System.out.println("Enc: " + Arrays.toString(encrypted));
        System.out.println("Dec: " + Arrays.toString(decrypted));
        Assertions.assertArrayEquals(input, decrypted);
    }

    @Test
    void checkIfFileIsTxt(){
        Assertions.assertEquals("banana.txt".split("\\.")[1].equals("txt"),true);
    }

}
