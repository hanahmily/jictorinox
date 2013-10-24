package org.jictorinox;

import com.google.gson.annotations.Expose;
import org.jictorinox.conf.InitialException;
import org.jictorinox.conf.JsonConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

public class JedisFactory extends JsonConfiguration<JedisFactory, List<JedisFactory.Conf>> {

    public JedisFactory(String confPath) {
        super(confPath);
    }

    @Override
    protected String getFileName() {
        return "redis.json";
    }

    private static final Logger logger = LoggerFactory.getLogger(JedisFactory.class);

    private static JedisPool jedisPool;

    private static JedisFactory factory;

    private static final Byte lock = 0x00;

    public static Jedis get() throws InitialException {
        try {
            return jedisPool.getResource();
        } catch (Exception e) {
            factory.init();
            return jedisPool.getResource();
        }
    }

    public static void close(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }


    @Override
    public JedisFactory init() throws InitialException {
        synchronized (lock) {
            if (factory == null) {
                factory = this;
            }
            if (jedisPool != null) {
                try {
                    jedisPool.returnResource(jedisPool.getResource());
                    return factory;
                } catch (Exception e) {
                    logger.info("====================redis连接池失效====================");
                }

            }
        }

        logger.info("====================开始创建redis连接池====================");

        if (factory.jsonData == null || factory.jsonData.size() < 1) {
            throw new NullPointerException("您没有调用load()或者jedis.json 文件为空");
        }
        for (Conf conf : factory.jsonData) {
            try {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxActive(conf.maxActive);
                config.setMaxIdle(conf.maxIdle);
                config.setMaxWait(conf.maxWait);

                jedisPool = new JedisPool(config, conf.ip, conf.port);
                if (jedisPool != null) {
                    //测试链接池的链接和归还功能
                    jedisPool.returnResource(jedisPool.getResource());
                    logger.info("====================redis连接池创建成功：ip:"
                            + conf.ip + " port:"
                            + conf.port
                            + "====================");
                    return factory;
                }
            } catch (Exception e) {
                logger.error("获取redis链接失败", e);
            }
        }

        throw new InitialException("没有找到可用的redis链接");
    }

    public class Conf {
        @Expose
        private String ip;
        @Expose
        private int port;
        @Expose
        private int maxActive;
        @Expose
        private int maxIdle;
        @Expose
        private int maxWait;

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }
    }


}
