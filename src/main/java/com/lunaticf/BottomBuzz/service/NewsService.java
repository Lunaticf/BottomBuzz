package com.lunaticf.BottomBuzz.service;

import com.lunaticf.BottomBuzz.dao.NewsDAO;
import com.lunaticf.BottomBuzz.dao.UserDAO;
import com.lunaticf.BottomBuzz.model.News;
import com.lunaticf.BottomBuzz.model.User;
import com.lunaticf.BottomBuzz.model.ViewObject;
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

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public List<ViewObject> getLatestNews(int userId, int offset, int limit) {
//        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);

        List<ViewObject> res = new ArrayList<>();

        List<News> news = newsDAO.selectByUserIdAndOffset(userId, offset, limit);

        for (News singleNews : news) {
            ViewObject vo = new ViewObject();
            vo.set("news", singleNews);

            User user = userDAO.getUser(singleNews.getUserId());
            vo.set("user", user);
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

}
