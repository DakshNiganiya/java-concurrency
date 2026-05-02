package producer;

import manager.Message;

import java.util.concurrent.BlockingQueue;

public class v6RunnableProducer implements Runnable {
    private final BlockingQueue<Message> queue;
    private final String name;
    private boolean shouldRun = true;
    private final int capacity;
    private int counter = 0;

    public v6RunnableProducer(BlockingQueue<Message> queue, String name, int capacity) {
        this.queue = queue;
        this.name = name;
        this.capacity = capacity;
    }

    public void finishProducing() {
        shouldRun = false;
    }

    @Override
    public void run() {
        try {
            while (shouldRun) {
                Message message = new Message(counter++,"Hello from Producer " + name);
                queue.put(message);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
