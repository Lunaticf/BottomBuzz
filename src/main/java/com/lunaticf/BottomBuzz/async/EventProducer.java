package com.lunaticf.BottomBuzz.async;


import com.alibaba.fastjson.JSON;
import com.lunaticf.BottomBuzz.controller.LikeController;
import com.lunaticf.BottomBuzz.utils.JedisAdapter;
import com.lunaticf.BottomBuzz.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 事件生产者
@Service
public class EventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);


    @Autowired
    JedisAdapter jedisAdapter;

    // 把事件加入Redis队列
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSON.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("发起事件失败" + e.getMessage());
            return false;
        }
    }
}
