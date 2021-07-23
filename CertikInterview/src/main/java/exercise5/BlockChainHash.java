package exercise5;

public class BlockChainHash {
    private BlockChain blockChain;
    private String hash;


    public BlockChainHash(BlockChain blockChain, String hash) {
        this.blockChain = blockChain;
        this.hash = hash;
    }

    public BlockChain getBlock() {
        return blockChain;
    }

    public void setBlock(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
