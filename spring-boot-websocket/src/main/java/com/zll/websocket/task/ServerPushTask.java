package com.zll.websocket.task;

import cn.hutool.core.date.DateUtil;
import com.zll.websocket.model.ServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>服务定时推送数据任务</p>
 */
@Component
@Slf4j
@EnableScheduling
public class ServerPushTask {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    /**
     * 2s 执行一次 模拟服务器产生新的数据
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void wsSendMsg() {

        log.info("【推送消息开始】" + DateUtil.formatDateTime(new Date()));

        ServerInfo serverInfo = new ServerInfo();

        simpMessagingTemplate.convertAndSend("/topic/server", serverInfo);

    }

}