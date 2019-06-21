package com.lunaticf.BottomBuzz.controller;

import com.lunaticf.BottomBuzz.model.*;
import com.lunaticf.BottomBuzz.service.CommentService;
import com.lunaticf.BottomBuzz.service.LikeService;
import com.lunaticf.BottomBuzz.service.NewsService;
import com.lunaticf.BottomBuzz.service.UserService;
import com.lunaticf.BottomBuzz.utils.HelpUtils;
import org.springframework.ui.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    // 获取新闻详情页面
    @RequestMapping(value = "/news/{newsId}", method = RequestMethod.GET)
    public String getNewsDetail(Model model, @PathVariable("newsId") int newsId) {

        try {
            News news = newsService.getNewsById(newsId);

            if (news != null) {
                int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
                if (localUserId != 0) {
                    model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                } else {
                    model.addAttribute("like", 0);
                }

                model.addAttribute("news", news);

                // 获取新闻的评论
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);

                // 需要打包到viewObject
                List<ViewObject> vos = new ArrayList<>();
                for (Comment comment : comments) {
                    User user = userService.getUser(comment.getUserId());
                    ViewObject vo = new ViewObject();
                    vo.set("user", user);
                    vo.set("comment", comment);
                    vos.add(vo);
                }
                model.addAttribute("comments", vos);
            }
            model.addAttribute("owner", userService.getUser(news.getUserId()));
            model.addAttribute("loginer", hostHolder.getUser());

        } catch (Exception e) {
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";
    }


    // 上传图片
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = newsService.saveImage(file);
            if (imageUrl == null) {
                return HelpUtils.getJSONString(1, "上传失败");
            }
            return HelpUtils.getJSONString(0, imageUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return HelpUtils.getJSONString(1, "上传失败");
        }
    }

    // 访问图片
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ResponseBody
    public void getImage(@RequestParam("name") String name, HttpServletResponse httpServletResponse) {

        try {
            httpServletResponse.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(HelpUtils.IMAGE_DIR + name), httpServletResponse.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + name + e.getMessage()) ;
        }
    }


    // 添加新闻
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setLink(link);
            news.setTitle(title);
            news.setUserId(hostHolder.getUser().getId());
            newsService.addNews(news);
            return HelpUtils.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return HelpUtils.getJSONString(1, "发布失败");
        }
    }

    // 添加评论
    @RequestMapping(value = "addComment", method = RequestMethod.POST)
    public String addComment(@RequestParam("newsId") int newsId, @RequestParam("content") String content) {
        try {

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            comment.setUserId(hostHolder.getUser().getId());

            commentService.addComment(comment);

            // 然后还要更新News表中commentCount的值
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        }catch (Exception e) {
            logger.error("评论添加失败" + e.getMessage());
        }
        return "redirect:/news/" + newsId;
    }


    @RequestMapping("/deleteComment")
    public String deleteComment(HttpServletRequest httpServletRequest, @RequestParam("id") int id) {
        try {
            commentService.deleteComment(id);
        } catch (Exception e) {
            logger.error("删除评论失败" + e.getMessage());
        }
        String referer = httpServletRequest.getHeader("Referer");
        return "redirect:"+ referer;
    }

}
