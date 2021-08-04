package com.myself.mail.receiver;

import com.myself.server.pojo.Employee;
import com.myself.server.pojo.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 消息监听器
 *
 * @author Wei
 * @since 2021/8/2
 */
@Component
public class MailReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MailConstants.MAIL_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = MailConstants.MAIL_EXCHANGE_NAME),
            key = {MailConstants.MAIL_ROUTING_KEY}
    ))
    public void handler(Message message, Channel channel) {
        //获取员工信息
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //获取消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //获取msgId
        String msgId = (String) headers.get("spring_returned_message_correlation");

        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            //幂等性校验，判断是否接收到重复消息
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                LOGGER.error("消息已经被消费！", msgId);
                //手动确认消息，false表示单条确认
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送日期
            helper.setSentDate(new Date());

            //邮件内容
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            //引入mail.html邮件模板
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true); //true表示处理成html页面

            //发送邮件
            javaMailSender.send(msg);
            LOGGER.info("邮件发送成功！");
            hashOperations.put("mail_log", msgId, "OK");
            //手动确认消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, true); //ture表示退回到队列
            } catch (IOException ioException) {
                LOGGER.error("邮件发送失败！", e.getMessage());
            }
            LOGGER.error("邮件发送失败！", e.getMessage());
        }
    }
}
