package com.java.mysql.service.impl;

import com.java.mysql.dao.Result;
import com.java.mysql.service.DatabasesService;
import com.java.mysql.utils.MySQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author HASEE
 * @title DatabasesServiceImpl
 * @date 2023/2/9 17:20
 * @description TODO
 */
@Service
@Slf4j
public class DatabasesServiceImpl implements DatabasesService {

    @Autowired
    private MySQLUtils mySQLUtils;

    @Override
    public Result showDataBases(String userName, String passWord) {

        if(userName.isEmpty() || passWord.isEmpty()) {
            return Result.fail(500, "用户名或密码为空");
        }

        Map<String, List<String>> baseNameList = null;
        try {
            baseNameList = mySQLUtils.getDataBaseName(userName, passWord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!baseNameList.get("databaseNames").isEmpty()) {
            log.info("查询成功 ----->" + baseNameList.toString());
            return Result.success(baseNameList);
        }

        return Result.fail(500,"数据库为空!!");
    }

    @Override
    public Result showTables(String userName, String passWord, String databaseName) {

        if(userName.isEmpty() || passWord.isEmpty()) {
            return Result.fail(500, "用户名或密码为空");
        }

        if(databaseName.isEmpty()) {
            return Result.fail(500, "数据库名称不能为空！");
        }

        Map<String, List<String>> tableNameList = null;
        try {
            tableNameList = mySQLUtils.getTablesByDataBase(userName, passWord, databaseName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!tableNameList.get("tableNames").isEmpty()) {
            log.info("查询成功 ----->" + tableNameList.toString());
            return Result.success(tableNameList);
        }

        return Result.fail(500,"该数据库为空!!");

    }
}
