package com.lunaticf.BottomBuzz.async.handler;

import com.lunaticf.BottomBuzz.async.EventModel;
import com.lunaticf.BottomBuzz.async.EventType;
import com.lunaticf.BottomBuzz.model.Message;
import com.lunaticf.BottomBuzz.service.MessageService;
import com.lunaticf.BottomBuzz.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RegHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setToId(eventModel.getActorId());
        message.setContent("祝贺您成功注册 欢迎来到ButtomBuzz");

        // SYSTEM ACCOUNT
        message.setFromId(1);
        message.setCreatedDate(new Date());
        message.setHasRead(0);

        int fromId = 1;
        int toId = eventModel.getActorId();
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<>();
        map.put("username", eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("to"), "祝贺你成功注册",
                "/mail/login.ftl", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.REGISTER);
    }
}
