package exercise5;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class DecentralizedMining extends Thread implements BlockListener {


    private static final List<BlockListener> listeners = new CopyOnWriteArrayList<>();
    private final BlockingQueue<List<BlockChain>> queue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    //keeping track of the Hash
    private final Stack<String> stack = new Stack<>();

    //testing this
//    private static final List<String> booleanListeners = new CopyOnWriteArrayList<>();


    //we might need this for thread termination?
    private volatile boolean running = true;

    //constructor
    public DecentralizedMining(String name){
        listeners.add(this);
        Thread.currentThread().setName(name);
    }

    @Override
    public void messageReceived(List<BlockChain> chain){
        try{
            queue.put(chain);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void booleanReceived(String msg){
        try{
            messages.put(msg);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void sendToAllOtherThreads(List<BlockChain> message){
        for(BlockListener listener: listeners){
            if(listener == this){
                continue;
            }
            listener.messageReceived(message);
        }
    }

    public void sendAll(String msg){
        for(BlockListener listener: listeners){
            if (listener == this){
                continue;
            }
            listener.booleanReceived(msg);
        }
    }

    /**
     * this run function for the thread creates a list<BlockChain>> objects, as an array list
     * we havea  try block where we create a MineAndVerify block obj, which we will call methods from
     * first check if queue is not empty, if that is the case, set chain = element on top of queue ,
     * mine the next block with current thread as miner, send to all our other threads the current chain
     */
    @Override
    public void run(){
        List<BlockChain> chain = new ArrayList<>();
        System.out.println("chain start: "+ chain.size());

        boolean found = false;

        try{
            MineAndVerify block = new MineAndVerify();

//            if(!queue.isEmpty()){
//                chain = queue.take();
//                BlockChain newBlock = block.mineTheNextBlock(Character.getNumericValue(Thread.currentThread().getName().charAt(currentThread().getName().length()-1)));
//                chain.add(newBlock);
//                sendToAllOtherThreads(chain);
//                //sending
//                System.out.println("sending chain: size "+chain.size());
//            }

            if(!messages.isEmpty()){
                if(messages.take().equals("found")){
                    found = true;
                }
            }

            //we will hit the 10th iteration
            while (chain.size() < 10 && !found){
//                int minerId = Character.getNumericValue(Thread.currentThread().getName().charAt(currentThread().getName().length()-1));
//                System.out.println("Current miner: " +minerId);

                BlockChain newBlock = mineBlock(chain);

                if(newBlock != null){
                    chain.add(newBlock);
                    sendToAllOtherThreads(chain);
                }

                if(!messages.isEmpty()){
                    if(messages.peek().equals("found")){
                        break;
                    }
                }



                //Trying new approach
                List<List<BlockChain>> incomingChains = new ArrayList<>();
                queue.drainTo(incomingChains);
//                queue.addAll(incomingChains);
                //incoming
                System.out.println("incoming size: "+incomingChains.size() );

                for(List<BlockChain> incomingChain : incomingChains){
                    if(block.verifyChain(incomingChain) && incomingChain.size() > chain.size()){
                        chain = incomingChain;
                    }
                }

                if(chain.size() == 10) {
                    sendToAllOtherThreads(chain);
                    sendAll("found");
                    for(int i = 0; i < 10; i++ ){
                        System.out.println("Block_"+i+" : "+"{'nonce' : " + chain.get(i).getNonce() + " , 'miner': " + chain.get(i).getMiner());
                    }
                    break;

                }

                System.out.println("current chain size: "+chain.size()+" current thread: "+Thread.currentThread());

            }
            System.out.println("A chain was found!");
            //print



        }catch (UnsupportedEncodingException | NoSuchAlgorithmException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    //BlockChain Methods go HERE
    //same method from exervise 2 but this time we pass in a List<BlockCahin> chain
    public BlockChain mineBlock(List<BlockChain> chain) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        //genesis block creation
        List<BlockChain> links = new ArrayList<>();

        int minerId = Character.getNumericValue(Thread.currentThread().getName().charAt(currentThread().getName().length()-1));

        if(stack.isEmpty() && chain.isEmpty()){

            BlockChain block_0 = new BlockChain(minerId);

            String miner = block_0.getMiner().toString();

            String genesisNonce = solveHash(miner);

            block_0.setNonce(genesisNonce);
            String currentHash = hashSHA256(block_0.getMiner().toString()+block_0.getNonce());
            stack.push(currentHash);
//            blockChain.add(block_0);

            return block_0;
        }
        else{
            BlockChain block_i = new BlockChain(minerId);

            String miner = block_i.getMiner().toString();
            String hash = stack.peek();
//            System.out.println(hash);

            String block_iNonce = solveHash(miner+hash);
//            block_i.setNonce(solveHash(stack.pop().getHash()+block_i.getMiner().toString(),generateRandomAlpha(35)));
            block_i.setNonce(block_iNonce);
            String currentHash = hashSHA256(block_i.getMiner().toString()+stack.peek());
            stack.push(currentHash);

            return block_i;
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







    public static void main(String[] args){
        System.out.println("We will create 10 threads to show mining in a decentralized manner");

        DecentralizedMining thread0 = new DecentralizedMining("Thread0");
        DecentralizedMining thread1 = new DecentralizedMining("Thread1");
        DecentralizedMining thread2 = new DecentralizedMining("Thread2");
        DecentralizedMining thread3 = new DecentralizedMining("Thread3");
        DecentralizedMining thread4 = new DecentralizedMining("Thread4");
        DecentralizedMining thread5 = new DecentralizedMining("Thread5");
        DecentralizedMining thread6 = new DecentralizedMining("Thread6");
        DecentralizedMining thread7 = new DecentralizedMining("Thread7");
        DecentralizedMining thread8 = new DecentralizedMining("Thread8");
        DecentralizedMining thread9 = new DecentralizedMining("Thread9");

        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();

    }

}
/*
Notes: We need maybe a global list our threads can access...so we can keep track of our list....
 */