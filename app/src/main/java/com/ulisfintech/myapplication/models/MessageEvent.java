package com.ulisfintech.myapplication.models;

public class MessageEvent {

    private Class eventFor;
    private EventData eventData;
    private Object data;

    public MessageEvent() {
    }

    public MessageEvent(Class eventFor, EventData eventData) {
        this.eventFor = eventFor;
        this.eventData = eventData;
    }

    public MessageEvent(Class eventFor, Object data) {
        this.eventFor = eventFor;
        this.data = data;
    }

    public MessageEvent sendData(Object data){
        this.data = data;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Class getEventFor(){
        return eventFor;
    }

    public MessageEvent setEventFor(Class cls){
        this.eventFor = cls;
        return this;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }
}
