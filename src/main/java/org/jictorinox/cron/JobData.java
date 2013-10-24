package org.jictorinox.cron;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 能力框架
 * <p/>
 * Date: 13-10-17
 * Time: 下午3:38
 *
 * @auth gaohongtao
 */
@Documented
@Nonnull(when= When.UNKNOWN)
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierNickname
public @interface JobData {
}
