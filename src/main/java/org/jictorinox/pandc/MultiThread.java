package org.jictorinox.pandc;

/**
 * 有并发能力的接口
 * <p/>
 * Date: 13-9-11
 * Time: 下午2:53
 *
 * @auth gaohongtao
 */
public interface MultiThread {

    /**
     * 获取当前接口中并发能力
     * @return
     */
    int getConcurrentThreadNum();

    /**
     * 抛出异常,进行必要处理,然后判断是否要继续启动新线程运行
     *
     * @param t 异常
     * @return
     */
    boolean exceptionAndIsContinue(Throwable t);

    /**
     * 线程退出的时候执行相关操作
     */
    void exitHook();
}
