package org.jictorinox.cron;

import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Iterator;
import java.util.SortedMap;


/**
 * 能力框架
 * <p/>
 * Date: 13-10-10
 * Time: 下午5:35
 *
 * @auth gaohongtao
 */
public class StsCronTab implements CronTab<JobKey> {
    private final Scheduler scheduler;
    private final Object lock = new Object();

    public StsCronTab() throws CronTabException {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            throw new CronTabException("初始化失败", e);
        }
    }

    @Override
    public void start() throws CronTabException {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new CronTabException("启动失败", e);
        }
    }

    @Override
    public void shutdown() throws CronTabException {
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            throw new CronTabException("关闭失败", e);
        }
    }

    @Override
    public void pauseJob(JobKey key) throws CronTabException {
        try {
            scheduler.pauseJob(key);
        } catch (SchedulerException e) {
            throw new CronTabException("暂停任务失败", e);
        }
    }

    @Override
    public void pauseAll() throws CronTabException {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            throw new CronTabException("暂停说有任务失败", e);
        }
    }

    @Override
    public void resumeJob(JobKey key) throws CronTabException {
        try {
            scheduler.resumeJob(key);
        } catch (SchedulerException e) {
            throw new CronTabException("恢复任务失败", e);
        }
    }

    @Override
    public void resumeAll() throws CronTabException {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            throw new CronTabException("恢复任务失败", e);
        }
    }

    @Override
    public void submitJob(JobKey key, CronJob job) throws CronTabException {
        synchronized (lock) {
            deleteJob(key);
            try {
                StsCronJob cronJob = (StsCronJob) job;
                scheduler.scheduleJob(cronJob.jobDetail, cronJob.trigger);
            } catch (SchedulerException e) {
                throw new CronTabException("增加任务出现异常", e);
            }

        }

    }

    @Override
    public void deleteJob(JobKey key) throws CronTabException {
        try {
            scheduler.deleteJob(key);
        } catch (SchedulerException e) {
            throw new CronTabException("删除任务失败", e);
        }
    }

    @Override
    public void interruptJob(JobKey key) throws CronTabException {
        try {
            scheduler.interrupt(key);
        } catch (SchedulerException e) {
            throw new CronTabException("中断任务失败", e);
        }
    }

    @Override
    public CronJob getJob(JobKey key) {
        try {
            return new StsCronJob(scheduler.getJobDetail(key), (CronTrigger) scheduler.getTriggersOfJob(key).get(0));
        } catch (SchedulerException e) {
            return null;
        }
    }

    @Override
    public SortedMap<String, Iterator<CronJob>> getAllJob() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
