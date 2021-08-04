package com.myself.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myself.server.mapper.EmployeeMapper;
import com.myself.server.pojo.Employee;
import com.myself.server.pojo.MailConstants;
import com.myself.server.pojo.MailLog;
import com.myself.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送定时任务
 *
 * @author Wei
 * @since 2021/8/2
 */
@Component
@EnableScheduling
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //每隔10秒执行邮件发送
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        //获取消息状态为投递中且重试时间小于当前时间的消息列表
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0).lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            //超过重试次数，更新状态为投递失败。
            if (MailConstants.MAX_TRY_COUNT <= mailLog.getCount()) {
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status", 2)
                        .eq("msgId", mailLog.getMsgId())
                );
            }
            //更新重试次数、更新时间、重试时间
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count", mailLog.getCount() + 1)
                    .set("updateTime", LocalDateTime.now())
                    .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .eq("msgId", mailLog.getMsgId())
            );
            Employee emp = employeeMapper.getEmployeeById(mailLog.getEid());
            //发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY, emp, new CorrelationData(mailLog.getMsgId()));
        });
    }
}
