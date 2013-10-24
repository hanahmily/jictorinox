package org.jictorinox.pandc;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * 超市缓冲区
 * <p/>
 * Date: 13-9-11
 * Time: 上午10:45
 *
 * @auth gaohongtao
 */
public class SuperMarket<GOODS extends Comparable> implements Channel<GOODS> {

    private static final Logger log = LoggerFactory.getLogger(SuperMarket.class);

    public static SuperMarket getInstance(Productor p, Consumer c, int cap) {
        return new SuperMarket(p, c, cap);
    }

    private final ExecutorService productor;
    private final ExecutorService consumer;
    private PriorityBlockingQueue<GOODS> shelf;

    private final int cap;
    private final Productor<GOODS> p;
    private final Consumer<GOODS> c;

    private final static int WAIT_SHUTDOWN_MILLISECOND = 200;

    private SuperMarket(Productor<GOODS> p, Consumer<GOODS> c, int cap) {
        if (p == null || c == null)
            throw new NullPointerException();
        if (cap < 1 || p.getConcurrentThreadNum() < 1 || c.getConcurrentThreadNum() < 1)
            throw new InvalidParameterException();
        this.cap = cap;
        this.p = p;
        this.c = c;
        shelf = new PriorityBlockingQueue<>(cap);
        productor = Executors.newCachedThreadPool();
        consumer = Executors.newCachedThreadPool();
    }

    private static void waitNormal() {
        try {
            Thread.sleep(WAIT_SHUTDOWN_MILLISECOND);
        } catch (InterruptedException e) {
        }
    }


    public void open() {
        int count = 0;
        try {
            for (int i = 0; i < c.getConcurrentThreadNum(); i++) {
                if (!consumer.isShutdown()) {
                    consumer.submit(new ConsumerTask<>(i, c, this, NDC.peek()));
                    count++;
                }
            }
        } catch (RejectedExecutionException e) {
            log.warn("执行器已经关闭,停止添加任务");
        }
        log.info("一共产生消费者 {} {} 个", c.getClass().getName(), count);
        count = 0;
        waitNormal();

        try {
            for (int i = 0; i < p.getConcurrentThreadNum(); i++) {
                productor.submit(new ProductorTask<>(i, p, this, NDC.peek()));
                count++;
            }
        } catch (RejectedExecutionException e) {
            log.warn("执行器已经关闭,停止添加任务");
        }

        log.info("一共产生生产者 {} {} 个", p.getClass().getName(), count);

        log.info("开始等待超市营业结束");
        while (!productor.isTerminated() || !consumer.isTerminated()) {
            waitNormal();
        }
        log.info("生产者与消费者已经完成,超市营业结束");
    }

    @Override
    public boolean canSupply() {
        return shelf.size() < cap;
    }

    @Override
    public void supply(GOODS goods) {
        shelf.offer(goods);
    }

    @Override
    public GOODS get() {
        return shelf.poll();
    }

    @Override
    public boolean isOpen() {
        return !(productor.isShutdown() && consumer.isShutdown());
    }

    @Override
    public <T extends Runnable> void submitTask(T task) {
        if (task instanceof ProductorTask)
            productor.submit(task);
        else if (task instanceof ConsumerTask)
            consumer.submit(task);
    }

    public void close() {

        log.info("开始结束超市营业");
        try {
            productor.shutdown();
        } catch (Exception e) {
            log.error("生产者关闭失败", e);
            try {
                productor.shutdownNow();
            } catch (Exception e1) {
                log.error("生产者强制关闭失败", e);
            }
        }

        try {
            consumer.shutdown();
        } catch (Exception e) {
            log.error("消费者关闭失败", e);
            try {
                consumer.shutdownNow();
            } catch (Exception e1) {
                log.error("消费者强制关闭失败", e);
            }
        }

        log.info("发送超市营业结束信息给生产者和消费者");
    }

}
