//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.commons.lang3.StringUtils;
////import org.bouncycastle.crypto.digests.SHA256Digest;
//
//import java.io.UnsupportedEncodingException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class FindNonce {
//    //member vars to use
//    MessageDigest messageDigest;
//    private String challengeText;
//    private int timeToSolveMS;
//    private String successfulNonce;
//    private int successfulNonceINT;
//    private String successfulHash;
//
//    public FindNonce() throws NoSuchAlgorithmException {
//        this.messageDigest = MessageDigest.getInstance("SHA-256");
//    }
//
//    public String solveHash(String inputText, String randStr, int leadingZeros) throws UnsupportedEncodingException {
//        this.challengeText = inputText;
//        String hashPrefixGoal = StringUtils.repeat("0", leadingZeros);
//
//        //find time to succeed /measure
//        long startTime = System.nanoTime();
//        NumberFormat formatter =  new DecimalFormat();
//        formatter = new DecimalFormat("0.#####E0");
//
//        //will use int nonceInteger to track nonces
//        int nonceInteger = 0;
//        String currentNonce = getNonceFromInteger(nonceInteger);
//        System.out.println("currentNonce: "+inputText+" "+currentNonce);
//        int size = 100 - inputText.length();
//
//        String currentHash = hashSHA256(inputText+currentNonce);
//        while(!currentHash.substring(0,leadingZeros).equalsIgnoreCase(hashPrefixGoal)){
//            nonceInteger += 1;
////            currentNonce = getHexNonceFromInteger(nonceInteger);
//            currentNonce = generateRandomAlpha(size);
//            currentHash = hashSHA256(challengeText + currentNonce);
//
//            if(nonceInteger % 500000 == 0){
//                System.out.println(" % of nonces tried: "+ formatter.format(nonceInteger));
//            }
//        }
//        long estimatedTime = System.nanoTime() - startTime;
//
//        this.successfulNonce = currentNonce;
//        this.successfulNonceINT = nonceInteger;
//        this.successfulHash = currentHash;
//        this.timeToSolveMS = (int) Math.floor(estimatedTime/1000000.0);
//
//        System.out.println("currentNonce: "+inputText+currentNonce);
////        System.out.println(getAlphaFromHex(currentNonce));
//
//        return this.successfulNonce;
//    }
//
//    //input is text we want to find a hashFor
//    public String hashSHA256(String textInput) throws UnsupportedEncodingException{
//        this.messageDigest.update(textInput.getBytes("UTF-8"));
//
//        byte[] digest = this.messageDigest.digest();
//        return String.format("%064x", new java.math.BigInteger(1,digest));
//    }
//
//
//    //method to find the sha256 hash
//    private static String calculateHash(String input) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(input.getBytes());
//
//        byte[] digest = md.digest();
//        StringBuffer sb = new StringBuffer();
//
//        for(byte b: digest){
//            sb.append(String.format("%02x", b  & 0xff));
//        }
//
//        return sb.toString();
//    }
//
//
//
//    public static String generateRandomAlpha(int charCount){
//        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
//        return alphaNum;
//    }
//
//    public static String getUserIn(){
//        Scanner in = new Scanner(System.in);
//        System.out.println("Pleaes enter string that does not exceed 70 chars");
//        String userIN = "";
//        while(true){
//            userIN = in.nextLine();//get userIn from stdIN
//            if(userIN.length() <= 70)
//                break;
//            System.out.println("please reEnter the string....");
//        }
//        return userIN;
//
//    }
//
//
//
//
//
//
//    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
////        Scanner in = new Scanner(System.in);
//
//        //test
//        //String userIn = "3"; //no more than length 70 fyi
//
//        String userIn = getUserIn();
//
//
//        //code we can encapsulate...
//        FindNonce nonce = null;
//        try{
//            nonce = new FindNonce();
//        }catch(NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//
//        String successNonce = null;
//
//        int charSize = 100 - userIn.length();
//        String randomStr = generateRandomAlpha(charSize);
//
//        int zerosWeWant = 5;
//        try{
//            successNonce = nonce.solveHash(userIn , randomStr, zerosWeWant);
//        }catch(UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//
//        NumberFormat format = new DecimalFormat();
//        format = new DecimalFormat("0.#####E0");
//
//        //end of encap
//
//        System.out.println("\nSuccesful nonce which yields a hash with" +zerosWeWant+ " leading 0's: " + successNonce);
//        System.out.println("Random alphanumeric padded string: "+successNonce +" length : "+successNonce.length());
//        System.out.println("the userIN: "+userIn);
//        System.out.println("256 Hash: "+nonce.getSuccessfulHash());
//        System.out.println("Time to solve: " + nonce.getTimeToSolveMS() +"ms"+ " with this many nonces: "+ format.format(nonce.getSuccessfulNonceINT()));
//        System.out.println("\n"+calculateHash(userIn+successNonce));
//
//
//    }
//
//
//
////    public static void findNonce(String input) throws UnsupportedEncodingException {
////        SHA256Digest SHA = new SHA256Digest();
////
////        byte[] digest = new byte[32];
////
////        byte[] textBytes;
////        long nonce = 0L;
////
////        String message = input;
////
////        boolean foundNonce;
////
////        do{
////            //calculate digest
////            textBytes = (message+nonce).getBytes("UTF-8");
////            SHA.update(textBytes,0,textBytes.length);
////            SHA.doFinal(digest,0);
////
////            //first we want 4 0's
////            foundNonce = digest[0] == 0 && digest[1] == 0 && digest[2] == 0 && digest[3] == 0;
////
////            //try next nonce
////            nonce++;
////        }while(!foundNonce);
////        System.out.println("FOund at: SHA256("+message+(nonce-1L)+")");
////        System.out.println("SHA256 digest = "+ Arrays.toString(digest));
////    }
//
//    public static void findTheNonce(String userIn) throws NoSuchAlgorithmException {
//        if(userIn.length() > 70){
//            throw new IllegalArgumentException("Re-enter a valid string");
//        }
//
//        int charSize = 100-userIn.length();
//
//
//        boolean nounceFound = false;
//        int iterationCount = 0;
//
//        String randomPad = generateRandomAlpha(charSize);
//        String hash = calculateHash(userIn+randomPad);
//
//        while(!nounceFound){
//            if(hash.substring(0,3).equals("0000")) {
//                nounceFound = true;
//            }
//
//            randomPad = generateRandomAlpha(charSize);
//            hash = calculateHash(userIn+randomPad);
//            iterationCount++;
//
//        }
//
//        System.out.println("The nounce for the given hash: "+hash + "is: "+randomPad);
//        System.out.println("this many iterations: "+iterationCount);
//
//    }
//
//    private byte[] toBytes(int i){
//        byte[] result = new byte[4];
//
//        result[0] = (byte) (i >> 24);
//        result[1] = (byte) (i >> 16);
//        result[2] = (byte) (i >> 8);
//        result[3] = (byte) (i /* >> 0 */);
//
//        return result;
//    }
//
//    private String getHexNonceFromInteger(int nonceInt){
//        return String.format("%x", new java.math.BigInteger(1,toBytes(nonceInt)));
//    }
//
//    private String getNonceFromInteger(int nonceInt){
//        return String.format("%x", new java.math.BigInteger(1,toBytes(nonceInt)));
//    }
//
////    private String getAlphaFromHex(String hexStr){
////        StringBuilder sb = new StringBuilder(" ");
////        for(int i = 0; i < hexStr.length(); i += 2){
////            String str = hexStr.substring(i,i+2);
////            sb.append((char) Integer.parseInt(str,16));
////        }
////        return sb.toString();
////    }
//
//    public String getChallengeText() {
//        return this.challengeText;
//    }
//
//    public int getTimeToSolveMS(){
//        return this.timeToSolveMS;
//    }
//
//    public String getSuccesfulNonce(){
//        return this.successfulNonce;
//    }
//
//    public int getSuccessfulNonceINT(){
//        return successfulNonceINT;
//    }
//
//    public String getSuccessfulHash(){
//        return this.successfulHash;
//    }
//}
