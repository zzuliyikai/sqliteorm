package com.yikai.sqliteframe;

import com.yikai.sqliteframe.annotation.DbField;
import com.yikai.sqliteframe.annotation.DbTable;

/**
 * Created by 15738 on 2019/4/10.
 */
@DbTable("tb_user")
public class User {
    @DbField("tb_name")
    private String name;
    @DbField("tb_password")
    private String password;
    @DbField("tb_id")
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
