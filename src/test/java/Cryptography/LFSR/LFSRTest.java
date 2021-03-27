package Cryptography.LFSR;

import Cryptography.crypto3.LFSR;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Struct;
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
        lfsr.algorithm();
    }

}
