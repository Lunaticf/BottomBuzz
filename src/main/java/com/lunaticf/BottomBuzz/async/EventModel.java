package com.lunaticf.BottomBuzz.async;

import com.lunaticf.BottomBuzz.model.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EventModel {

    private EventType eventType;    // 事件类型
    private EntityType entityType;  // 事件作用的对象类型
    private int entityId;           // 事件作用的对象的Id

    public Map<String, String> getExts() {
        return exts;
    }

    private int entityOwnerId;      // 事件作用的对象所有者的Id;

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    private int actorId;            // 事件发起者Id;


    // 一些扩展属性
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {

    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }
}
