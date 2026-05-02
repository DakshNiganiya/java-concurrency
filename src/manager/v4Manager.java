package manager;

import consumer.v4RunnableConsumer;
import producer.v4RunnableProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class v4Manager {
    static void main() throws InterruptedException {
        ArrayList<Message> queue = new ArrayList<>();
        List<v4RunnableConsumer> v4RunnableConsumers =  new ArrayList<>();
        List<v4RunnableProducer> v4RunnableProducers =  new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            v4RunnableProducer producer = new v4RunnableProducer(queue,"Producers",100);
            v4RunnableProducers.add(producer);
        }
        for (int i = 0; i < 30; i++) {
            v4RunnableConsumer consumer = new v4RunnableConsumer(queue);
            v4RunnableConsumers.add(consumer);
        }
        for (v4RunnableConsumer consumer : v4RunnableConsumers) {
            new Thread(consumer).start();
        }
        for (v4RunnableProducer producer : v4RunnableProducers) {
            new Thread(producer).start();
        }
        Thread.sleep(1000);
        for (v4RunnableProducer producer : v4RunnableProducers) {
            producer.finishProducing();
        }
        for (v4RunnableConsumer consumer : v4RunnableConsumers) {
            consumer.finishConsuming();
        }
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
        //Consumer Threads blockieren mittels wait() wenn die Queue leer ist und aufwachen nur wenn Messages hizugefügt werden
        //und dann rufen notifyAll(). Folglich entsteht kein busy waiting. Die Queue ist auch komplett leer bevor es terminiert
        // denn die Remaining consumed messages sind 0. Hier sind anzahl producers = 10 und consumers = 30
    }
    }

