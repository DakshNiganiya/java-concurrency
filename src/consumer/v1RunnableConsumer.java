package consumer;

import manager.Message;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class v1RunnableConsumer implements Runnable {
    private final ArrayList<Message> queue;
    private boolean shouldRun = true;

    // Use this (actually working) synchronized data structure for your analysis later on :)
    private final static ConcurrentHashMap<Message, LongAdder> consumedMessages = new ConcurrentHashMap<>();

    public v1RunnableConsumer(ArrayList<Message> queue) {
        this.queue = queue;
    }

    public void finishConsuming() {
        shouldRun = false;
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                Message e = queue.removeFirst();
                // you do not understand how to write this line, but maybe you understand how to read this line.
                // It essentially tries to read a key from a Map, and if this key does not exist it (atomically) sets it to something.
                // LongAdder is a Thread-Safe counter
                // This code is from the Documentation of the ConcurrentHashMap
                v1RunnableConsumer.consumedMessages.computeIfAbsent(e, m -> new LongAdder()).increment();
                Thread.sleep(2);
            } catch (InterruptedException e) {
                break;
            } catch (Exception ignored) {
                continue;
            }
        }
    }

    public static ConcurrentHashMap<Message, LongAdder> getConsumedMessages() {
        return  consumedMessages;
    }

}
