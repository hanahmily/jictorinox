package org.jictorinox.cron;

import com.sun.istack.internal.NotNull;
import org.quartz.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

/**
 * 标准的cron任务
 * <p/>
 * Date: 13-10-17
 * Time: 下午3:16
 *
 * @auth gaohongtao
 */
public class StsCronJob implements CronJob {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final JobDetail jobDetail;
    final CronTrigger trigger;
    private final JobBuilder jobBuilder = JobBuilder.newJob();
    private final TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

    public StsCronJob(@NotNull JobDetail jobDetail, @NotNull CronTrigger trigger) {
        this.jobDetail = jobDetail;
        this.trigger = trigger;
    }

    public StsCronJob(@NotNull String name, @NotNull String group, @NotNull String cronExpression
            , @NotNull Object jobData, @NotNull Class<StsTask> clazz) {
        this.withJobData(jobData);
        this.jobDetail = jobBuilder.ofType(clazz).withIdentity(new JobKey(name, group)).build();
        this.trigger = triggerBuilder.withIdentity(new TriggerKey("triggerOf" + name, group))
                .withSchedule(misfireHandle(MisfireInstruction.DO_NOTING, CronScheduleBuilder.cronSchedule(cronExpression))).build();
    }

    private <Object> CronJob withJobData(Object jobData) {
        Set<Field> fieldSet = getAllFields(jobData.getClass(), withAnnotation(JobData.class));
        JobDataMap map = new JobDataMap();
        for (Field field : fieldSet) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(jobData));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
        jobBuilder.setJobData(map);
        return this;
    }

    @Override
    public <TaskData> TaskData getJobData(@NotNull Class<TaskData> c) {
        Set<Field> fieldSet = getAllFields(c, withAnnotation(JobData.class));
        TaskData instance;
        try {
            instance = c.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        for (Field field : fieldSet) {
            field.setAccessible(true);
            try {
                field.set(instance, jobDetail.getJobDataMap().get(field.getName()));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return instance;
    }

    private CronScheduleBuilder misfireHandle(MisfireInstruction ins, CronScheduleBuilder builder) {
        switch (ins) {
            case FIRE_ONCE_NOW:
                builder.withMisfireHandlingInstructionFireAndProceed();
                break;
            case IGNORE:
                builder.withMisfireHandlingInstructionIgnoreMisfires();
                break;
            default:
                builder.withMisfireHandlingInstructionDoNothing();
        }
        return builder;
    }

    public abstract class TaskData {
        public String getName() {
            return jobDetail.getKey().getName();
        }

        public String getGroup() {
            return jobDetail.getKey().getName();
        }

        public String getCronExpression() {
            return trigger.getCronExpression();
        }

        public String getStartTime() {
            return DATE_FORMAT.format(trigger.getStartTime());
        }

        public String getEndTime() {
            return DATE_FORMAT.format(trigger.getEndTime());
        }

        public String getPreviousFireTime() {
            return DATE_FORMAT.format(trigger.getPreviousFireTime());
        }

        public String getNextFireTime() {
            return DATE_FORMAT.format(trigger.getNextFireTime());
        }

        public String getFinalFireTime() {
            return DATE_FORMAT.format(trigger.getFinalFireTime());
        }
    }
}
