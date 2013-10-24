package org.jictorinox.pandc;

import java.util.List;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-11
 * Time: 上午10:37
 *
 * @auth gaohongtao
 */
public interface Productor<GOODS> extends MultiThread {

    /**
     * 向shelf添加货物
     *
     * @param
     * @return 如果还有货物需要添加
     */
    List<GOODS> product();


}
