package producer;

import manager.Message;

import java.util.ArrayList;

public class v1RunnableProducer implements Runnable {

    private final ArrayList<Message> queue;
    private final String name;
    private boolean shouldRun = true;
    private final int capacity;
    private int counter = 0;

    public v1RunnableProducer(ArrayList<Message> queue, String name, int capacity) {
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
            if (queue.size() < capacity) {
                try {
                    queue.add(new Message(counter++, "Hello from Producer " + name  + "!"));
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    break;
                } catch (Exception ignored) {
                    continue;
                }
            }
        }
    }
}
