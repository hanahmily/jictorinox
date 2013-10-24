package org.jictorinox.conf;

import jodd.exception.UncheckedException;
import jodd.util.StringPool;
import jodd.util.StringUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * 配置文件加载接口,该接口描述了一般配置文件加载的过程.
 * 该过程包括配置文件载入:接口提供了两种载入方式,load()方法通过文件系统路径进行载入
 */
abstract class Configuration<C extends Configuration<C>> {

    protected final Reader fileReader;

    protected Configuration(final String confPath) {
        if (StringUtil.isBlank(confPath))
            throw new NullPointerException();
        try {
            fileReader = new FileReader(confPath + StringPool.SLASH + getFileName());
        } catch (FileNotFoundException e) {
            throw new UncheckedException(e);

        }
    }

    abstract C load();

    protected abstract String getFileName();

    protected abstract C init() throws InitialException;
}
