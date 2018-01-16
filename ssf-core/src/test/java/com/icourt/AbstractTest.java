/*
 * @date 2017年03月30日 下午10:51
 */
package com.icourt;

import com.icourt.common.H2Util;
import com.icourt.orm.dao.IApplicationDao;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        applicationDao.execute("runscript from '"+ H2Util.getH2Script("test.sql")+"'");
    }


}
