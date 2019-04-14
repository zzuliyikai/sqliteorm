package com.yikai.sqliteframe;

/**
 * Created by Administrator on 2019/4/8.
 */

public interface IBaseDao<T> {


    long insert(T entity);

    int delete(T entity);

    int update(T entity,T where);


}
