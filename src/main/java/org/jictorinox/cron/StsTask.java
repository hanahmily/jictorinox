package org.jictorinox.cron;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 能力框架
 * <p/>
 * Date: 13-10-17
 * Time: 下午6:03
 *
 * @auth gaohongtao
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public interface StsTask extends InterruptableJob {
}
