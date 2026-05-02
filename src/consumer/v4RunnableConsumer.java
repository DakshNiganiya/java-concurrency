package consumer;

import manager.Message;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class v4RunnableConsumer implements Runnable {
    private final ArrayList<Message> queue;
    private boolean shouldRun = true;


    private final static ConcurrentHashMap<Message, LongAdder> consumedMessages = new ConcurrentHashMap<>();

    public v4RunnableConsumer(ArrayList<Message> queue) {
        this.queue = queue;
    }

    public void finishConsuming() {
        shouldRun = false;
    }

    @Override
    public void run() {
        while (shouldRun||!queue.isEmpty()) {
            Message message;
            synchronized (queue) {
                while (queue.isEmpty()) {
                    if (!shouldRun) {
                        return;
                    }
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                message = queue.removeFirst();
                queue.notifyAll();
            }
            v4RunnableConsumer.getConsumedMessages().computeIfAbsent(message, k -> new LongAdder()).increment();



        }

    }
    public static ConcurrentHashMap<Message, LongAdder> getConsumedMessages() {
        return  consumedMessages;
    }}
