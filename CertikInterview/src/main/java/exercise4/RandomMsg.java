package exercise4;

public class RandomMsg {
    private long threadId;
    private String message;

    public RandomMsg(long threadId, String message) {
        this.threadId = threadId;
        this.message = message;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
