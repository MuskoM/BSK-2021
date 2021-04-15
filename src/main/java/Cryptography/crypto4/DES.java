package Cryptography.crypto4;

import Cryptography.Cipher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DES implements Cipher {



    @Override
    public byte[] encrypt(byte[] input, Object key) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] input, Object key) {
        return new byte[0];
    }

    public byte[] readFile(File file) throws IOException {
        byte[] text = Files.readAllBytes(file.toPath());
        if(text.length%8==0){
            return  text;
        }else{
            int fillLenght = 8-text.length%8;
            byte[] filledText = new byte[text.length + fillLenght];
            for(int i = 0; i<filledText.length;i++){
                if(i<text.length){
                    filledText[i] = text[i];
                }else if(i<filledText.length-1){
                    filledText[i] = (byte) 0x00;
                }else {
                    filledText[i] = Integer.valueOf(fillLenght).byteValue();
                }
            }
            return filledText;
        }
    }

    private byte[] generateKey(){
        byte[] key = new byte[64];


        return key;
    }

    public void writeFile(File file, byte[] data)throws IOException{
        Files.write(file.toPath(),data);
    }

}
