package com.java.mysql.dao;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author HASEE
 * @title Status
 * @date 2023/2/13 9:17
 * @description TODO
 */

@Data
public class Status {

    private String file;

    private BigDecimal position;
}
