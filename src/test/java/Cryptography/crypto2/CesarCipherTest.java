package Cryptography.crypto2;
import Cryptography.Cipher;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CesarCipherTest {

    Cipher cesar = new CesarCipher();

    @Test
    void checkCesarCipherEncryption(){

        byte[] message = "Nienawidzę takich ludzi jak ona, co ja tatus przepchną".getBytes(StandardCharsets.UTF_8);

        byte[] output = cesar.encrypt(message,1);
        byte[] decrypted = cesar.decrypt(output,1);

        Assertions.assertArrayEquals(message,decrypted);

    }

    @Test
    void checkKeyRegex(){
        final Pattern CesarCipherKeyInputPattern = Pattern.compile("^[0-9]{1,4}$");
        String input="123";
        if(!CesarCipherKeyInputPattern.matcher(input).matches()){
            throw new IllegalArgumentException();
        }else{
            System.out.println("sprit");
        }
    }
}
