package org.jictorinox;

import org.junit.Assert;
import org.junit.Test;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-29
 * Time: 上午11:10
 *
 * @auth gaohongtao
 */
public class TestSystem {

    @Test
    public void testCpu() {
        //多核cpu每个核都是一个处理器
        Assert.assertEquals(2, Runtime.getRuntime().availableProcessors());
    }
}
