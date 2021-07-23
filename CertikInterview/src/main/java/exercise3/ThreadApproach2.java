package exercise3;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadApproach2 implements Runnable {


    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            int threadId = Character.getNumericValue(threadName.charAt(threadName.length()-1));
//            char threadNum = threadName.charAt(threadName.length()-1);
            String time = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
            System.out.println(threadName +": ["+threadId+"] -- time: "+time);

        };
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();


//        thread1.join();





    }

    @Override
    public void run() {

    }
}
