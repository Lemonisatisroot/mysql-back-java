package com.java.mysql.service;

import com.java.mysql.dao.Result;

import java.util.List;

/**
 * @author HASEE
 * @title BackService
 * @date 2023/2/9 11:39
 * @description TODO
 */

public interface BackService {

    /**
     * 备份所有数据库
     * @param isFlag 是否保存数据
     * @return
     */
    String backAlldatabases(Integer isFlag);

    /**
     * 选择备份数据库
     * @param databaseNames 数据库名称列表
     * @param isData 是否保存数据
     * @return
     */
    String backDatabases(List<String> databaseNames, Boolean isData);

    /**
     * 备份对应数据库中的一表或多表
     * @param databaseName 数据库名
     * @param tableNames 表名
     * @param isData 是否保存数据
     * @return
     */
    String backDatabasesByTableNames(String databaseName, List<String> tableNames, Boolean isData);

    /**
     * 备份事件
     * @param databaseName
     * @return
     */
    String backEvent(String databaseName);

    /**
     * 备份存储结构和自定义函数
     * @param databaseName
     * @return
     */
    String backStorageAndFun(String databaseName);


}
