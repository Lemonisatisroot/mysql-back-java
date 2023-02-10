package com.java.mysql.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HASEE
 * @title User
 * @date 2023/2/9 17:30
 * @description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserParams {

    private String userName;

    private String passWord;

    private String databaseName;
}
