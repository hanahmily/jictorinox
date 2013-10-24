package org.jictorinox.util;

import jodd.util.StringPool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 能力框架
 * <p/>
 * Date: 13-9-25
 * Time: 上午10:22
 *
 * @auth gaohongtao
 */
public class StringUtil extends jodd.util.StringUtil {

    /**
     * 进行MD5计算
     *
     * @param str 输入字符串
     * @return MD5加密后的字符串,大写
     */
    public static String md5(String str) {
        MessageDigest alga = null;
        try {
            alga = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        alga.update(str.getBytes());
        byte[] digest = alga.digest();
        return byte2hex(digest);
    }

    /**
     * 将二进制数组转换为大写字母字符串
     *
     * @param b 需要转换的数字
     * @return 大写字符串
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (byte aB : b) {
            String stmp = Integer.toHexString(aB & 0xFF);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }


    /**
     * 将{@link java.sql.PreparedStatement} 中使用的参数占位符 <code>?</code> 替换为真正的参数.
     * <p>
     * <code>select * from a where t = ? </code> 参数为 3
     * </p>
     * 转换为
     * <p>
     * <code>select * from a where t = 3 </code>
     * </p>
     *
     * @param sql 原始的sql语句
     * @param param 需要替换占位符的参数
     * @return 真正执行的sql语句
     */
    public static String getPrepareStatmentSql(String sql, Object... param) {
        for (Object s : param) {
            sql = jodd.util.StringUtil.replaceFirst(sql, StringPool.QUESTION_MARK, s.toString());
        }
        return sql;
    }
}
