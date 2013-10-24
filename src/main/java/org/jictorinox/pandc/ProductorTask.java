package org.jictorinox.pandc;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-12
 * Time: 上午10:23
 *
 * @auth gaohongtao
 */
public class ProductorTask<GOODS> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ProductorTask.class);
    private final int index;
    private final Channel<GOODS> channel;
    private final Productor<GOODS> productor;
    private final String ndc;

    public ProductorTask(int index, Productor<GOODS> productor, Channel<GOODS> channel, String ndc) {
        this.index = index;
        this.channel = channel;
        this.productor = productor;
        this.ndc = ndc;

    }

    private static void waitNormal() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void run() {
        NDC.push(ndc + "-ProductorTask-" + index);
        boolean isSubmitTask = false;
        try {
            while (channel.isOpen()) {
                if (channel.canSupply()) {
                    List<GOODS> goods = productor.product();
                    if (goods != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("提交产品{}个", goods.size());
                        }
                        for (GOODS good : goods) {
                            channel.supply(good);
                        }

                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("不再提供商品");
                        }
                        //返回null说明没有要处理的,而且不想继续
                        channel.close();
                    }
                    Thread.yield();

                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("超市货架满,等待");
                    }
                    waitNormal();
                }
            }
        } catch (Exception e) {
            log.error("出现异常", e);
            isSubmitTask = productor.exceptionAndIsContinue(e);
        } finally {
            productor.exitHook();
            if (log.isDebugEnabled()) {
                log.debug("停止生产");
            }
            NDC.remove();
            if (isSubmitTask) {
                if (log.isDebugEnabled()) {
                    log.error("重新启动");
                }
                channel.submitTask(this);
            }
        }
    }
}
