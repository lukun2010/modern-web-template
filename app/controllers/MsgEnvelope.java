package controllers;

/**
 * Created by lu.kun on 2015/4/9.
 */
public class MsgEnvelope {
    public final String event;
    public final Object data;

    public MsgEnvelope(String event, Object data) {
        this.event = event;
        this.data = data;
    }
}
