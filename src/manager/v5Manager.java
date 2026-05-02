package manager;

import consumer.v4RunnableConsumer;
import producer.v4RunnableProducer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

public class v5Manager {
    static void main() throws InterruptedException {
        ArrayList<Message> queue = new ArrayList<>();
        List<v4RunnableConsumer> consumers = new ArrayList<>();
        List<v4RunnableProducer> producers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            producers.add(new v4RunnableProducer(queue,"Producers",100));
        }
        for (int i = 0; i < 2; i++) {
            consumers.add(new v4RunnableConsumer(queue));
        }
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (v4RunnableConsumer consumer : consumers) {
            executor.submit(consumer);
        }
        for (v4RunnableProducer producer : producers) {
            executor.submit(producer);
        }
        Thread.sleep(100);
        for (v4RunnableProducer producer : producers) {
            producer.finishProducing();
        }
        for (v4RunnableConsumer consumer : consumers) {
            consumer.finishConsuming();
        }
        executor.shutdown();
        Map<Message, LongAdder> consumedMessages = v4RunnableConsumer.getConsumedMessages();
        int processed = 0;
        int duplicate = 0;
        for (Map.Entry<Message, LongAdder> entry : consumedMessages.entrySet()) {
            int count = entry.getValue().intValue();
            if (count>=1){
                processed++;
            }
            if (count>1){
                duplicate++;
            }
        }
        int remaining = queue.size();
        System.out.println("Messages processed:"+ processed );
        System.out.println("Duplicates processed:"+duplicate);
        System.out.println("Remaining consumed messages:"+remaining);
        // Der throughput in diesem Fall ist weniger als v4Manager aufgrund (wahrscheinlich) die thread pool verwaltung.
    }
}
