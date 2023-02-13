package com.java.mysql.utils;

import com.java.mysql.service.BackService;
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
    private BackUpUtils backUpUtils;


    @Scheduled(cron = "${time.cron-first}")
    public void doTask1() {
        String fileName = "db_" + getFileName();
        log.info("生成的文件为 ------ >" + fileName);
        boolean back = backUpUtils.backUpDataBase(fileName);
        log.info("备份数据库 ----> 定时任务已经执行 -----> 执行结果" + back);

    }

    @Scheduled(cron = "${time.cron-second}")
    public void doTask2() {
        String fileName = "table_" + getFileName();
        log.info("生成的文件为 ------ >" + fileName);
        boolean back = backUpUtils.backUpByTableName(fileName);
        log.info("备份数据库表 ----> 定时任务已经执行 -----> 执行结果" + back);
    }

//    @Scheduled(cron = "0/6 * * * * ?")
//    public void doTask3() {
//        String fileName = "other_" + getFileName();
//        log.info("生成的文件为 ------ >" + fileName);
//        boolean others = backUpUtils.backUpOthers(fileName);
//        log.info("其他备份 ----> 定时任务已经执行 -----> 执行结果" + others);
//    }




    /**
     * 生成随机文件名称
     * @return
     */
    private static String getFileName() {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = simpleDateFormat.format(date) + ".sql";

        return fileName;
    }
}
