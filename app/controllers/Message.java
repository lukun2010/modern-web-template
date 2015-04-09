package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.WebSocket;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lu.kun on 2015/4/8.
 */
public class Message extends Controller {

    public static WebSocket<JsonNode> socket() {



        return new WebSocket<JsonNode>() {
            @Override
            public void onReady(In<JsonNode> in, final Out<JsonNode> out) {

                in.onMessage(new F.Callback<JsonNode>() {
                    @Override
                    public void invoke(JsonNode jsonNode) throws Throwable {
                        System.out.println(jsonNode.toString());
                    }
                });

                Akka.system().scheduler().schedule(
                        Duration.create(0, TimeUnit.SECONDS),
                        Duration.create(1, TimeUnit.SECONDS),
                        new Runnable() {
                            private int cnt = 0;
                            @Override
                            public void run() {
                                Map<String, String> innerMap = new HashMap<String, String>();
                                innerMap.put("event", "message");
                                if (cnt % 10 == 0) {
                                    innerMap.put("data", "Hello!");
                                } else {
                                    innerMap.put("data", "Bye bye!");
                                }
                                cnt++;
                                try {
                                    out.write(Json.toJson(innerMap));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        Akka.system().dispatcher()
                );

            }
        };
    }

}
