package com.lsh.wxweb.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * ip处理工具类
 * @author Luoshuhong
 * @Company  
 * 2015年6月6日
 *
 */
public class IpUtil {
    
    private static Set<String> limitIps;
    
    static {
        initLimitIps();
    }
    
    /**
     * <p>Title: initLimitIps</p>
     * <p>Description: 初始化ip列表</p>
     */
    private static void initLimitIps() {
        try {
            if (limitIps == null) {
                limitIps = new HashSet<String>();
            }
            ResourceBundle bundle = ResourceBundle.getBundle("ip");
            Set<String> sets = bundle.keySet();
            for (String set : sets) {
                if (!StringUtils.isEmpty(set)) {
                    set = set.replaceAll("_", ".");
                    limitIps.add(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <p>Title: isInnerIp</p>
     * <p>Description: 判断ip是否在白名单之内</p>
     * @param ip ip
     * @return true 在白名单里 ;false 不在白名单中
     */
    public static boolean isInnerIp(String ip) {
        if (StringUtils.isEmpty(ip)){
            return false;
        }
        if ("127.0.0.1".equals(ip)) {
            return true;
        }
        if (limitIps == null || limitIps.size() == 0) {
            initLimitIps();
        }
        return limitIps.contains(ip);
    }
    
    /**
     * <p>Title: isInnerIp</p>
     * <p>Description: 判断ip是否在白名单之内</p>
     * @param ip ip
     * @return true 在白名单里 ;false 不在白名单中
     */
    public static boolean isInnerIp(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String ip = getRealIp(request);
        return isInnerIp(ip);
    }
    /**
     * @Title:
     * @Description: 获取请求IP地址
     * @param req 请求
     * @return String
     */
    public static String getRealIp(HttpServletRequest req)
    {

        String ip = req.getHeader("X-Real-IP");
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip.trim())) {
            ip = req.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip.trim())) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip.trim())) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        ip = ip == null ? "" : ip.trim();
        String[]arrip = ip.split(",");
        ip = "";
        for (int i = 0;i < arrip.length;i ++) {
            if (!"unknown".equalsIgnoreCase(arrip[i].trim())) {
                ip = arrip[i];
                break;
            }
        }
        return ip;
    }
    /**
    * @Title: getServerIp 
    * @Description: 获取服务器IP地址
    * @param eth 
    * @return String
     */
    private static String getServerIp(String eth) {
        String serverIp = null;
        try {
            Enumeration allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
                        .nextElement();
                if (eth.equalsIgnoreCase(netInterface.getName())) {
                    Enumeration addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = (InetAddress) addresses.nextElement();
                        if ((ip != null) && ((ip instanceof Inet4Address))) {
                            serverIp = ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            serverIp = null;
        }
        return serverIp;
    }
    /**
    * @Title: getServerPrivateIp 
    * @Description: 获取服务器IP
    * @param 
    * @return String
     */
    public static String getServerPrivateIp()
    {
        return getServerIp("eth0");
    }
}
