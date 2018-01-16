package com.icourt.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * H2数据库工具类
 * @author cbb
 * @date 2017/3/27 14:37
 */
public class H2Util {


    /**
     * 修改数据库脚本并保存到临时目录
     * @param pathInclass 文件在classpath下路径
     * @return 临时文件路径
     */
    public static String getH2Script(String pathInclass) {
        String tempFolder = System.getProperty("java.io.tmpdir");
        if(!tempFolder.endsWith(File.separator)){
            tempFolder += File.separator;
        }
        String todayStr = DateKit.getDateStr(new Date());

        String path = tempFolder+todayStr+File.separator+pathInclass;
        File file = new File(path);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            ClassLoader classLoader = H2Util.class.getClassLoader();
            URL resource = classLoader.getResource(pathInclass);
            assert resource != null;
            InputStream inputStream = resource.openStream();
            String script = IOUtils.toString(inputStream);
            script = script.replaceAll("(CHARSET=utf8\\s)(COMMENT=.*;)","$1;");
            FileUtils.write(file,script);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

}
