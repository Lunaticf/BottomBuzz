package com.lunaticf.BottomBuzz.service;

import com.lunaticf.BottomBuzz.dao.NewsDAO;
import com.lunaticf.BottomBuzz.dao.UserDAO;
import com.lunaticf.BottomBuzz.model.*;
import com.lunaticf.BottomBuzz.utils.HelpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public List<ViewObject> getLatestNews(int userId, int offset, int limit) {

        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;

        List<ViewObject> res = new ArrayList<>();

        List<News> news = newsDAO.selectByUserIdAndOffset(userId, offset, limit);

        for (News singleNews : news) {
            ViewObject vo = new ViewObject();
            vo.set("news", singleNews);

            User user = userDAO.getUser(singleNews.getUserId());
            vo.set("user", user);

            // 如果当前有用户 就应该看到自己的状态
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, singleNews.getId()));
            } else {
                vo.set("like", 0);
            }

            res.add(vo);
        }
        return res;
    }

    // 根据id获取新闻
    public News getNewsById(int newsId) {
        return newsDAO.selectById(newsId);
    }

    // 保存图片
    public String saveImage(MultipartFile file) throws IOException {
        String ext = HelpUtils.getFileExt(file.getOriginalFilename());
        if (ext == null) {
            return null;
        }
        if (!HelpUtils.isFileAllowed(ext)) {
            return null;
        }

        String randomFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + ext;
        // 保存到本地
        Files.copy(file.getInputStream(), new File(HelpUtils.IMAGE_DIR + randomFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return HelpUtils.APP_DOMAIN + "image?name=" + randomFileName;
    }

    public int updateCommentCount(int id, int commentCount) {
        return newsDAO.updateCommentCount(id, commentCount);
    }

    public int updateLikeCount(int id, int likeCount) {return newsDAO.updateLikeCount(id, likeCount); }


}
