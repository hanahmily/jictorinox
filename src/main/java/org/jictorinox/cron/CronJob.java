package org.jictorinox.cron;

import com.sun.istack.internal.NotNull;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * cron任务
 * <p/>
 * Date: 13-10-10
 * Time: 下午3:13
 *
 * @auth gaohongtao
 */
public interface CronJob<JOD_DATA, RETURN_VALUE> {
    /**
     * @param name
     * @param group
     * @param cronExpression
     * @param jobData
     * @param callable
     * @return
     */
    CronJob build(@NotNull String name, @NotNull String group, @NotNull String cronExpression
            , @NotNull JOD_DATA jobData, @NotNull Callable<RETURN_VALUE> callable);

    String getName();

    String getGroup();

    String getCronExpression();

    Date getStartTime();

    Date getStopTime();

    JOD_DATA getJobData(Class<JOD_DATA> c);

    void setMaxStoredJobState(int max);



}
