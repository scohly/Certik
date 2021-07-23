package redundancies;

public class MsgSimulator {
    public static void main(String[] args){
        Message msg = new Message();
        /*
                java.lang.ThreadLocal<String> threadLocal = new java.lang.ThreadLocal<>();

        Thread thread1 = new Thread( () -> {
            threadLocal.set("Thread 0: [0] --time: " + LocalDateTime.now().toString());
            try{
                //2s sleep
                Thread.sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            String val = threadLocal.get();
            System.out.println(val);
        });
         */


//        java.lang.ThreadLocal<String> threadLocal = new java.lang.ThreadLocal<>();
        Thread thread0 = new Thread(new MsgSender(msg));
        Thread thread1 = new Thread(new Receiver(msg));
        Thread thread2 = new Thread(new Receiver(msg));
        Thread thread3 = new Thread(new Receiver(msg));

        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();

//        thread0.start();

    }
}
