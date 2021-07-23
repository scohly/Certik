package exercise4;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;



public class InterThreadComms extends Thread implements Listener {

    private static final List<Listener> listeners = new CopyOnWriteArrayList<>();
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    private volatile boolean running = true;
    private int numberOfMsgs;


    //Constructor
    public InterThreadComms(String threadName, int numMsgsToExpect){
        listeners.add(this);
        Thread.currentThread().setName(threadName);
        numberOfMsgs = numMsgsToExpect;
    }

    /**
     *
     * @param messsage : the message that will be broadcasted to all our threads, accessed via blockingqueue
     */
    @Override
    public void messageReceived(String messsage){
        try{
            queue.put(messsage);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * takes no input, we run our thread from here
     * from within the thread a random alphanumeric stirng is generated (length of 10)
     * this message is printed on console with the name of the current thread
     * message is then sent to all other threads using the method : sendToAllOtherThreads(message)
     */
    @Override
    public void run(){
        String msg = generateRandomAlpha();
        System.out.println(getName()+ " sending message " + msg + "  to all other threads");
        sendToAllOtherThreads(msg);

        try{
            //while numThreads > 0 , this is passed in via constructor
            while(numberOfMsgs > 0){

                String message = queue.take();
                System.out.println(getName() + " received message: "+ message);

                //after each print we decrease number of msgs to read
                numberOfMsgs--;

            }
        }catch (InterruptedException e){
        e.printStackTrace();
        }
    }


    /**
     *  method takes no input
     *  we set the count for chars in the random alphanumeric to 10 within the method
     *  use apache commons library to use method randomAlpanumeric(int charCount) to generate our string
     *
     * @return string with 10 random alphanumerics
     */
    public static String generateRandomAlpha(){
        int charCount = 10;
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }

    /**
     *  this is our broadcast to other thread method,
     * @param message , the message that is to be sent
     */
    public void sendToAllOtherThreads(String message){
        for(Listener listener: listeners){
            if(listener == this){
                continue;
            }
            listener.messageReceived(message);
        }
    }


    public static void main(String[] args){

        InterThreadComms thread0 = new InterThreadComms("Thread-0",2);
        InterThreadComms thread1 = new InterThreadComms("Thread-1",2);
        InterThreadComms thread2 = new InterThreadComms("Thread-2",2);

        thread0.start();
        thread1.start();
        thread2.start();

    }


}
