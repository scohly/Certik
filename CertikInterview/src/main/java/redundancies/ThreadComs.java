package redundancies;

import exercise4.Listener;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadComs extends Thread implements Listener {

    private static final List<Listener> listeners = new CopyOnWriteArrayList<>();
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    private volatile boolean running = true;



    public ThreadComs(String name){
        listeners.add(this);
        Thread.currentThread().setName(name);
    }

    @Override
    public void messageReceived(String messsage){
        try{
            queue.put(messsage);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String msg = generateRandomAlpha();
        System.out.println(getName()+ " sending message " + msg + "  to all other threads");
        sendToAllOtherThreads(msg);

        while(running){
            try{
                String message = queue.take();
                System.out.println(getName() + " received message: "+ message);

//                Thread.sleep(3000);

                if("STOP".equals(message)){
                    break;
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
//    public void halt(){
//        exit = true;
//    }

    public static String generateRandomAlpha(){
        int charCount = 10;
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }

    public void sendToAllOtherThreads(String message){
        for(Listener listener: listeners){
            if(listener == this){
                continue;
            }
            listener.messageReceived(message);
        }
    }

    public void stopThread(){
        running = false;
        interrupt();
    }

    public static void main(String[] args){
        ThreadComs thread0 = new ThreadComs("Thread-0");
        ThreadComs thread1 = new ThreadComs("Thread-1");
        ThreadComs thread2 = new ThreadComs("Thread-2");

        thread0.start();
        thread1.start();
        thread2.start();


//        thread0.sendToAllOtherThreads("STOP");
//        thread1.sendToAllOtherThreads("STOP");
//        thread2.sendToAllOtherThreads("STOP");
        thread0.stopThread();



    }


}
