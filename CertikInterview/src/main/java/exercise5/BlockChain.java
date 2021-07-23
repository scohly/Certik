package exercise5;

public class BlockChain {
    private String nonce;
    private Integer miner;

    public BlockChain(int miner) {
        this.miner = miner;
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

    public BlockChain(Integer miner) {
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


