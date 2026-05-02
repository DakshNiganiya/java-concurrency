package consumer;

import manager.Message;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class v3RunnableConsumer implements Runnable {
    private final ArrayList<Message> queue;
    private boolean shouldRun = true;


    private final static ConcurrentHashMap<Message, LongAdder> consumedMessages = new ConcurrentHashMap<>();

    public v3RunnableConsumer(ArrayList<Message> queue) {
        this.queue = queue;
    }

    public void finishConsuming() {
        shouldRun = false;
    }

    @Override
    public void run() {
        while (shouldRun) {
            Message message = null;
            synchronized (queue) {
                if (!queue.isEmpty()) {
                    message = queue.removeFirst();
                }
            }
            if (message != null) {
                v3RunnableConsumer.consumedMessages.computeIfAbsent(message, m -> new LongAdder()).increment();
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                break;
            }
        }

}
    public static ConcurrentHashMap<Message, LongAdder> getConsumedMessages() {
        return  consumedMessages;
    }
}
