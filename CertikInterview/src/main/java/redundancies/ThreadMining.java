//package exercise5;
//
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class ThreadMining extends Thread implements BlockListener {
//
//    private static final List<BlockListener> listeners = new CopyOnWriteArrayList<>();
//    private final BlockingQueue<List<BlockChain>> queue = new LinkedBlockingQueue<>();
//
//    //teting this
////    private static final List<String> booleanListeners = new CopyOnWriteArrayList<>();
//
//
//    //we might need this for thread termination?
//    private volatile boolean running = true;
//
//    //constructor
//    public ThreadMining(String name){
//        listeners.add(this);
//        Thread.currentThread().setName(name);
//    }
//
//    @Override
//    public void messageReceived(List<BlockChain> chain){
//        try{
//            queue.put(chain);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void sendToAllOtherThreads(List<BlockChain> message){
//        for(BlockListener listener: listeners){
//            if(listener == this){
//                continue;
//            }
//            listener.messageReceived(message);
//        }
//    }
//
//    /**
//     * this run function for the thread creates a list<BlockChain>> objects, as an array list
//     * we havea  try block where we create a MineAndVerify block obj, which we will call methods from
//     * first check if queue is not empty, if that is the case, set chain = element on top of queue ,
//     * mine the next block with current thread as miner, send to all our other threads the current chain
//     */
//    @Override
//    public void run(){
//        List<BlockChain> chain = new ArrayList<>();
//        System.out.println("chain start: "+ chain.size());
//
//        try{
//            MineAndVerify block = new MineAndVerify();
//
////            if(!queue.isEmpty()){
////                chain = queue.take();
////                BlockChain newBlock = block.mineTheNextBlock(Character.getNumericValue(Thread.currentThread().getName().charAt(currentThread().getName().length()-1)));
////                chain.add(newBlock);
////                sendToAllOtherThreads(chain);
////                //sending
////                System.out.println("sending chain: size "+chain.size());
////            }
//
//            //we will hit the 10th iteration
//            while (chain.size() < 10 ){
//                int minerId = Character.getNumericValue(Thread.currentThread().getName().charAt(currentThread().getName().length()-1));
//                System.out.println("Current miner: " +minerId);
//
//                BlockChain newBlock = block.mineTheNextBlock(minerId);
//
//                if(newBlock != null){
//                    chain.add(newBlock);
//                    sendToAllOtherThreads(chain);
//                }
//
//                List<BlockChain> newChain;
//                //changed from take to peek
////                newChain = queue.peek();
////                if(block.verifyChain(newChain) && newChain.size() > chain.size()){
////                    chain = newChain;
////                }
//
//                //Trying new approach
//                List<List<BlockChain>> incomingChains = new ArrayList<>();
//                queue.drainTo(incomingChains);
////                queue.addAll(incomingChains);
//                //incoming
//                System.out.println("incoming size: "+incomingChains.size() );
//
//                for(List<BlockChain> incomingChain : incomingChains){
//                    if(block.verifyChain(incomingChain) && incomingChain.size() > chain.size()){
//                        chain = incomingChain;
//                    }
//                }
//
//                if(chain.size() == 10) {
//                    sendToAllOtherThreads(chain);
//                    for(int i = 0; i < 10; i++ ){
//                        System.out.println("Block_"+i+" : "+"{'nonce' : " + chain.get(i).getNonce() + " , 'miner': " + chain.get(i).getMiner());
//                    }
//                    break;
//
//                }
//
//                System.out.println("current chain size: "+chain.size()+" current thread: "+Thread.currentThread());
//
//            }
//            //print
//
//
//
//        }catch (UnsupportedEncodingException | NoSuchAlgorithmException  e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    public static void main(String[] args){
//        System.out.println("We will create 10 threads to show mining in a decentralized manner");
//
//        ThreadMiners thread0 = new ThreadMiners("Thread0");
//        ThreadMiners thread1 = new ThreadMiners("Thread1");
//        ThreadMiners thread2 = new ThreadMiners("Thread2");
//        ThreadMiners thread3 = new ThreadMiners("Thread3");
//        ThreadMiners thread4 = new ThreadMiners("Thread4");
//        ThreadMiners thread5 = new ThreadMiners("Thread5");
//        ThreadMiners thread6 = new ThreadMiners("Thread6");
//        ThreadMiners thread7 = new ThreadMiners("Thread7");
//        ThreadMiners thread8 = new ThreadMiners("Thread8");
//        ThreadMiners thread9 = new ThreadMiners("Thread9");
//
//        thread0.start();
//        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        thread5.start();
//        thread6.start();
//        thread7.start();
//        thread8.start();
//        thread9.start();
//
//    }
//
//
//
//
//}
///*
//Notes: We need maybe a global list our threads can access...so we can keep track of our list....
// */