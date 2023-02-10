package com.java.mysql.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author HASEE
 * @title DatabasesParams
 * @date 2023/2/9 13:41
 * @description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabasesParams {

    private List<String> databaseNames;

//    private Integer isFlag;

    private Boolean isData;
}
