package com.java.mysql.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author HASEE
 * @title TimeTaskUtils 定时任务工具
 * @date 2023/2/8 11:59
 * @description TODO
 */
@Component
@Slf4j
public class TimeTaskUtils {

    @Autowired
    private MySQLUtils mySQLUtils;

    //添加定时任务
    //@Scheduled(cron = "30 40 23 0 0 5") // cron表达式：每周一 23:40:30 执行
    //@Scheduled(fixedRate = 30000)
    public void doTaskBackUp(){

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = simpleDateFormat.format(date) + ".sql";

        mySQLUtils.mysqlBackUpAllDataBase(fileName, true);

        log.info("定时备份已经执行 ------>" + fileName);


    }

    //添加定时任务,定时备份刷新日志
    //@Scheduled(cron = "30 40 23 0 0 5") // cron表达式：每周一 23:40:30 执行
    @Scheduled(fixedRate = 30000)
    public void doIncrementTaskBackUp(){

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = simpleDateFormat.format(date) + ".sql";

        mySQLUtils.databaseIncrementBackup(fileName);

        log.info("定时备份已经执行 ------>" + fileName);


    }
}
