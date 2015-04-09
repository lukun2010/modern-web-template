package controllers;

import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lu.kun on 2015/4/8.
 */
public class KafkaSourceUtil {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaSourceUtil.class);

    public static Properties getKafkaProperties(KafkaSourceContext context) throws ConfigurationException {
        log.info("context={}",context.toString());
        Properties props =  generateDefaultKafkaProps();
        setKafkaProps(context,props);
        addDocumentedKafkaProps(context,props);
        return props;
    }

    public static ConsumerConnector getConsumer(Properties kafkaProps) {
        ConsumerConfig consumerConfig =
                new ConsumerConfig(kafkaProps);
        ConsumerConnector consumer =
                Consumer.createJavaConsumerConnector(consumerConfig);
        return consumer;
    }

    /**
     * Generate consumer properties object with some defaults
     * @return
     */
    private static Properties generateDefaultKafkaProps() {
        Properties props = new Properties();
        props.put(KafkaSourceConstants.AUTO_COMMIT_ENABLED,
                KafkaSourceConstants.DEFAULT_AUTO_COMMIT);
        props.put(KafkaSourceConstants.CONSUMER_TIMEOUT,
                KafkaSourceConstants.DEFAULT_CONSUMER_TIMEOUT);
        props.put(KafkaSourceConstants.GROUP_ID,
                KafkaSourceConstants.DEFAULT_GROUP_ID);
        return props;
    }

    /**
     * Add all configuration parameters starting with "kafka"
     * to consumer properties
     */
    private static void setKafkaProps(KafkaSourceContext context,Properties kafkaProps) {

        Map<String,String> kafkaProperties =
                context.getSubProperties(KafkaSourceConstants.PROPERTY_PREFIX);

        for (Map.Entry<String,String> prop : kafkaProperties.entrySet()) {

            kafkaProps.put(prop.getKey(), prop.getValue());
            if (log.isDebugEnabled()) {
                log.debug("Reading a Kafka Producer Property: key: "
                        + prop.getKey() + ", value: " + prop.getValue());
            }
        }
    }

    /**
     * Some of the producer properties are especially important
     * We documented them and gave them a camel-case name to match Flume config
     * If user set these, we will override any existing parameters with these
     * settings.
     * Knowledge of which properties are documented is maintained here for now.
     * If this will become a maintenance issue we'll set a proper data structure.
     */
    private static void addDocumentedKafkaProps(KafkaSourceContext context,
                                                Properties kafkaProps)
            throws ConfigurationException {
        String zookeeperConnect = context.getString(
                KafkaSourceConstants.ZOOKEEPER_CONNECT_FLUME);
        if (zookeeperConnect == null) {
            throw new ConfigurationException("ZookeeperConnect must contain " +
                    "at least one ZooKeeper server");
        }
        kafkaProps.put(KafkaSourceConstants.ZOOKEEPER_CONNECT, zookeeperConnect);

        String groupID = context.getString(KafkaSourceConstants.GROUP_ID_FLUME);

        if (groupID != null ) {
            kafkaProps.put(KafkaSourceConstants.GROUP_ID, groupID);
        }
    }

}
