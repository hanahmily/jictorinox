package org.jictorinox.conf;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.ParameterizedType;

/**
 *
 * <p/>
 * Date: 13-9-4
 * Time: 下午1:51
 *
 * @auth gaohongtao
 */
public abstract class JsonConfiguration<C extends JsonConfiguration<C, D>, D> extends Configuration<C> {
    protected JsonConfiguration(String confPath) {
        super(confPath);
    }

    protected D jsonData;

    @Override
    @SuppressWarnings(value = "unchecked")
    public C load() {
        GsonBuilder gb = new GsonBuilder();
        //另外一种写法TypeToken 的用法无法获取真正的类型
        jsonData = gb.excludeFieldsWithoutExposeAnnotation().create().fromJson(new JsonReader(this.fileReader),
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
        return (C) this;
    }

}
