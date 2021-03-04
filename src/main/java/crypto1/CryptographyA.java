package crypto1;

// Rail Fence algorithm

public class CryptographyA {
    public static byte[] railFenceEncryption(byte[] input, int key)
    {
        int matrix_length = getMatrixLength(input.length, key);
        byte[][] encryptionMatrix = calculateEncryptionMatrix(input, key);

        for (int i = 0; i < encryptionMatrix.length; i++) {
            if (i % 2 == 0)
            {
                for (int j = 0; j < matrix_length; j++) {
                    //smth
                }
            }
        }

        return null;
    }

    public static byte[][] calculateEncryptionMatrix(byte[] input, int key)
    {
        int matrix_length = getMatrixLength(input.length, key);

        return new byte[key][matrix_length];
    }

    private static int getMatrixLength(int input_length, int key)
    {
        return (int)Math.ceil((float)input_length / ((key * 2) - 2));
    }
}
