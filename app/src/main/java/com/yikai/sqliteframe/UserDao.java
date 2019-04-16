package com.yikai.sqliteframe;

import com.yikai.sqliteframe.db.BaseDao;

import java.util.List;

public class UserDao extends BaseDao {

    @Override
    public String createTable() {
        return "create table if not exists tb_user(tb_id int,tb_name varchar(20),tb_password varchar(10))";
    }


    @Override
    public List query(Object where) {
        return super.query(where);
    }
}
