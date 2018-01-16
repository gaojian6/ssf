package com.icourt.orm.dao;

import com.icourt.common.DateKit;
import com.icourt.common.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * SQL报告
 * @author june
 */
public class SqlReporter {

    private static Logger logger = LoggerFactory.getLogger(SqlReporter.class);

    public static void doReport(SqlParams sp,Long time){
        if(sp.getParamMap()!=null){
            doReport(sp.getSql(),sp.getParamMap(),time,null);
        }else {
            doReport(sp.getSql(), sp.getParams(),time,null);
        }
    }

    public static void doReport(String sql , Object[] params,Long time,String dsId){
        String paramsStr = null;
        if(params!=null){
            paramsStr = "";
            for(int i=0;i<params.length;i++){
                if(i>0){
                    paramsStr += ",";
                }
                Object obj = params[i];
                if(obj instanceof Date){
                    paramsStr += DateKit.getDateTimeStr((Date)obj);
                }else{
                    paramsStr += params[i];
                }
            }
        }
        doReport(sql,paramsStr,time,dsId);
    }

    public static void doReport(String sql , Map<String,Object> paramMap,Long time,String dsId){
        doReport(sql, (paramMap == null ? null : paramMap.toString()),time,dsId);
    }

    public static void doReport(String sql , String paramStr,Long time,String dsId){
        StringBuilder sb = new StringBuilder();
        sb.append("\nSQL：").append(sql);
        sb.append("\nParams：").append(paramStr);
        if(time!=null){
            sb.append("\nTime：").append(time).append(" ms");
        }
        if(StrKit.isNotEmpty(dsId)){
            sb.append("\nDsId：").append(dsId);
        }
        sb.append("\n--------------------------------------------------------------------------------");
        logger.info(sb.toString());
    }


}
