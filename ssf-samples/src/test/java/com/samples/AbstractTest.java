/*
 * @date 2017年03月30日 下午10:51
 */
package com.samples;

import com.icourt.common.DateKit;
import com.icourt.orm.dao.IApplicationDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author june
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@Transactional
public abstract class AbstractTest {

    @Autowired
    protected IApplicationDao applicationDao;

    @Before
    public void setUp() throws Exception {
        applicationDao.execute("drop all objects;");
        applicationDao.execute("runscript from '"+getH2Script("user.sql")+"'");
    }

    /**
     * 修改数据库脚本并保存到临时目录
     * @param pathInclass 文件在classpath下路径
     * @return 临时文件路径
     */
    protected static String getH2Script(String pathInclass) {
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
            ClassLoader classLoader = AbstractTest.class.getClassLoader();
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
