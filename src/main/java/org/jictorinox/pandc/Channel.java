package org.jictorinox.pandc;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-12
 * Time: 上午10:28
 *
 * @auth gaohongtao
 */
public interface Channel<GOODS> {

    /**
     * 是否可以供货
     * @return
     */
    boolean canSupply();

    /**
     * 供货
     * @param goods
     */
    void supply(GOODS goods);

    /**
     * 获取商品
     * @return
     */
    GOODS get();

    /**
     * 判断商场是不是开着的
     * @return
     */
    boolean isOpen();

    /**
     *
     * @param task
     * @param <T>
     */
    <T extends Runnable> void submitTask(T task);

    /**
     * 通知关闭
     */
    void close();


}
