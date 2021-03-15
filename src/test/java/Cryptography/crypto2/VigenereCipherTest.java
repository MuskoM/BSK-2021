package Cryptography.crypto2;

import Cryptography.Cipher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class VigenereCipherTest {
    Cipher vigenere = new VigenereCipher();

    @Test
    void checkVigenereCipherEncryption()
    {
        byte[] message = "Bezpieczenstwo Sieci Komputerowych TO NAPRAWDE FAJNY PRZEDMIOT!!!".getBytes(StandardCharsets.UTF_8);
        byte[] key = "KRZYSIOMISIO".getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = vigenere.encrypt(message, key);
        byte[] decrypted = vigenere.decrypt(encrypted, key);

        Assertions.assertArrayEquals(decrypted, message);
    }
}
