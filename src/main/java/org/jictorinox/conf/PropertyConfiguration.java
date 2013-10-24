package org.jictorinox.conf;

import jodd.exception.UncheckedException;

import java.io.IOException;
import java.util.Properties;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-4
 * Time: 下午4:55
 *
 * @auth gaohongtao
 */
public abstract class PropertyConfiguration<C extends PropertyConfiguration<C>> extends Configuration<C> {
    protected PropertyConfiguration(String confPath) {
        super(confPath);
    }

    protected Properties prop;

    @Override
    @SuppressWarnings("unchecked")
    public C load() {
        prop = new Properties();
        try {
            prop.load(this.fileReader);
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
        return (C) this;
    }
}
