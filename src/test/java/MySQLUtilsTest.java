import com.java.mysql.utils.MySQLUtils;
import com.java.mysql.utils.TimeTaskUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HASEE
 * @title MySQLUtilsTest
 * @date 2023/2/7 22:48
 * @description TODO
 */

public class MySQLUtilsTest {


    @Autowired
    private MySQLUtils mySQLUtils;
    
    @Test
    public void testThree() {
        /**
         * 备份所有数据库，是否备份数据
         */
        boolean isBack = mySQLUtils.mysqlBackUpAllDataBase("alldatabases.sql", false);
        System.out.println(isBack ? "备份完成":"备份失败");
    }

    @Test
    public void testFour() {
        /**
         * 备份数据库事件
         */
        boolean isBack = mySQLUtils.mysqlBackUpEvent("book", "backEvent");
        System.out.println(isBack ? "备份事件完成" : "备份事件失败" );
    }

    @Test
    public void testFive() {
        /**
         * 备份数据库存储结构和自定义函数
         */
        boolean isBack = mySQLUtils.mysqlBackUpStorageAndFun("book", "storage.sql");
        System.out.println(isBack ? "存储结构和函数完成" : "失败");
    }

    @Test
    public void testSix() {
        /**
         * 备份数据库的触发器
         */
        boolean isBack = mySQLUtils.mysqlBackUpTrigger("book", "trigger.sql");
        System.out.println(isBack ? "触发器备份完成" : "失败");
    }

    @Test
    public void testSeven() {
        /**
         * 备份数据库中所有表空间
         */
        boolean isBack = mySQLUtils.mysqlBackUpAllTablesPaces("book", "alltables");
        System.out.println(isBack ? "备份所有表空间成功" : "失败");
    }


    @Test
    public void test() {
        /**
         * 数据库备份，是否备份数据
         */
        List<String> databasesNames = new ArrayList<>();

        databasesNames.add("cloud");
        databasesNames.add("book");

        String fileName = "databasesList.sql";

        boolean isFlag = mySQLUtils.mysqlBackUpByDataBaseName(databasesNames, fileName, false);

        System.out.println(isFlag);

        if(isFlag) {
            System.out.println("操作成功");
        }else {
            System.out.println("操作失败");
        }
    }


    @Test
    public void testOne() {
        /**
         * 数据库表中的数据是否备份
         */
        List<String> tableNames = new ArrayList<>();
        tableNames.add("payment");
        boolean isBack = mySQLUtils.mysqlBackUpByTableName(tableNames, "time1.sql",true);
        System.out.println(isBack);
    }

}
