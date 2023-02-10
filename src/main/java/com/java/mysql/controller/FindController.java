package com.java.mysql.controller;

import com.java.mysql.dao.Result;
import com.java.mysql.service.DatabasesService;
import com.java.mysql.vo.params.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author HASEE
 * @title FindController
 * @date 2023/2/9 17:19
 * @description TODO
 */
@RestController
@RequestMapping("mysql")
public class FindController {

    @Autowired
    private DatabasesService databasesService;

    @PostMapping("findList")
    public Result showDataBasesList(@RequestBody UserParams userParams) {
        return databasesService.showDataBases(userParams.getUserName(),  userParams.getPassWord());
    }

    @PostMapping("findTableList")
    public Result showTableList(@RequestBody UserParams userParams) {
        return databasesService.showTables(userParams.getUserName(), userParams.getPassWord(), userParams.getDatabaseName());
    }
}
