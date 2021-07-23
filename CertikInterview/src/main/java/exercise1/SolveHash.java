package exercise1;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Scanner;

public class SolveHash {

    /**
     *
     * @param inputText : this can be userInput...max length is 70
     * @param randStr : this is a random generated alphanumeric string which is basically size 100-userIn.lengtH()
     * @param leadingZeros : the number of zeros we want our hash to have as a "prefix"
     * @return : the method will return a hash
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public  String solveHash(String inputText, String randStr, int leadingZeros) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String challengeText = inputText;
        String hashPrefixGoal = StringUtils.repeat("0", leadingZeros);

        //find time to succeed /measure
        long startTime = System.nanoTime();
        NumberFormat formatter =  new DecimalFormat();
        formatter = new DecimalFormat("0.#####E0");

        //will use int nonceInteger to track nonces
        int nonceInteger = 0;
        //String currentNonce = getNonceFromInteger(nonceInteger); //setting String currentNone = randStr
        String currentNonce = randStr;

//        System.out.println("currentNonce: "+inputText+" "+currentNonce);
        int size = 100 - inputText.length();

        String currentHash = hashSHA256(inputText+currentNonce);
        while(!currentHash.substring(0,leadingZeros).equalsIgnoreCase(hashPrefixGoal)){
            nonceInteger += 1;
            //currentNonce = getHexNonceFromInteger(nonceInteger);
            currentNonce = generateRandomAlpha(size);
            currentHash = hashSHA256(challengeText + currentNonce);

        }
        long estimatedTime = System.nanoTime() - startTime;



        System.out.println("\ncurrentNonce: "+inputText+currentNonce);
        System.out.println("Time to solve(MS): "+(int)Math.floor(estimatedTime/1000000.0));

        return currentNonce;
    }

    //input is text we want to find a hashFor

    /**
     *
     * @param textInput , what we supply the hash function to find a SHA256 hash for
     * @return a sha256 hash
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public  String hashSHA256(String textInput) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(textInput.getBytes("UTF-8"));

        byte[] digest = messageDigest.digest();
        return String.format("%064x", new java.math.BigInteger(1,digest));
    }

    public  String generateRandomAlpha(int charCount){
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }

    public  String getUserIn(){
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter string that does not exceed 70 chars or have any spaces");
        String userIN = "";
        while(true){
            userIN = in.nextLine();//get userIn from stdIN
            if(userIN.length() > 70)
                break;
            System.out.println("please re-enter the string....");
        }
        return userIN;

    }






    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        //create the SolveHash object, named hashSolver
        SolveHash hashSolver = new SolveHash();

        //commented out for simplicity/testing, uncomment for userIn
        /*
        String userIn = hashSolver.getUserIn();
        */

        String userIn = "HelloCertik";

        String successNonce = null;

        if(userIn.length() <= 70){
            int charSize = 100 - userIn.length();
            String randomStr = hashSolver.generateRandomAlpha(charSize);

            int zerosWeWant = 4;

            //find nonce
            successNonce = hashSolver.solveHash(userIn , randomStr, zerosWeWant);

            NumberFormat format = new DecimalFormat();
            format = new DecimalFormat("0.#####E0");

            //end of encap

            System.out.println("\nSuccesful nonce which yields a hash with " +zerosWeWant+ " leading 0's: " + successNonce);
            System.out.println("Random alphanumeric padded string: "+successNonce +" length : "+successNonce.length());
            System.out.println("the userIN: "+userIn);
            System.out.println("\n"+"256 Hash: "+ hashSolver.hashSHA256(userIn+successNonce));
        }
        else{
            System.out.println("the userIn exceeded length 70, please provide valid input!");
        }




    }

}
