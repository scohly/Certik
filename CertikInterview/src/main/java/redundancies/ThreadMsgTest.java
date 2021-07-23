package redundancies;

import exercise4.ConcurrentArrayList;
import exercise4.RandomMsg;

public class ThreadMsgTest {

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
