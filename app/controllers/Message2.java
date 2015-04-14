package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Akka;
import play.libs.F;
import play.mvc.WebSocket;
import play.mvc.Controller;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class Message2  extends Controller {

    private static final Logger log =
            LoggerFactory.getLogger(Message2.class);

    private static LookupBusImpl lookupBus = new LookupBusImpl();

    private static ActorRef kafkaActor;

    static {
        // 设置Context
        KafkaSourceContext context = new KafkaSourceContext();
        context.put(KafkaSourceConstants.ZOOKEEPER_CONNECT_FLUME, "datanode1.hadoop.bi.bestv.com.cn:2181,datanode2.hadoop.bi.bestv.com.cn:2181,datanode3.hadoop.bi.bestv.com.cn:2181,namenode.hadoop.bi.bestv.com.cn:2181,secondarynamenode.hadoop.bi.bestv.com.cn:2181");
        context.put(KafkaSourceConstants.GROUP_ID_FLUME, "00000000");
        context.put(KafkaSourceConstants.TOPIC, "OTT-TPLAY");
        // Create Actor
        kafkaActor = Akka.system().actorOf(KafkaSourceActor.props(context, lookupBus));
        // Configure
        kafkaActor.tell("configure", ActorRef.noSender());
        // Start
        kafkaActor.tell("start", ActorRef.noSender());
        // Process

        Akka.system().scheduler().scheduleOnce(Duration.create(1, TimeUnit.SECONDS),
                kafkaActor,
                "process",
                Akka.system().dispatcher(),
                ActorRef.noSender());

        System.out.println("kafkaActor process");
    }

    public static WebSocket<JsonNode> socket() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            @Override
            public Props apply(ActorRef out) throws Throwable {
                return MyWebSocketActor.props(out, lookupBus);
            }
        });
    }
}
