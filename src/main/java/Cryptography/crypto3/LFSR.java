package Cryptography.crypto3;

import java.util.*;
import java.util.concurrent.Callable;


public class LFSR implements Callable<Boolean[]> {

    private String userPolynomialInput = "";
    private boolean[] seed;
    private Integer[] taps;
    LinkedList<Boolean> cipher = new LinkedList<>();


    public void setUserPolynomialInput(String userPolynomialInput) {
        this.userPolynomialInput = userPolynomialInput;
    }

    public void initialize(){
        initalizeSeed(convertUserInput(userPolynomialInput));
    }

    public Integer[] convertUserInput(String userInput){
        LinkedList<Integer> convertedUserInput = new LinkedList<>();

        String[] polynomialPowers = userInput.split(",");
        for (String s : polynomialPowers
             ) {
            convertedUserInput.add(Integer.parseInt(s));
        }

        Integer[] arr = convertedUserInput.toArray(new Integer[convertedUserInput.size()]);
        return arr;
    }

    @Override
    public Boolean[] call() throws Exception {
        step();
        return cipher.toArray(new Boolean[0]);
    }

    private boolean step(){
        for (int i = 0; i < seed.length ; i++) {

            System.out.print(" [" + i + "]" + ": " + seed[i]);

        }
        boolean returnBit = seed[seed.length-1];
        System.out.println(" Return bit " + returnBit);
        boolean[] shiftedSeed = new boolean[seed.length];
        boolean xoredBits=seed[taps[0]];

        if(taps.length==2){
            xoredBits = seed[taps[0]]^seed[taps[1]];
        }else{
            for (int i = 1; i < taps.length;i++){
                xoredBits = xoredBits ^ seed[taps[i]];
            }
        }
        shiftedSeed[0] = xoredBits;

        for (int i = 1; i < seed.length ; i++) {
            shiftedSeed[i] = seed[i-1];
        }

        seed = shiftedSeed;
        cipher.add(returnBit);
        return returnBit;
    }

    private void initalizeSeed(Integer[] inputArray){
        Optional<Integer> maxArrNum = Arrays.stream(inputArray).max(Integer::compareTo);
        taps = inputArray;
        seed = new boolean[maxArrNum.get()+1];
        for (int i = 0; i <seed.length ; i++) {
            seed[i] = new Random().nextBoolean();
        }
    }

    public Boolean[] algorithm(){
        initalizeSeed(convertUserInput(userPolynomialInput));
        boolean[] res = new boolean[8];
        for (int i = 0; i < 8; i++) {
            cipher.add(step());
        }
        return cipher.toArray(cipher.toArray(new Boolean[0]));
    }
}
