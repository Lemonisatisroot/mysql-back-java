package com.java.mysql.service;

import com.java.mysql.dao.Result;

import java.util.List;
import java.util.Map;

/**
 * @author HASEE
 * @title DatabasesService
 * @date 2023/2/9 17:20
 * @description TODO
 */
public interface DatabasesService {

    /**
     * 查找所有的数据库
     * @param userName 数据库用户名
     * @param password 数据库用户密码
     * @return
     */
    Result showDataBases(String userName, String password);

    /**
     * 查找对应数据库下的所有表名
     * @param userName
     * @param passWord
     * @param databaseName
     * @return
     */
    Result showTables(String userName, String passWord, String databaseName);
}
