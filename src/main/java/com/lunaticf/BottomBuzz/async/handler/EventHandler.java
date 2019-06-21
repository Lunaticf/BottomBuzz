package com.lunaticf.BottomBuzz.async.handler;

import com.lunaticf.BottomBuzz.async.EventModel;
import com.lunaticf.BottomBuzz.async.EventType;

import java.util.List;

public interface EventHandler {
    void doHandle(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}
