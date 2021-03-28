package Cryptography.crypto3;

import Cryptography.Cipher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StreamCipher {
    public int[] temp1;

    public BufferedImage encrypt(BufferedImage image, String key) throws IOException {
        int h=0;
        for(int i=0;i<8;i++){
            if(key.length() > 20){
                if(key.charAt(i) != '.') {
                    h++;
                }
            }
        }
        temp1 = new int[key.length()-h];
        for(int i=0;i< temp1.length;i++){
            if(key.charAt(i) == 48){
                temp1[i] = 0;
            }else {
                temp1[i] = 1;
            }
        }
        for(int i=0;i<image.getHeight();i++){
            for(int j=0;j<image.getWidth();j++){
                int pixel = image.getRGB(j,i);
                Color color = new Color(pixel, true);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red = red ^ generate();
                green = green ^ generate();
                blue = blue ^ generate();
                Color newcolor = new Color(red, green, blue);
                image.setRGB(j,i,newcolor.getRGB());
            }
        }
        return image;
    }

    public BufferedImage decrypt(BufferedImage image, String key) throws IOException {
        int h=0;
        for(int i=0;i<8;i++){
            if(key.length() > 20){
                if(key.charAt(i) != '.') {
                    h++;
                }
            }
        }
        int[] temp1 = new int[key.length()-h];
        for(int i=0;i< temp1.length;i++){
            if(key.charAt(i) == 48){
                temp1[i] = 0;
            }else {
                temp1[i] = 1;
            }
        }
        for(int i=0;i<image.getHeight();i++){
            for(int j=0;j<image.getWidth();j++){
                int pixel = image.getRGB(j,i);
                Color color = new Color(pixel, true);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red = red ^ generate();
                green = green  ^ generate();
                blue = blue ^ generate();
                Color newcolor = new Color(red, green, blue);
                image.setRGB(j,i,newcolor.getRGB());
            }
        }
        return image;
    }
    private int generate(){
        String stringKey = "";
        int[] tab = new int[8];
        for(int i=0;i<8;i++){
            int last = temp1[temp1.length - 1];
            tab[i] = temp1[0] ^ temp1[temp1.length - 1];
            for(int j=temp1.length - 1;j > 0;j--){
                temp1[j] = temp1[j - 1];
            }
            temp1[0] = tab[i];
        }
        for (int i=0;i<8;i++){
            stringKey = stringKey + tab[i];
        }
        int binaryKey = Integer.parseInt(stringKey,2);
        return binaryKey;
    }
}
