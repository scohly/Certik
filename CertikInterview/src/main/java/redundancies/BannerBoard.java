package redundancies;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class BannerBoard {

    ConcurrentHashMap<Long, ConcurrentLinkedQueue<String>> messages = new ConcurrentHashMap<>();

    public void introduceThreads (Thread[] threads){
        for(Thread thread : threads){
            messages.put(thread.getId(), new ConcurrentLinkedQueue<>());
        }
    }

    public void sendMessage(String msg){
        long id = Thread.currentThread().getId();
        if(messages.containsKey(id)){
            messages.forEach((i,list)->{
                // we do not want the same thread sending itself messages...hence this part
                if(!i.equals(id)){
                    list.add(msg);
                }
            });
        }
    }

    public void consumeIfAnyMessage(Consumer<String> reader){
        Long id = Thread.currentThread().getId();
        if(messages.containsKey(id)){
            messages.get(id).stream().filter(Objects::nonNull).forEach(reader);
        }
    }

    public void readIfAny(){
        Long id = Thread.currentThread().getId();
        if(messages.containsKey(id)){
//            String[] msgs = (String[]) messages.get(id).toArray();
            Object[] arr = messages.get(id).toArray();
            String threadName = Thread.currentThread().getName();
            int threadNum = Character.getNumericValue(threadName.charAt(threadName.length()-1));



            for(Object array : arr){
                System.out.println("Thread-"+threadNum+" received message "+ arr);
            }

        }
    }

    public static String generateRandomAlpha(){
        int charCount = 10;
        String alphaNum = RandomStringUtils.randomAlphanumeric(charCount);
        return alphaNum;
    }

    public static void main(String[] args){
        BannerBoard bb = new BannerBoard();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
//                while (true){
                String msg = generateRandomAlpha();
                String threadName = Thread.currentThread().getName();
                int threadId = Character.getNumericValue(threadName.charAt(threadName.length()-1));
                System.out.println("Thread-"+threadId + " sending message "+ msg+ " to all other threads.");
                    bb.sendMessage(msg);
                    System.out.println("Thread-"+threadId+" received message ");
                    bb.consumeIfAnyMessage(System.out::println);
//                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
//                while(true){
                String msg = generateRandomAlpha();
                String threadName = Thread.currentThread().getName();
                int threadId = Character.getNumericValue(threadName.charAt(threadName.length()-1));
                System.out.println("Thread-"+threadId + " sending message "+ msg+ " to all other threads.");
                bb.sendMessage(msg);
                System.out.println("Thread-"+threadId+" received message ");
                bb.consumeIfAnyMessage(System.out::println);
                bb.readIfAny();
//                }
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
//                while(true){
                String msg = generateRandomAlpha();
                String threadName = Thread.currentThread().getName();
                int threadId = Character.getNumericValue(threadName.charAt(threadName.length()-1));
                System.out.println("Thread-"+threadId + " sending message "+ msg+ " to all other threads.");
                bb.sendMessage(msg);
                System.out.println("Thread-"+threadId+" received message ");
                bb.consumeIfAnyMessage(System.out::println);
//                }
            }
        });

        Thread[] threads = {t1,t2, t3};
        bb.introduceThreads(threads);
        t1.start();
        t2.start();
        t3.start();
    }
}
