package com.yikai.sqliteframe.db;

import java.util.List;

/**
 * Created by Administrator on 2019/4/8.
 */

public interface IBaseDao<T> {


    long insert(T entity);

    int delete(T entity);

    int update(T entity,T where);

    List<T> query(T where);

    List<T> query(T where,String orderBy, Integer startIndex, Integer limit);

}
