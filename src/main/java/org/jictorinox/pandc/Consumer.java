package org.jictorinox.pandc;

import java.util.List;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-11
 * Time: 上午10:43
 *
 * @auth gaohongtao
 */
public interface Consumer<GOODS> extends MultiThread {
    /**
     * 消费鞥里
     * @param goods
     * @return  true 能继续i消费,false就理解停止整个生产和消费
     */
    boolean consume(List<GOODS> goods);

    /**
     *
     * @return 每次最大的消费能力
     */
    int capacity();
}
