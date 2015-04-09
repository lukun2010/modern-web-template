package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class MyWebSocketOutputActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketOutputActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketOutputActor(ActorRef out) {
        this.out = out;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("onReceive in MyWebSocketOutputActor");
        if (message instanceof MsgEnvelope) {
            Map<String, String> innerMap = new HashMap<String, String>();
            innerMap.put("event", "message");
            innerMap.put("data", ((MsgEnvelope) message).data);
            System.out.println(innerMap);
            out.tell(Json.toJson(innerMap), self());
        }
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
