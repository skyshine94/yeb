package com.myself.server.config.rabbitMQ;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myself.server.pojo.MailLog;
import com.myself.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * rabbitMQ配置类
 *
 * @author Wei
 * @since 2021/8/2
 */
public class RabbitMQConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //消息确认回调，data表示消息唯一标识，ack表示确认结果，cause表示失败原因
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                LOGGER.info("消息发送成功！", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1).eq("msgId", msgId));
            }else {
                LOGGER.error("消息发送失败！", msgId);
            }
        });
        //消息失败回调，msg表示消息主题，repCode表示响应码，repText表示响应描述
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            LOGGER.error("消息发送失败！", returnedMessage.getMessage());
        });
        return rabbitTemplate;
    }
}
