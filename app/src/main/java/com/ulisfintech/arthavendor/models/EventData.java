package com.ulisfintech.arthavendor.models;

public class EventData {
    private int type = 0;
    private Object object = null;

    public EventData() {
    }

    public EventData(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
