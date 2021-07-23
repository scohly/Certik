package redundancies;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumerExecutorService {

    public static String generateRandomAlpha(){
        int charCount = 10;
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>(2);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable producerTask = () -> {
            try {
                String value = generateRandomAlpha();

                blockingQueue.put(value);

                System.out.println("Produced " + value);

                value = generateRandomAlpha();

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable consumerTask = () -> {
            try {

                String value = blockingQueue.take();

                System.out.println("Consume " + value);

                Thread.sleep(1000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        executor.execute(producerTask);
        executor.execute(consumerTask);


        executor.shutdown();
    }
}
