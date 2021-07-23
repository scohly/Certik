package exercise5;

import exercise2.Block;

import java.util.List;

public interface BlockListener {
    void messageReceived(List<BlockChain> chain);

    //added as a test
    void booleanReceived(String message);
}
