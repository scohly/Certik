package redundancies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueExample {
    public static void main(String... argv) throws Exception {
        final BlockingQueue<Integer> input = new ArrayBlockingQueue<>(50);
        final BlockingQueue<Integer> output = new ArrayBlockingQueue<>(50);

        List<WorkerThread> workers = Arrays.asList(
                new WorkerThread("Worker 1", input, output),
                new WorkerThread("Worker 2", input, output),
                new WorkerThread("Worker 3", input, output),
                new WorkerThread("Worker 4", input, output)
        );

        workers.forEach(Thread::start);

        //This thread prints our results
        Thread printerThread = new Thread(() -> {
            int count = 0;
            System.out.println("Printer started");
            while(true) {
                int value = takeFrom(output);
                if(value < 0) {
                    System.out.println("Printer finished");
                    return;
                }
                System.out.println("Result " + ++count + ": " + value);
            }
        });

        printerThread.start();

        //Stuff 1000 integers between 1M and 2M into the input
        Random random = new Random();
        for(int i = 0;i < 1000;i++) {
            input.put(1000000 + random.nextInt(1000000));
        }

        //For each worker put an end message into the input
        workers.forEach(w -> putIn(input, -1));

        //Wait for the workers to finish
        workers.forEach(QueueExample::join);

        //Tell the printerthread we're done
        putIn(output, -1);

        //Wait for it to finish
        join(printerThread);

        System.out.println("Done!");
    }

    private static class WorkerThread extends Thread {
        private BlockingQueue<Integer> input;
        private BlockingQueue<Integer> output;

        public WorkerThread(String name, BlockingQueue<Integer> input, BlockingQueue<Integer> output) {
            super(name);
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " started");
            while(true) {
                int value = takeFrom(input);
                if(value < 0) { //End of queue message
                    System.out.println(Thread.currentThread().getName() + " finished");
                    return;
                }
                if(isPrime(value)) {
                    putIn(output, value);
                }
            }
        }

        //Inefficient primality check
        private boolean isPrime(int value) {
            if(value <= 1) {
                return false;
            }
            for(int div = 2;div <= Math.sqrt(value);div++) {
                if(value % div == 0) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * Few helper methods to wrap the checked exceptions in runtime exceptions.
     */

    private static int takeFrom(BlockingQueue<Integer> queue) {
        try {
            return queue.take();
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void putIn(BlockingQueue<Integer> queue, int value) {
        try {
            queue.put(value);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void join(Thread t) {
        try {
            t.join();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
