public interface Cipher {
    byte[] encrypt(byte[] input, Object key);
    byte[] decrypt(byte[] input, Object key);
}
