package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.ConsumerTimeoutException;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import play.libs.Akka;
import scala.concurrent.duration.Duration;


/**
 * Created by lu.kun on 2015/4/9.
 */
public class KafkaSourceActor extends UntypedActor {

    private static final Logger log = LoggerFactory.getLogger(KafkaSourceActor.class);

    public static Props props(KafkaSourceContext context, LookupBusImpl lookupBus) {
        return Props.create(KafkaSourceActor.class, context, lookupBus);
    }

    private ConsumerConnector consumer;
    private ConsumerIterator<byte[],byte[]> it;
    private String topic;
    private boolean kafkaAutoCommitEnabled;
    private KafkaSourceContext context;
    private Properties kafkaProps;

    private final LookupBusImpl lookupBus;

    public KafkaSourceActor(KafkaSourceContext context, LookupBusImpl lookupBus) {
        this.context = context;
        this.lookupBus = lookupBus;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        // No-op
        if (message instanceof String) {
            if (message.equals("process")) {
                process();
            } else if (message.equals("start")) {
                start();
            } else if (message.equals("stop")) {
                stop();
            } else if (message.equals("configure")) {
                configure();
            } else {
                System.out.println("Unknown message");
            }
        }
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    public void process() {

        byte[] kafkaMessage;
        byte[] kafkaKey;
        Map<String, String> headers;
        try {
            boolean iterStatus = false;
            long startTime = System.nanoTime();
            System.out.println("topic = " + topic);
            System.out.println("process before loop");
            while (hasNext()) {
                System.out.println("process in loop");
                // get next message
                MessageAndMetadata<byte[], byte[]> messageAndMetadata = it.next();
                kafkaMessage = messageAndMetadata.message();
                // kafkaKey = messageAndMetadata.key();
                System.out.println("got message from kafka: " + new String(kafkaMessage));
                MsgEnvelope msgEnvelope = new MsgEnvelope("data1" , new String(kafkaMessage));
                lookupBus.publish(msgEnvelope);
            }
            System.out.println("process after loop");
            long endTime = System.nanoTime();
            if (!kafkaAutoCommitEnabled) {
                // commit the read transactions to Kafka to avoid duplicates
                consumer.commitOffsets();
            }
            Akka.system().scheduler().scheduleOnce(Duration.create(1, TimeUnit.SECONDS),
                    self(),
                    "process",
                    Akka.system().dispatcher(),
                    ActorRef.noSender());
        } catch (Exception e) {
            log.error("KafkaSourceActor EXCEPTION, {}", e);
        }
    }

    public void configure() throws ConfigurationException {
        topic = context.getString(KafkaSourceConstants.TOPIC);

        if(topic == null) {
            throw new ConfigurationException("Kafka topic must be specified.");
        }

        kafkaProps = KafkaSourceUtil.getKafkaProperties(context);
        kafkaAutoCommitEnabled = Boolean.parseBoolean(kafkaProps.getProperty(
                KafkaSourceConstants.AUTO_COMMIT_ENABLED));

    }

    public synchronized void start() throws KafkaSourceException {
        log.info("Starting {}...", this);

        try {
            //initialize a consumer. This creates the connection to ZooKeeper
            consumer = KafkaSourceUtil.getConsumer(kafkaProps);
        } catch (Exception e) {
            throw new KafkaSourceException("Unable to create consumer. " +
                    "Check whether the ZooKeeper server is up and that the " +
                    "Flume agent can connect to it.");
        }

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        // We always have just one topic being read by one thread
        topicCountMap.put(topic, 1);

        // Get the message iterator for our topic
        // Note that this succeeds even if the topic doesn't exist
        // in that case we simply get no messages for the topic
        // Also note that currently we only support a single topic
        try {
            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
                    consumer.createMessageStreams(topicCountMap);
            List<KafkaStream<byte[], byte[]>> topicList = consumerMap.get(topic);
            KafkaStream<byte[], byte[]> stream = topicList.get(0);
            it = stream.iterator();
        } catch (Exception e) {
            throw new KafkaSourceException("Unable to get message iterator from Kafka");
        }
        log.info("Kafka source started.");
    }

    public synchronized void stop() {
        if (consumer != null) {
            // exit cleanly. This syncs offsets of messages read to ZooKeeper
            // to avoid reading the same messages again
            consumer.shutdown();
        }
        log.info("Kafka Source stopped.");
    }

    boolean hasNext() {
        try {
            it.hasNext();
            return true;
        } catch (ConsumerTimeoutException e) {
            return false;
        }
    }
}
