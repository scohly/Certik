package exercise5;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MineAndVerify {
    //create these static stack/list to assist in tracking the various variables required to solve the hash/nonce
    static Stack<BlockChainHash> stack = new Stack<>();
//    static List<exercise2.BlockHash> hashList = new ArrayList<>();


    /**
     *
     * @param blockChain
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     *
     * this method return type is void, param is a List of exercise2.Block objects and determines based on each
     * hash whether or not it can be verified
     * the criteria for verification is having a hash that has the following prefix: 0x0000
     * if verified, it will be print with the nonce followed by the miner
     */
    public  Boolean verifyChain(List<BlockChain> blockChain) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String currentHash = null;
        String hashPrefixGoal = StringUtils.repeat("0", 4);
        Stack<String> hashStack = new Stack<>();

        for(int i = 0; i < blockChain.size(); i++){
            //genesis block check
            if(hashStack.isEmpty()){
                currentHash = hashSHA256(blockChain.get(i).getMiner().toString() + blockChain.get(i).getNonce());
                hashStack.push(currentHash);

                if(!(currentHash.substring(0,4).equalsIgnoreCase(hashPrefixGoal))){
//                    System.out.println("Block_"+i+" : "+"{'nonce' : " + blockChain.get(i).getNonce() + " , 'miner': " + blockChain.get(i).getMiner());
                    return false;
                }
            }
            else{
                currentHash = hashSHA256(blockChain.get(i).getMiner().toString() + hashStack.pop() + blockChain.get(i).getNonce());
                hashStack.push(currentHash);
                if(!(currentHash.substring(0,4).equalsIgnoreCase(hashPrefixGoal))){
//                    System.out.println("Block_"+i+" : "+"{'nonce' : " + blockChain.get(i).getNonce() + " , 'miner': " + blockChain.get(i).getMiner());
                    return false;
                }
            }

        }
        return true;
    }


    /**
     *
     * @return a exercise2.Block
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     *
     * this method returns a exercise2.Block object which comprises of two fields: int Miner, String Nonce
     * the method also calls the solveHash method (if the genesis block, then with just the miner) if being called,
     * returns the genesis block
     *
     * for other blocks, the method solveHash will take the miner,preceeding blocks hash, and will solve ,
     * after which the blocks nonce will be set to the solved nonce
     * a exercise2.BlockHash object is then constructed with the newly formed block, and Hash (which is calculated with hashSHA256 method)
     * finally the block is returned
     */
    public BlockChain mineTheNextBlock(int minerId) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        //genesis block creation
        if(stack.isEmpty()){
            BlockChain block_0 = new BlockChain(minerId);

            String miner = block_0.getMiner().toString();

            String genesisNonce = solveHash(miner);

            block_0.setNonce(genesisNonce);
            BlockChainHash chain = new BlockChainHash(block_0, hashSHA256(block_0.getMiner().toString()+block_0.getNonce()));
            stack.push(chain);
//            blockChain.add(block_0);
            return block_0;
        }
        else{
            BlockChain block_i = new BlockChain(minerId);

            String miner = block_i.getMiner().toString();
            String hash = stack.peek().getHash();
//            System.out.println(hash);

            String block_iNonce = solveHash(miner+hash);
//            block_i.setNonce(solveHash(stack.pop().getHash()+block_i.getMiner().toString(),generateRandomAlpha(35)));
            block_i.setNonce(block_iNonce);
            BlockChainHash blockLink = new BlockChainHash(block_i, hashSHA256(block_i.getMiner().toString()+stack.peek().getHash()+block_i.getNonce()));
            stack.push(blockLink);
//            hashList.add(blockLink);
            return block_i;
        }

    }

    public List<BlockChain> mineBlocks(List<BlockChain> chain, int minerId) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        //genesis block creation
        List<BlockChain> links = new ArrayList<>();

        if(stack.isEmpty() && chain.isEmpty()){

            BlockChain block_0 = new BlockChain(minerId);

            String miner = block_0.getMiner().toString();

            String genesisNonce = solveHash(miner);

            block_0.setNonce(genesisNonce);
            BlockChainHash chainLink = new BlockChainHash(block_0, hashSHA256(block_0.getMiner().toString()+block_0.getNonce()));
            stack.push(chainLink);
//            blockChain.add(block_0);
            links.add(block_0);
            return links;
        }
        else{
            BlockChain block_i = new BlockChain(minerId);

            String miner = block_i.getMiner().toString();
            String hash = stack.peek().getHash();
//            System.out.println(hash);

            String block_iNonce = solveHash(miner+hash);
//            block_i.setNonce(solveHash(stack.pop().getHash()+block_i.getMiner().toString(),generateRandomAlpha(35)));
            block_i.setNonce(block_iNonce);
            BlockChainHash blockLink = new BlockChainHash(block_i, hashSHA256(block_i.getMiner().toString()+stack.peek().getHash()+block_i.getNonce()));
            stack.push(blockLink);
//            hashList.add(blockLink);
            links.add(block_i);
            return links;
        }

    }



    /**
     *
     * @param inputText , text that we will find the nonce for that when combined with the input yields a 0x0000 prefix hash
     * @return String nonce
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     *
     * this method was borrowed from ex1 and changed to have just 1 input aka input text,
     * leading zeros declared within method same with the hashPrefixGoal
     * we set size equal to 100-inputText.length which for every block besides genesis will have sie 35
     * we also call the method generateRandomAlpha from within the whileLoop to get our random gen string which we
     * use to find the hash for with "inputText"
     */
    public  String solveHash(String inputText) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int leadingZeros = 4;

        String hashPrefixGoal = StringUtils.repeat("0", leadingZeros);

        String currentNonce = "";

        int size = 100 - inputText.length();

        String currentHash = hashSHA256(inputText+currentNonce);
        while(!currentHash.substring(0,leadingZeros).equalsIgnoreCase(hashPrefixGoal)){
            currentNonce = generateRandomAlpha(size);
            currentHash = hashSHA256(inputText + currentNonce);
        }
        return currentNonce;
    }

    //input is text we want to find a hashFor

    /**
     *
     * @param textInput ; text we will be hashing (using SHA 256)
     * @return ; returns the SHA256 hash for the input text
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * I went online to learn how to compute the hash using MessageDigest and this is the cleanest way I could find
     */
    public  String hashSHA256(String textInput) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(textInput.getBytes("UTF-8"));

        byte[] digest = messageDigest.digest();
        return String.format("%064x", new java.math.BigInteger(1,digest));
    }

    /**
     *
     * @param charCount ; this method takes in an int specifying length of the random alphanumeric string to be returned
     * @return ; a randomly generated alphanumeric strick
     *
     *  NOTE: I had to implement the following: 'org.apache.commons:commons-lang3:3.12.0' which sped up the alphanumeric
     *  string generation, read online that this was a clean and fast way to generate a random alphanumeric string
     */
    public  String generateRandomAlpha(int charCount){
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }



    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        int minerId = 0;
        MineAndVerify blockNew = new MineAndVerify();
        List<BlockChain> blocksToMine = new ArrayList<>();
        System.out.println("We want to simulate mining 10 blockchains ");
        for(int i = 0 ; i < 10; i++){
            blocksToMine.add(blockNew.mineTheNextBlock(minerId));
            System.out.println("Block_"+i+" : "+"{'nonce' : " + blocksToMine.get(i).getNonce() + " , 'miner': " + blocksToMine.get(i).getMiner());
        }

        List<String> hash = new ArrayList<>();
        while (!stack.isEmpty()){
            hash.add(stack.pop().getHash());
        }

        System.out.println();
        //this is just aux hash printing function to track the progress
        Collections.reverse(hash);
        for(int i = 0; i < hash.size(); i++){
            System.out.println("SHA256: 0x0000 : "+i+": "+hash.get(i));
        }


//        verifyChain(blockList);
        System.out.println();

        System.out.println("Verified blockchain: ");
        if(blockNew.verifyChain(blocksToMine)){
            for(int i = 0; i < 10; i++){
                System.out.println("Block_"+i+" : "+"{'nonce' : " + blocksToMine.get(i).getNonce() + " , 'miner': " + blocksToMine.get(i).getMiner());
            }
        }



    }
}
