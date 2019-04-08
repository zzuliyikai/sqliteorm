package com.yikai.sqliteframe;

import com.yikai.sqliteframe.annotation.DbField;
import com.yikai.sqliteframe.annotation.DbTable;

/**
 * Created by Administrator on 2019/4/8.
 */
@DbTable("tb_person")
public class Person {

    @DbField("name")
    private String name;
    @DbField("password")
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
