package manager;

import consumer.v1RunnableConsumer;
import producer.v1RunnableProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class v1Manager {




    static void main() throws InterruptedException {
         ArrayList<Message> queue = new ArrayList<>();
         List<v1RunnableConsumer> v1RunnableConsumers =  new ArrayList<>();
         List<v1RunnableProducer> v1RunnableProducers =  new ArrayList<>();
         for (int i = 0; i < 2; i++) {
             v1RunnableProducer producer = new v1RunnableProducer(queue,"Producers",300);
             v1RunnableProducers.add(producer);
         }
         for (int i = 0; i < 2; i++) {
             v1RunnableConsumer consumer = new v1RunnableConsumer(queue);
             v1RunnableConsumers.add(consumer);
         }
         for (v1RunnableConsumer consumer : v1RunnableConsumers) {
             new Thread(consumer).start();
         }
         for (v1RunnableProducer producer : v1RunnableProducers) {
             new Thread(producer).start();
         }
         Thread.sleep(1000);
         for (v1RunnableProducer producer : v1RunnableProducers) {
             producer.finishProducing();
         }
         for (v1RunnableConsumer consumer : v1RunnableConsumers) {
             consumer.finishConsuming();
         }
         Map<Message, LongAdder> consumedMessages = v1RunnableConsumer.getConsumedMessages();
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
        //ArrayList ist nicht Thread safe, deswegen ergibt queue.size() negative werte obwohl queue nicht negativ sein kann.

    }
}
