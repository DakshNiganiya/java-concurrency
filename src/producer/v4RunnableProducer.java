package producer;

import manager.Message;

import java.util.ArrayList;

public class v4RunnableProducer implements Runnable {
    private final ArrayList<Message> queue;
    private final String name;
    private boolean shouldRun = true;
    private final int capacity;
    private int counter = 0;

    public v4RunnableProducer(ArrayList<Message> queue, String name, int capacity) {
        this.queue = queue;
        this.name = name;
        this.capacity = capacity;
    }

    public void finishProducing() {
        shouldRun = false;
    }

    @Override
    public void run() {
        while (shouldRun) {
            synchronized (queue) {
                while (queue.size() >= capacity) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                queue.add(new Message(counter++, "Hello from Producer " + name  + "!"));
                queue.notifyAll();
            }
                    }
    }
}
