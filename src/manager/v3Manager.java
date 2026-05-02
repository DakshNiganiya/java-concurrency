package manager;

import consumer.v3RunnableConsumer;
import producer.v3RunnableProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class v3Manager {
    static void main() throws InterruptedException {
        ArrayList<Message> queue = new ArrayList<>();
        List<v3RunnableConsumer> v3RunnableConsumers =  new ArrayList<>();
        List<v3RunnableProducer> v3RunnableProducers =  new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            v3RunnableProducer producer = new v3RunnableProducer(queue,"Producers",100);
            v3RunnableProducers.add(producer);
        }
        for (int i = 0; i < 2; i++) {
            v3RunnableConsumer consumer = new v3RunnableConsumer(queue);
            v3RunnableConsumers.add(consumer);
        }
        for (v3RunnableConsumer consumer : v3RunnableConsumers) {
            new Thread(consumer).start();
        }
        for (v3RunnableProducer producer : v3RunnableProducers) {
            new Thread(producer).start();
        }
        Thread.sleep(100);
        for (v3RunnableProducer producer : v3RunnableProducers) {
            producer.finishProducing();
        }
        for (v3RunnableConsumer consumer : v3RunnableConsumers) {
            consumer.finishConsuming();
        }
        Map<Message, LongAdder> consumedMessages = v3RunnableConsumer.getConsumedMessages();
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
        //Da die kritische Abschnitte synchronisiert sind, ist die queue size nie negative. Aufgrund von der "Forced/Unnecessary"
        //synchronisierung und der mechanismus, die Anzahl der Processed messages ziemlich klein. Zusätzlich ist die Queue nicht leer.
    }
}
