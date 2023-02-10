package com.java.mysql.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * @author HASEE
 * @title MySQLUtils
 * @date 2023/2/7 22:06
 * @description TODO 数据库备份操作工具类
 */
@Component
public class MySQLUtils {

//    //数据库IP地址
//    private static String host = "localhost";
//    //数据库用户名
//    private static String userName = "root";
//    //数据库密码
//    private static String password = "abc123";
//    //备份保存地址
//    private static String savePath = "D:\\TestModel";
//    //mysql的bin安装目录
//    private static String mysqlBin = "D:\\MySQL\\MySQL Server 8.0\\bin";
//    //数据库名称
//    private static String databaseName = "cloud";

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    private static final String showDataBaseSQL = "SHOW DATABASES;";

    private static final String showTablesSQL = "select table_name from information_schema.tables where table_schema = ?";

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
    @Value("${back.savepath}")
    private String savePath;
    //mysql的bin安装目录
    @Value("${back.mysqlbin}")
    private String mysqlBin;
    //数据库名称
    @Value("${back.databasename}")
    private String databaseName;



    // 获取数据库连接
    private Connection getDataBaseConnection(String userName, String passWord) throws Exception {
        Class<?> driverClass = Class.forName(driverClassName);
        Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(driver);
        return DriverManager.getConnection(url, userName, passWord);
    }

    //查询所有数据库
    public Map<String, List<String>> getDataBaseName(String userName, String passWord) throws Exception {
        Connection connection = getDataBaseConnection(userName, passWord);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(showDataBaseSQL);
        List<String> dataBaseNames = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            int count = metaData.getColumnCount();
            for (int i = 0; i < count; i++) {
                dataBaseNames.add(resultSet.getString(i + 1));
            }
        }

        resultSet.close();
        statement.close();
        connection.close();

        Map<String, List<String>> resultMap = new HashMap<>();
        resultMap.put("databaseNames", dataBaseNames);

        return resultMap;
    }

    //查询数据库对应的所有表名
    public Map<String, List<String>> getTablesByDataBase(String username, String password, String databaseName) throws Exception{
        Connection connection = getDataBaseConnection(username, password);
        PreparedStatement statement = connection.prepareStatement(showTablesSQL);
        statement.setString(1, databaseName);
        ResultSet resultSet = statement.executeQuery();
        List<String> tableNames = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();while (resultSet.next()) {
            int count = metaData.getColumnCount();
            for (int i = 0; i < count; i++) {
                tableNames.add(resultSet.getString(i + 1));
            }
        }

        resultSet.close();
        statement.close();
        connection.close();

        Map<String, List<String>> resultMap = new HashMap<>();
        resultMap.put("tableNames", tableNames);

        return resultMap;

    }



    /**
     * 备份所有数据库
     * @param fileName 保存的文件名
     * @param isData 是否保存表中数据
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpAllDataBase(String fileName, boolean isData){

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
        cmd.append(mysqlBin).append("/mysqldump")
                .append(" --opt").append(" --host=")
                .append(host)
                .append(" --all-databases ");

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
     * 备份多个数据库的方法
     * @param databaseNames 数据库名列表
     * @param fileName 文件名称
     * @param isData 是否备份表中数据
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpByDataBaseName(List<String> databaseNames, String fileName, boolean isData) {

        if(databaseNames.isEmpty()) {
            return false;
        }

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //拼接命令行的命令 数据库备份方法
        // mysqldump --opt --host=localhost --databases backup --user=root --password=root --result-file=E:\Sqldata\test.sql --default-character-set=utf8
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump")
                .append(" --opt").append(" --host=")
                .append(host)
                .append(" --databases ");
        //循环添加多个数据库
        for (String databaseName: databaseNames) {
            cmd.append(databaseName + " ");
        }

        if (!isData) {
            cmd.append(" --no-data ");
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
     * 对数据库中的一个或多个表进行备份
     * @param tableNames 数据库表名
     * @param fileName 文件名称
     * @param isData 是否只保存数据
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpByTableName(List<String> tableNames, String fileName, boolean isData) {

        if(tableNames.isEmpty()) {
            return false;
        }

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //拼接命令行的命令 数据库备份方法
        // mysqldump --opt --host=localhost --databases backup --tables log_sys sys_user --user=root --password=root --result-file=E:\Sqldata\test.sql --default-character-set=utf8
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump").append(" --opt")
                .append(" --host=").append(host)
                .append(" --databases ").append(databaseName)
                .append(" --tables ");

        for(String tableName : tableNames) {
            cmd.append(tableName + " ");
        }

        if(!isData) {
            cmd.append(" --no-data ");
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
     * 对指定数据库中的一个或多个表进行备份
     * @param tableNames 数据库表名
     * @param fileName 文件名称
     * @param isData 是否保存表中数据
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpByTableName(String databaseName, List<String> tableNames, String fileName, boolean isData) {

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //拼接命令行的命令 数据库备份方法
        // mysqldump --opt --host=localhost --databases backup --tables log_sys sys_user --user=root --password=root --result-file=E:\Sqldata\test.sql --default-character-set=utf8
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump").append(" --opt")
                .append(" --host=").append(host)
                .append(" --databases ").append(databaseName) // -B
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
     * 备份所有表空间
     * @param databaseName 数据库名称
     * @param fileName 文件名称
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpAllTablesPaces(String databaseName, String fileName) {
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
                .append(" --databases ").append(databaseName) // -B
                .append(" --all-tablespaces ") // -Y
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


    /**
     * 导出数据库存储结构和自定义函数
     * @param databaseName 数据库名
     * @param fileName 文件名.sql
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpStorageAndFun(String databaseName, String fileName) {

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
                .append(" --databases ").append(databaseName) // -B
                .append(" -R ") // --routines
                .append(" -ndt ")
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

    /**
     * 导出事件
     * @param databaseName 数据库名
     * @param fileName 文件名.sql
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpEvent(String databaseName, String fileName) {

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
                .append(" --databases ").append(databaseName) // -B
                .append(" -E ") // --events
                .append(" -ndt ")
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


    /**
     * 不导出触发器
     * @param databaseName 数据库名
     * @param fileName 文件名.sql
     * @return true: 备份成功  false: 备份失败
     */
    public boolean mysqlBackUpTrigger(String databaseName, String fileName) {

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
                .append(" --opt").append(" --host=").append(host)
                .append(" --databases ").append(databaseName) // -B
                .append(" --skip-triggers ") // 屏蔽导出(默认为导出)
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

    /**
     * 数据库增量备份
     * @param fileName 保存文件名
     * @return true: 备份成功  false: 备份失败
     */
    public Boolean databaseIncrementBackup(String fileName) {

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            //给保存文件路径 + \  拼接保存路径
            savePath = savePath + File.separator;
        }
        //全备命令
        //mysqldump --single-transaction --flush-logs --master-data=2 > backup.sql
        StringBuilder cmd = new StringBuilder();
        cmd.append(mysqlBin).append("/mysqldump")
                .append(" --host=").append(host)
                .append(" -u ").append(userName)
                .append(" -p").append(password)
                .append(" --all-databases")
                .append(" --single-transaction")
                .append(" --flush-logs")
                .append(" --master-data=2")
                .append(" --result-file=").append(savePath + fileName)
                .append(" --default-character-set=utf8 ");


        try {
            Process process = Runtime.getRuntime().exec(cmd.toString());
            process.waitFor();//等待上述命令执行完毕后打印下面log
            System.out.println(process.exitValue());
            System.out.println("数据库备份结束，备份结果：{}" + (process.exitValue() == 0 ? "success" : "fail"));
            if (process.exitValue() == 0) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
