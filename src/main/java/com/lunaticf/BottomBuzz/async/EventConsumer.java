package com.lunaticf.BottomBuzz.async;

import com.alibaba.fastjson.JSON;
import com.lunaticf.BottomBuzz.async.handler.EventHandler;
import com.lunaticf.BottomBuzz.utils.JedisAdapter;
import com.lunaticf.BottomBuzz.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 消费事件的类
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    // type->handler映射 如何根据type找到相应的handler
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取所有handler
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
            // 先拿到每个handler支持的事件
            List<EventType> supportTypes = entry.getValue().getSupportEventTypes();
            for (EventType eventType : supportTypes) {
                if (!config.containsKey(eventType)) {
                    config.put(eventType, new ArrayList<>());
                }
                config.get(eventType).add(entry.getValue());
            }
        }

        // 启动线程去消费事件
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 不停的从队列中拿事件
                while (true) {
                    String queueKey = RedisKeyUtil.getEventQueueKey();
                    List<String> messages = jedisAdapter.brpop(0, queueKey);

                    for (String message : messages) {
                            if (message.equals(queueKey)) {
                                continue;
                            }

                            // 将拿到的事件解析
                            EventModel eventModel = JSON.parseObject(message, EventModel.class);


                            // 如果没找到handler
                            if (!config.containsKey(eventModel.getEventType())) {
                                logger.error("不能识别的事件");
                                continue;
                            }


                            // handler进行处理
                            for (EventHandler handler : config.get(eventModel.getEventType())) {
                                handler.doHandle(eventModel);
                            }
                        }

                }
            }
        });
        thread.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
