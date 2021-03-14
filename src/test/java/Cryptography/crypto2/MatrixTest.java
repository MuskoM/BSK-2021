package Cryptography.crypto2;

import Cryptography.Cipher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class MatrixTest {

    Cipher cesar = new CryptographyC();

    @Test
    void checkCesarCipherEncryption(){

        byte[] message = "THIS IS A TEST MESSAGE, THERE WAS NO HOSTILITY HERE".getBytes(StandardCharsets.UTF_8);

        byte[] output = cesar.encrypt(message,"KEY");
        byte[] decrypted = cesar.decrypt(output,"KEY");

        Assertions.assertArrayEquals(message,decrypted);

    }

}
