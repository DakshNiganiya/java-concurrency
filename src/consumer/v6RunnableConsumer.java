package consumer;

import manager.Message;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class v6RunnableConsumer implements Runnable {
    private final BlockingQueue<Message> queue;
    private boolean shouldRun = true;


    private final static ConcurrentHashMap<Message, LongAdder> consumedMessages = new ConcurrentHashMap<>();

    public v6RunnableConsumer(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    public void finishConsuming() {
        shouldRun = false;
    }

    @Override
    public void run() {
        try {
            while (shouldRun||!queue.isEmpty()) {
                Message message = queue.take();
                consumedMessages.computeIfAbsent(message, k -> new LongAdder()).increment();
        }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public static ConcurrentHashMap<Message, LongAdder> getConsumedMessages() {
        return  consumedMessages;
    }

}
