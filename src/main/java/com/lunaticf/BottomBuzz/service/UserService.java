package com.lunaticf.BottomBuzz.service;


import com.lunaticf.BottomBuzz.dao.LoginTicketDAO;
import com.lunaticf.BottomBuzz.dao.UserDAO;
import com.lunaticf.BottomBuzz.model.LoginTicket;
import com.lunaticf.BottomBuzz.model.User;
import com.lunaticf.BottomBuzz.utils.HelpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.getUser(id);
    }

    public Map<String, Object> reg(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        // 检查用户名和密码是否为空
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        // 检查用户名是否已经存在
        if (userDAO.getUserByName(username) != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        // 插入用户
        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(HelpUtils.MD5(password + user.getSalt()));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDAO.addUser(user);

        // 注册成功 马上登录
        String ticket = addLoginTicket(userDAO.getUserByName(username).getId());
        map.put("ticket", ticket);
        return map;
    }

    // 登录服务
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        // 检查用户名和密码是否为空
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        // 如果用户名不存在
        if (userDAO.getUserByName(username) == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        // 检查密码是否正确
        User user = userDAO.getUserByName(username);
        String reqPwd = HelpUtils.MD5(password + user.getSalt());
        if (!reqPwd.equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        // 密码正确 生成ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    private String addLoginTicket(int userId) {
        // 把loginTicket加入数据库
        LoginTicket loginTicket = new LoginTicket();
        Date date = new Date();
        date.setTime(date.getTime() + 3600*1000*24);
        loginTicket.setExpired(date);
        loginTicket.setUserId(userId);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));

        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();

    }

    // 登出
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

}
