package com.java.mysql.service.impl;

import com.java.mysql.dao.Result;
import com.java.mysql.service.BackService;
import com.java.mysql.utils.MySQLUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * @author HASEE
 * @title BackServiceImpl
 * @date 2023/2/9 11:40
 * @description TODO
 */
@Service
@Slf4j
public class BackServiceImpl implements BackService {

    @Autowired
    private MySQLUtils mySQLUtils;


    @Override
    public String backAlldatabases(Integer isFlag) {

        boolean isData = true;

        if (isFlag != 1) {
            isData = false;
        }

        String fileName = getFileName();

        log.info("保存的文件名称为 ----->" + fileName);

        boolean back = mySQLUtils.mysqlBackUpAllDataBase(fileName, isData);

        return back ? "备份成功!" : "备份失败!";
    }

    @Override
    public String backDatabases(List<String> databaseNames, Boolean isData) {

        if (databaseNames.isEmpty()) {
            return "传入数据为空！";
        }

        boolean back = mySQLUtils.mysqlBackUpByDataBaseName(databaseNames, getFileName(), isData);
        return back ? "备份成功!" : "备份失败!";
    }

    @Override
    public String backDatabasesByTableNames(String databaseName, List<String> tableNames, Boolean isData) {

        if (tableNames.isEmpty() && databaseName.isEmpty()) {
            return "传入数据为空！";
        }

        boolean back = mySQLUtils.mysqlBackUpByTableName(databaseName, tableNames, getFileName(), isData);
        return back ? "备份成功!" : "备份失败!";
    }

    @Override
    public String backEvent(String databaseName) {

        if (databaseName.isEmpty()) {
            return "传入数据为空！";
        }

        boolean back = mySQLUtils.mysqlBackUpEvent(databaseName, getFileName());
        return back ? "备份成功!" : "备份失败!";
    }

    @Override
    public String backStorageAndFun(String databaseName) {

        if (databaseName.isEmpty()) {
            return "传入数据为空！";
        }

        boolean back = mySQLUtils.mysqlBackUpStorageAndFun(databaseName, getFileName());
        return back ? "备份成功!" : "备份失败!";
    }


    private static String getFileName() {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = simpleDateFormat.format(date) + ".sql";

        log.info("生成的文件为 ------ >" + fileName);

        return fileName;
    }
}
