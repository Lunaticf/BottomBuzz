package com.lunaticf.BottomBuzz.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;


@Service
public class MailSender implements InitializingBean {
    private JavaMailSenderImpl mailSender;

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    @Autowired
    private Configuration freemarkerConfig;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {

            String nick = MimeUtility.encodeText("BottomBuzz");
            InternetAddress from = new InternetAddress(nick + "<724922441@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
            Template t = freemarkerConfig.getTemplate(template);


            String result = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        // 邮箱和密码，用于发送邮件
        mailSender.setUsername("724922441@qq.com");
        mailSender.setPassword("kfzdhbvfghusbfbe");
        mailSender.setHost("smtp.qq.com");
        // 请配置自己的邮箱和密码

        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
