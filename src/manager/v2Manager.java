package manager;

import consumer.v2RunnableConsumer;
import producer.v2RunnableProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class v2Manager {
    static void main() throws InterruptedException {
        ArrayList<Message> queue = new ArrayList<>();
        List<v2RunnableConsumer> v2RunnableConsumers =  new ArrayList<>();
        List<v2RunnableProducer> v2RunnableProducers =  new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            v2RunnableProducer producer = new v2RunnableProducer(queue,"Producers",100);
            v2RunnableProducers.add(producer);
        }
        for (int i = 0; i < 2; i++) {
            v2RunnableConsumer consumer = new v2RunnableConsumer(queue);
            v2RunnableConsumers.add(consumer);
        }
        for (v2RunnableConsumer consumer : v2RunnableConsumers) {
            new Thread(consumer).start();
        }
        for (v2RunnableProducer producer : v2RunnableProducers) {
            new Thread(producer).start();
        }
        Thread.sleep(100);
        for (v2RunnableProducer producer : v2RunnableProducers) {
            producer.finishProducing();
        }
        for (v2RunnableConsumer consumer : v2RunnableConsumers) {
            consumer.finishConsuming();
        }
        Map<Message, LongAdder> consumedMessages = v2RunnableConsumer.getConsumedMessages();
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
        //Wenn der Manager-Thread `consumer.finishedConsuming()` aufruft, kann er die Lock nicht erhalten, da `finishConsuming()`
        // ebenfalls synchronisiert ist und sich der Consumer-Thread noch innerhalb des Blocks befindet. Dies bedeutet, dass
        // `shouldRun` nie auf `false` gesetzt wird, Consumer-Threads den Block nicht verlassen und das Programm weiterläuft,
        // was zu einem Deadlock oder Lock Starvation Situation führen kann.

    }
}
