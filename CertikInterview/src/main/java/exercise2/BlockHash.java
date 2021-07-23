package exercise2;

public class BlockHash {
    private Block block;
    private String hash;


    public BlockHash(Block block, String hash) {
        this.block = block;
        this.hash = hash;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
