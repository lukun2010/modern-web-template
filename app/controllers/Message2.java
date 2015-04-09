package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.WebSocket;
import play.mvc.Controller;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class Message2  extends Controller {

    private static final Logger log =
            LoggerFactory.getLogger(Message2.class);

    private static LookupBusImpl lookupBus = new LookupBusImpl();

    public static WebSocket<JsonNode> socket() {

        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.SECONDS),
                Duration.create(1, TimeUnit.SECONDS),
                new Runnable() {
                    private int cnt = 1;
                    @Override
                    public void run() {
                        // 定时向总线发消息
                        MsgEnvelope msgEnvelope = new MsgEnvelope("data" + cnt , "Hello! " + cnt);
                        if (cnt % 10 == 0) {
                            cnt = 1;
                        } else{
                            cnt++;
                        }
                        lookupBus.publish(msgEnvelope);
                    }
                },
                Akka.system().dispatcher()
        );

        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            @Override
            public Props apply(ActorRef out) throws Throwable {
                return MyWebSocketActor.props(out, lookupBus);
            }
        });
    }
}
