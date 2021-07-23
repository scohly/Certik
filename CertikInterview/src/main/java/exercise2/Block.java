package exercise2;

public class Block {
    private String nonce;
    private Integer miner;

    public Block() {

    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Integer getMiner() {
        return miner;
    }

    public void setMiner(Integer miner) {
        this.miner = miner;
    }

    public Block(Integer miner) {
        this.miner = miner;
    }

//    public String toString(int n){
//        return String.valueOf(n);
//    }

    @Override
    public String toString() {
        return"" + miner;
    }
}


