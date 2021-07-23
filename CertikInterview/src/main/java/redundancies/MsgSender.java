package redundancies;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.ThreadLocalRandom;

public class MsgSender implements Runnable{
    private Message msg;

    public MsgSender(Message msg){
        this.msg = msg;
    }


    public String generateRandomAlpha(){
        int charCount = 10;
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }

    public void run(){
        String randStr = generateRandomAlpha();
        System.out.println("sending message "+randStr+" to all other threads.");
        msg.send(randStr);

        try{
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000,5000));
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread Interrupted");
        }
    }







}
