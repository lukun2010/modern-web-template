package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Akka;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class MyWebSocketActor extends UntypedActor {

    public static Props props(ActorRef out, LookupBusImpl lookupBus) {
        return Props.create(MyWebSocketActor.class, out, lookupBus);
    }

    private final ActorRef out;
    private final LookupBusImpl lookupBus;
    private ActorRef outputActor = null;

    public MyWebSocketActor(ActorRef out, LookupBusImpl lookupBus) {
        this.out = out;
        this.lookupBus = lookupBus;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        // 创建输出Actor
        if (message instanceof JsonNode) {
            // 解析Json字符串
            MsgEnvelope msgEnvelope = new MsgEnvelope(((JsonNode) message).findPath("event").asText(),
                    ((JsonNode) message).findPath("data").asText());
            System.out.println("onReceive in MyWebScoketActor");
            if (msgEnvelope.event.equals("topic")) {
                if (outputActor != null) {
                    System.out.println("onReceive in MyWebScoketActor, kill old actor");
                    // 解除原来的订阅
                    lookupBus.unsubscribe(outputActor);
                    // 先杀掉原来的Actor
                    outputActor.tell(akka.actor.Kill.getInstance(), self());
                }
                System.out.println("onReceive in MyWebScoketActor, start new actor");
                // 启动新的Actor
                outputActor = Akka.system().actorOf(MyWebSocketOutputActor.props(out));
                // 进行新的订阅
                System.out.println("onReceive in MyWebScoketActor, subscribe by " + msgEnvelope.data);
                lookupBus.subscribe(outputActor, msgEnvelope.data.toString());
            } else {
                // 暂无其他动作
            }
        }
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
