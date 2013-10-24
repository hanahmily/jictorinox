package org.jictorinox.pandc;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-12
 * Time: 上午11:13
 *
 * @auth gaohongtao
 */
public class ConsumerTask<GOODS> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ConsumerTask.class);
    private final int index;
    private final Channel<GOODS> channel;
    private final Consumer<GOODS> consumer;
    private final String ndc;

    public ConsumerTask(int index, Consumer<GOODS> consumer, Channel<GOODS> channel, String ndc) {
        this.index = index;
        this.channel = channel;
        this.consumer = consumer;
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
        NDC.push(ndc + "-ConsumerTask-" + index);
        boolean isSubmitTask = false;
        try {
            while (true) {
                List<GOODS> goods = new LinkedList<>();
                for (int j = 0; j < consumer.capacity(); j++) {
                    GOODS good = channel.get();
                    if (good == null) {
                        break;
                    } else {
                        goods.add(good);
                    }
                }
                if (goods.size() > 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("消费 {} 个商品", goods.size());
                    }
                    if (!consumer.consume(goods)) {
                        if (log.isDebugEnabled()) {
                            log.debug("不再进行消費");
                        }
                        channel.close();
                    }
                    Thread.yield();
                } else {
                    if (!channel.isOpen())
                        break;
                    waitNormal();
                }
            }
        } catch (Exception e) {
            log.error("出现异常", e);
            isSubmitTask = consumer.exceptionAndIsContinue(e);
        } finally {
            consumer.exitHook();
            if (log.isDebugEnabled()) {
                log.debug("停止工作");
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
