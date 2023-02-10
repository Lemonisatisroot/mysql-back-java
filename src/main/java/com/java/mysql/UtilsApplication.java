package com.java.mysql;

import com.java.mysql.utils.TimeTaskUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author HASEE
 * @title UtilsApplication
 * @date 2023/2/8 11:54
 * @description TODO
 */
@SpringBootApplication
@EnableScheduling
public class UtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UtilsApplication.class,args);

    }
}
