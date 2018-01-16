package com.icourt.common;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常相关工具类
 * @author june
 */
public class ExceptionKit {


    /**
     * 取异常堆栈信息
     *
     * @return 异常堆栈信息
     */
    public static String getStackTraceText(Throwable _ex) {
        StringWriter strWriter = null;
        PrintWriter prtWriter = null;
        try {
            strWriter = new StringWriter();
            prtWriter = new PrintWriter(strWriter);
            _ex.printStackTrace(prtWriter);
            prtWriter.flush();
            return strWriter.toString();
        } catch (Exception ex2) {
            return _ex.getMessage();
        } finally {
            if (strWriter != null)
                try {
                    strWriter.close();
                } catch (Exception ex) {
                }
            if (prtWriter != null)
                try {
                    prtWriter.close();
                } catch (Exception ex) {
                }
        }
    }


    /**
     * 获取堆栈行号
     * @param _nDepth
     * @return
     */
    public static String getCallMeInfo(int _nDepth) {
        StackTraceElement[] pStackTraceElements = new Throwable().getStackTrace();
        int nDepth = _nDepth;
        if (nDepth >= pStackTraceElements.length) {
            nDepth = pStackTraceElements.length - 1;
        }

        StackTraceElement oStackTraceElement = pStackTraceElements[nDepth];
        return oStackTraceElement.getClassName() + "." + oStackTraceElement.getMethodName() + "("
                + oStackTraceElement.getFileName() + " " + oStackTraceElement.getLineNumber() + ")";
    }
}
