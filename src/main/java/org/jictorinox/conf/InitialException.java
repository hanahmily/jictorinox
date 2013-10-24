package org.jictorinox.conf;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-27
 * Time: 上午10:25
 *
 * @auth gaohongtao
 */
public class InitialException extends Exception {
    public InitialException(String message) {
        super("初始化异常:" + message);
    }
}
