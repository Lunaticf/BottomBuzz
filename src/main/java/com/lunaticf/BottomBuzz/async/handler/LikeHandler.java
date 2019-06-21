package com.lunaticf.BottomBuzz.async.handler;

import com.lunaticf.BottomBuzz.async.EventModel;
import com.lunaticf.BottomBuzz.async.EventType;
import com.lunaticf.BottomBuzz.model.Message;
import com.lunaticf.BottomBuzz.model.User;
import com.lunaticf.BottomBuzz.service.MessageService;
import com.lunaticf.BottomBuzz.service.UserService;
import com.lunaticf.BottomBuzz.utils.HelpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel eventModel) {

        // 给被点赞的用户发一个站内信
        Message message = new Message();
        message.setCreatedDate(new Date());
        int fromId = eventModel.getActorId();
        int toId = eventModel.getEntityOwnerId();
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        message.setToId(toId);
        message.setFromId(fromId);

        User user = userService.getUser(fromId);
        message.setContent("用户" + user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(eventModel.getEntityId()));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
