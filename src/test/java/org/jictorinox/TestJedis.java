package org.jictorinox;

import org.jictorinox.conf.InitialException;
import org.junit.Test;

import java.io.FileNotFoundException;


/**
 * 能力框架
 * <p/>
 * Date: 13-9-4
 * Time: 下午4:53
 *
 * @auth gaohongtao
 */
public class TestJedis {

    @Test
    public void testInit() throws FileNotFoundException, InitialException {
        new JedisFactory("/home/gaohongtao/桌面/adaptor-1.0/conf").load().init();
    }

    @Test
    public void testOracle() throws FileNotFoundException, JedisAccessException {
        new DataSourceUtil("/home/gaohongtao/桌面/adaptor-1.0/conf").load().init();
    }
}
