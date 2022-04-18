package com.city.traffic.util;

import java.io.Serializable;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:39
 */
public class RequestContext  implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }
}
