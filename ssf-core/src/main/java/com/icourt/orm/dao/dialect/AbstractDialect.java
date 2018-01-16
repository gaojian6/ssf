/*
 * @date 2016年10月30日 14:44
 */
package com.icourt.orm.dao.dialect;

import com.icourt.common.ClassKit;
import com.icourt.orm.dao.Table;
import com.icourt.orm.dao.TableBuilder;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author june
 */
public abstract class AbstractDialect implements Dialect{

    private final static Map<Class,Table> pojoTableMap = new HashMap<>();

    /**
     * 获取pojo的表信息
     * @param clazz pojo class
     * @return table
     */
    public static Table getTable(Class<?> clazz){
        Assert.notNull(clazz);
        Table table = pojoTableMap.get(clazz);
        if(table==null){
            table = TableBuilder.build(ClassKit.newInstance(clazz));
            pojoTableMap.put(clazz,table);
        }
        return table;
    }
}
