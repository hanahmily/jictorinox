package org.jictorinox.cron;

/**
 * 能力框架
 * <p/>
 * Date: 13-10-11
 * Time: 上午11:00
 *
 * @auth gaohongtao
 */
public class CronTabException extends OperateDenyException {
    public CronTabException(String message, Throwable cause) {
        super("控制台操作失败->" + message, cause);
    }
}
