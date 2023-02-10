package com.java.mysql.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author HASEE
 * @title TablesParams
 * @date 2023/2/9 14:02
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TablesParams {

    private String database;

    private List<String> tableNames;

    private Boolean isData;
}
