package com.lunaticf.BottomBuzz;

import com.lunaticf.BottomBuzz.dao.CommentDAO;
import com.lunaticf.BottomBuzz.dao.NewsDAO;
import com.lunaticf.BottomBuzz.dao.UserDAO;
import com.lunaticf.BottomBuzz.model.Comment;
import com.lunaticf.BottomBuzz.model.EntityType;
import com.lunaticf.BottomBuzz.model.News;
import com.lunaticf.BottomBuzz.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class insertDataTests {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Test
    public void insertUser() {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setHeadUrl("http://bottombuzz/user" + random.nextInt(100));
            user.setName("liugewr" + i);
            user.setPassword("fuck");
            user.setSalt("salt");
            userDAO.addUser(user);
        }
        Assert.assertNotNull(userDAO.getUser(1));

    }

    @Test
    public void addNews() {
        Random random = new Random();

        for (int i = 22; i < 33; i++){
            News news = new News();
            news.setCommentCount(i);

            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);
        }

    }

    @Test
    public void addComments() {
        for (int i = 45; i <= 48; i++) {
            Comment comment = new Comment();
            comment.setUserId(i+1);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            comment.setContent("这里是一个评论啊！");
            comment.setEntityId(i);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            commentDAO.addComment(comment);
        }
    }

}
