package com.java.mysql.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author HASEE
 * @title BackUpUtils
 * @date 2023/2/13 14:51
 * @description TODO
 */
@Component
@Slf4j
public class BackUpUtils {

    //数据库IP地址
    @Value("${back.host}")
    private String host;
    //数据库用户名
    @Value("${back.username}")
    private String userName;
    //数据库密码
    @Value("${back.password}")
    private String password;
    //备份保存地址
    @Value("${back.save-path}")
    private String savePath;
    //mysql的bin安装目录
    @Value("${back.mysql-bin}")
    private String mysqlBin;

    //数据库名称
    @Value("${back.database-name}")
    private String databaseName;
    //数据库对应下的表名
    @Value("${back.tables-name}")
    private List<String> tableNames;
    //备份的数据库名列表
    @Value("${back.databases-name}")
    private List<String> databasesName;

    //是否保存数据
    @Value("${back.is-data}")
    private Boolean isData;
    //是否全部备份
    @Value("${back.is-back-all}")
    private Boolean isBackAll;
    //是否导出触发器 (默认为true)
    @Value("${back.is-back-triggers}")
    private Boolean isBackTriggers;
    //是否备份事件
    @Value("${back.is-back-event}")
    private Boolean isBackEvent;
    //是否导出存储结构和自定义函数
    @Value("${back.is-back-storage}")
    private Boolean isBackStorage;



    /**
     * 对数据库进行备份操作(全部备份 / 选择备份) 根据配置文件进行备份
     * @param fileName
     * @return
     */
    public boolean backUpDataBase(String fileName){

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //拼接命令行的命令 数据库备份方法
        // mysqldump --opt --host=localhost --all-databases backup --user=root --password=root --result-file=E:\Sqldata\test.sql --default-character-set=utf8
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump").append(" --opt").append(" --host=").append(host);

        //是否备份所有数据库
        if(!isBackAll) {
            cmd.append(" --databases ");
            //遍历数据库列表
            for(String database : databasesName) {
                cmd.append(database + " ");
            }
        }else {
            cmd.append(" --all-databases ");
        }

        //是否备份数据结构
        if(!isData) {
            cmd.append(" --no-data "); // -d
        }

        //是否备份触发器
        if (!isBackTriggers) {
            cmd.append(" --skip-triggers ");
        }

        cmd.append(" --user=").append(userName)
                .append(" --password=").append(password)
                .append(" --result-file=").append(savePath + fileName)
                .append(" --default-character-set=utf8 ");
        try {
            //调用外部执行exe文件的javaAPI
            Process process = Runtime.getRuntime().exec(cmd.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止
                //System.out.println("操作完成!!");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 对指定数据库中的一个或多个表进行备份 根据配置文件进行备份
     * @param fileName 文件名称
     * @return true: 备份成功  false: 备份失败
     */
    public boolean backUpByTableName(String fileName) {

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //拼接命令行的命令 数据库备份方法
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump").append(" --opt")
                .append(" --host=").append(host)
                .append(" --databases ").append(databaseName)
                .append(" --tables ");
        if(!tableNames.isEmpty()) {

            for(String tableName : tableNames) {
                cmd.append(tableName + " ");
            }
        }

        if(!isData) {
            cmd.append(" --no-data "); // -d
        }

        cmd.append(" --user=").append(userName)
                .append(" --password=").append(password)
                .append(" --result-file=").append(savePath + fileName)
                .append(" --default-character-set=utf8 ");
        try {
            //调用外部执行exe文件的javaAPI
            Process process = Runtime.getRuntime().exec(cmd.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止
                //System.out.println("操作完成!!");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 导出其他数据
     * @param fileName 文件名.sql
     * @return true: 备份成功  false: 备份失败
     */
    public boolean backUpOthers(String fileName) {

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //拼接命令行的命令 数据库备份方法
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump")
                .append(" --opt").append(" --host=")
                .append(host)
                .append(" --databases ").append(databaseName); // -B

        //是否备份存储结构和自定义函数
        if (!isBackStorage) {
            cmd.append(" --routines ");
        }
        //是否备份事件
        if (!isBackEvent) {
            cmd.append(" --events ");
        }

        cmd.append(" -ndt ")
                .append(" --user=").append(userName)
                .append(" --password=").append(password)
                .append(" --result-file=").append(savePath + fileName)
                .append(" --default-character-set=utf8 ");
        try {
            //调用外部执行exe文件的javaAPI
            Process process = Runtime.getRuntime().exec(cmd.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止
                //System.out.println("操作完成!!");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

}
