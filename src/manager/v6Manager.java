package manager;

import consumer.v6RunnableConsumer;
import producer.v6RunnableProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class v6Manager {
    static void main() throws InterruptedException {
        BlockingQueue<Message> queue = new ArrayBlockingQueue<>(100);
        List<v6RunnableConsumer> consumers = new ArrayList<>();
        List<v6RunnableProducer> producers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            producers.add(new v6RunnableProducer(queue,"Producers",100));
        }
        for (int i = 0; i < 2; i++) {
            consumers.add(new v6RunnableConsumer(queue));
        }
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (v6RunnableConsumer consumer : consumers) {
            executor.submit(consumer);
        }
        for (v6RunnableProducer producer : producers) {
            executor.submit(producer);
        }
        Thread.sleep(100);
        for (v6RunnableProducer producer : producers) {
            producer.finishProducing();
        }
        for (v6RunnableConsumer consumer : consumers) {
            consumer.finishConsuming();
        }
        executor.shutdown();
        Map<Message, LongAdder> consumedMessages = v6RunnableConsumer.getConsumedMessages();
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
        //Blockingqueue blockiert die Producers, wenn es voll ist und consumers, wenn es leer ist. Es gibt keine manuelle Logik für
        //wait() und notify() und die Library Methods put() und take() wurden genutzt. Concurrency hier wurde von 
    }
}
