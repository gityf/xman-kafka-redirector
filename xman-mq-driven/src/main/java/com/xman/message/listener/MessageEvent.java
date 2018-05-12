package com.xman.message.listener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yangxiang@
 * @Date 2015/11/29
 * @Time 14:45
 */
public class MessageEvent {

    private boolean active;
    private String topic;
    private final Map<String, Object> attributes;

    public MessageEvent() {
        this.active = true;
        this.attributes = new LinkedHashMap();
    }

    public MessageEvent(Map<String, Object> attributes) {
        this.active = true;
        this.attributes = attributes;
    }

    public void setAttribute(String name, Object value) {
        this.checkActive();
        if (name == null || "".equals(name)) {
            return;
        }
        if (value != null) {
            this.attributes.put(name, value);
        } else {
            this.attributes.remove(name);
        }
    }

    public Object getAttribute(String name) {
        this.checkActive();
        return this.attributes.get(name);
    }

    public Object getAllAtrributes() {
        this.checkActive();
        return this.attributes;
    }

    protected void checkActive() throws IllegalStateException {
        if (!this.active) {
            throw new IllegalStateException("Request is not active anymore");
        }
    }

    public void clearAttributes() {
        this.attributes.clear();
    }

    public void close() {
        this.active = false;
    }

    public void invalidate() {
        this.close();
        this.clearAttributes();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "active=" + active +
                ", topic='" + topic + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
