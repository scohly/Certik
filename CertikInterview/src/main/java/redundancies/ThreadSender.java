package redundancies;

import exercise4.ConcurrentArrayList;
import exercise4.RandomMsg;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadSender extends Thread{

    private ConcurrentArrayList<RandomMsg> messages;
    private int numMsgs;


    ThreadSender(ConcurrentArrayList<RandomMsg> listMsgs, int numMsgs){

        this.messages = listMsgs;
        this.numMsgs = numMsgs;
    }




    //we print
    public void run(){
        String msg = generateRandomAlpha();
        RandomMsg ranMsg = new RandomMsg(Thread.currentThread().getId(),msg);
        messages.add(ranMsg);
        String currentThread = Thread.currentThread().getName();
        System.out.println(currentThread + " sending message "+msg);

        //debug
        System.out.println(messages.getSize());

        int index = 0;
        while (index != numMsgs){
            if(index < messages.getSize()){
                RandomMsg randomMessage = messages.get(index);
                if(randomMessage.getThreadId() != currentThread().getId()){
                    currentThread = Thread.currentThread().getName();
                    int currentId = Thread.currentThread().getName().charAt(currentThread.length()-1);
                    System.out.println("Thread-"+currentId + " received message "+randomMessage.getMessage());
                }
                index++;
            }
        }
    }

    public  String generateRandomAlpha(){
        int charCount = 10;
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }

    public static void main(String[] args){
        ConcurrentArrayList<RandomMsg> listOfMsgs = new ConcurrentArrayList<>();
        ThreadSender threadSender1 = new ThreadSender(listOfMsgs,3);
        ThreadSender threadSender2 = new ThreadSender(listOfMsgs,3);
        ThreadSender threadSender3 = new ThreadSender(listOfMsgs, 3);

        threadSender1.run();
        threadSender2.run();
        threadSender3.run();
    }

}
