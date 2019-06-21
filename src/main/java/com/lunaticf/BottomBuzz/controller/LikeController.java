package com.lunaticf.BottomBuzz.controller;

import com.lunaticf.BottomBuzz.model.EntityType;
import com.lunaticf.BottomBuzz.model.HostHolder;
import com.lunaticf.BottomBuzz.service.LikeService;
import com.lunaticf.BottomBuzz.service.NewsService;
import com.lunaticf.BottomBuzz.utils.HelpUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);


    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @RequestMapping(path = "/like", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId") int newsId) {

        try {
            long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);

            // 更新点赞数
            newsService.updateLikeCount(newsId, (int) likeCount);
            return HelpUtils.getJSONString(0, String.valueOf(likeCount));
        } catch (Exception e) {
            long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
            logger.error("点赞异常" + e.getMessage());
            return HelpUtils.getJSONString(0, String.valueOf(likeCount));
        }

    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, newsId);
        // 更新喜欢数
        newsService.updateLikeCount(newsId, (int) likeCount);
        return HelpUtils.getJSONString(0, String.valueOf(likeCount));
    }

}
