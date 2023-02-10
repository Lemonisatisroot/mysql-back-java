package com.java.mysql.controller;

import com.java.mysql.dao.Result;
import com.java.mysql.service.BackService;
import com.java.mysql.vo.params.DatabasesParams;
import com.java.mysql.vo.params.TablesParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HASEE
 * @title BackController
 * @date 2023/2/9 10:54
 * @description TODO
 */
@RestController
@RequestMapping("mysql")
@Slf4j
public class BackController {

    @Autowired
    private BackService backService;

    /**
     * 备份所有数据库
     * @return
     */
    @GetMapping("back/databases/all/{isFlag}")
    public Result backAllDatabases(@PathVariable("isFlag") Integer isFlag) {

        return Result.success(backService.backAlldatabases(isFlag));
    }

    /**
     * 选择备份数据库
     * @param databasesParam
     * @return
     */
    @PostMapping("back/databases")
    public Result backDataBaseByNames(@RequestBody DatabasesParams databasesParam) {
        log.info("数据库名列表为 ---- >" + databasesParam.getDatabaseNames() + "   是否保存数据 --->" + databasesParam.getIsData());
        return Result.success(backService.backDatabases(
                databasesParam.getDatabaseNames(),
                databasesParam.getIsData()));
    }

    /**
     * 选择数据库备份对应下的表, 以及是否要数据
     * @return
     */
    @PostMapping("back/database/tables")
    public Result backTablesByNames(@RequestBody TablesParams tablesParams) {
        log.info("表名列表 ----- >" + tablesParams.getTableNames().toString());
        log.info("数据库名为 ---- >" + tablesParams.getDatabase() + "是否保存数据 --->" + tablesParams.getIsData());
        return Result.success(backService.backDatabasesByTableNames(
                tablesParams.getDatabase(),
                tablesParams.getTableNames(),
                tablesParams.getIsData()));
    }

    /**
     * 备份数据库存储结构
     * @param databaseName
     * @return
     */
    @GetMapping("back/database/storage/{databaseName}")
    public Result backStorage(@PathVariable("databaseName") String databaseName) {
        return Result.success(backService.backStorageAndFun(databaseName));
    }

    /**
     * 备份数据库事件
     * @param databaseName
     * @return
     */
    @GetMapping("back/database/event/{databaseName}")
    public Result backEvent(@PathVariable("databaseName") String databaseName) {
        return Result.success(backService.backStorageAndFun(databaseName));
    }


}
