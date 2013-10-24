package org.jictorinox.cron;

/**
 * 所有进行操作被阻止,调用方应处理该一场.
 * 可以等待特定条件后再重试.或者直接结束调用
 * <p/>
 * Date: 13-10-10
 * Time: 下午3:25
 *
 * @auth gaohongtao
 */
public abstract class OperateDenyException extends Exception {
    public OperateDenyException(String message, Throwable cause) {
        super("Cron操作失败->" + message, cause);
    }

    public OperateDenyException(Throwable cause) {
        super(cause);
    }
}
