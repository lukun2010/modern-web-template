package controllers;

import akka.actor.ActorRef;
import akka.event.japi.LookupEventBus;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class LookupBusImpl extends LookupEventBus<MsgEnvelope, ActorRef, String> {
    @Override
    public int mapSize() {
        return 128;
    }

    @Override
    public int compareSubscribers(ActorRef a, ActorRef b) {
        return a.compareTo(b);
    }

    @Override
    public String classify(MsgEnvelope event) {
        return event.event;
    }

    @Override
    public void publish(MsgEnvelope event, ActorRef subscriber) {
        subscriber.tell(event, ActorRef.noSender());
    }
}
