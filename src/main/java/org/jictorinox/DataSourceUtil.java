package org.jictorinox;

import org.apache.commons.dbcp.BasicDataSource;
import org.jictorinox.conf.PropertyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用dbcp实现基本的datasource
 *
 * @author gaohongtao
 */
public class DataSourceUtil extends PropertyConfiguration<DataSourceUtil> implements DataSourceUtilMBean {
    private static BasicDataSource ds;

    private static final Logger log = LoggerFactory.getLogger(DataSourceUtil.class);

    private static final ThreadLocal<Connection> threadDb = new ThreadLocal<>();

    private static DataSourceUtil factory;

    private static final Byte lock = 0x00;

    public static final String DRIVER = "driver";
    public static final String USERNAME = "username";
    public static final String PWD = "pwd";
    public static final String URL = "url";
    public static final String MAX_ACTIVE = "maxActive";
    public static final String MAX_IDLE = "maxIdle";
    public static final String MAX_WAIT = "maxWait";


    public DataSourceUtil(String confPath) {
        super(confPath);
    }

    @Override
    protected String getFileName() {
        return "oracle_jdbc.properties";
    }

    public DataSourceUtil init() {
        synchronized (lock) {
            if (factory == null) {
                factory = this;
            }
            if (ds != null && !ds.isClosed())
                return factory;
        }

        log.info("====================开始创建oracle 连接池====================");
        ds = new BasicDataSource();
        ds.setDriverClassName(factory.prop.getProperty(DRIVER));
        ds.setUsername(factory.prop.getProperty(USERNAME));
        ds.setPassword(factory.prop.getProperty(PWD));
        ds.setUrl(factory.prop.getProperty(URL));
        ds.setMaxActive(Integer.valueOf(factory.prop.getProperty(MAX_ACTIVE)));
        ds.setMaxIdle(Integer.valueOf(factory.prop.getProperty(MAX_IDLE)));
        ds.setMaxWait(Integer.valueOf(factory.prop.getProperty(MAX_WAIT)));
        ds.setTimeBetweenEvictionRunsMillis(1000L * 60L * 60L);
        ds.setMinEvictableIdleTimeMillis(1000L * 60L * 60L);
        ds.setMinIdle(10);
        ds.setValidationQuery("select 1 from dual");
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(false);
        ds.setTestWhileIdle(true);
        ds.setPoolPreparedStatements(true);
        ds.setMaxOpenPreparedStatements(10);
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(180);
        ds.setDefaultAutoCommit(false);
        ds.setDefaultReadOnly(false);

        log.info("====================完成创建oracle 连接池====================");
        return factory;
    }

    public static synchronized DataSource getDs() {
        if (ds == null || ds.isClosed()) {
            factory.init();
        }
        return ds;
    }

    public static List<Integer> getDsCondition() {
        List<Integer> list = new ArrayList<>();
        list.add(ds.getNumActive());
        list.add(ds.getNumIdle());
        return list;
    }

    public static Connection getConn() throws SQLException {
        Connection con = threadDb.get();
        if (con == null || con.isClosed()) {
            con = getDs().getConnection();
            con.setAutoCommit(false);
            threadDb.set(con);
        }
        return con;
    }

    public static void commit() throws SQLException {
        Connection con = threadDb.get();
        if (con != null && !con.isClosed()) {
            con.commit();
        }
    }

    public static void rollback() {
        Connection con = threadDb.get();
        try {
            if (con != null && !con.isClosed()) {
                con.rollback();
            }
        } catch (SQLException e) {
        }
    }

    public static void removeConn() {
        Connection con = threadDb.get();
        threadDb.remove();
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public int getNumActive() {
        if (ds != null) {
            return ds.getNumActive();
        }
        return -1;
    }

    @Override
    public int getNumIdle() {
        if (ds != null) {
            return ds.getNumIdle();
        }
        return -1;
    }
}
