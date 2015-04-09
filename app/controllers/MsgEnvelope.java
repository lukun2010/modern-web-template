package controllers;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class MsgEnvelope {
    public final String event;
    public final String data;

    public MsgEnvelope(String event, String data) {
        this.event = event;
        this.data = data;
    }
}
