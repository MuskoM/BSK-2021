package Cryptography;

import java.io.File;
import java.io.IOException;

public interface Cipher {
    byte[] encrypt(byte[] input, Object key);
    byte[] decrypt(byte[] input, Object key);
    byte[] readFile(File file) throws IOException;
    void writeFile(File file, byte[] data)throws IOException;
}
