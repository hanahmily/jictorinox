package org.jictorinox;

/**
 * @author gaohongtao
 */
public class JedisAccessException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JedisAccessException(String s) {
        super(s);
    }

    public JedisAccessException(String s, Throwable e) {
        super(s, e);
    }

    public JedisAccessException(Throwable e) {
        super(e);
    }

}
