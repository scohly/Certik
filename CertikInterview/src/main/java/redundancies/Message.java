package redundancies;

import com.oracle.tools.packager.Log;

public class Message {
    private String message;

    //True if our receiver needs to wait (think for multiple threads)
    //False if sender should wait

    private boolean transferStatus = true;

    public synchronized void send(String data){
        while(!transferStatus){
            try{
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread Interrupted");

            }
        }
        transferStatus = false;
        this.message = data;
        notifyAll();
    }
    public synchronized String receive(){
        while(transferStatus){
            try{
                wait();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread Interrupted");
            }
        }
        transferStatus = true;
        notifyAll();
        return message;
    }
}
