package org.jictorinox.cron;

import org.quartz.JobKey;

import java.util.Iterator;
import java.util.SortedMap;

/**
 * <p/>
 * 定时任务台,定义任务台提供的功能
 * <p/>
 * <p/>
 * Date: 13-10-10
 * Time: 下午3:05
 *
 * @auth gaohongtao
 */
public interface CronTab<KEY_TYPE> {

    /**
     * 初始化启动工作台
     *
     * @throws CronTabException 初始化或者启动工作台失败
     */
    void start() throws CronTabException;

    void shutdown() throws CronTabException;

    void pauseJob(KEY_TYPE key) throws CronTabException;

    void pauseAll() throws CronTabException;

    void resumeJob(KEY_TYPE key) throws CronTabException;

    void resumeAll() throws CronTabException;

    void submitJob(KEY_TYPE key, CronJob job) throws CronTabException;

    void deleteJob(KEY_TYPE key) throws CronTabException;

    void interruptJob(KEY_TYPE key) throws CronTabException;

    CronJob getJob(KEY_TYPE key);

    SortedMap<String, Iterator<CronJob>> getAllJob();


}
