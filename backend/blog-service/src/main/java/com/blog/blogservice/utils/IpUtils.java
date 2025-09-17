package com.blog.blogservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * IP工具类
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String LOCAL_IP = "0:0:0:0:0:0:0:1";
    private static final int MAX_IP_LENGTH = 15;

    // 代理IP头信息
    private static final List<String> IP_HEADERS = Arrays.asList(
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    );

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = null;

        // 从代理IP头信息中获取IP
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                break;
            }
        }

        // 如果没有从代理头信息中获取到IP，则获取远程地址
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多IP情况（例如代理链），取第一个IP
        if (StringUtils.isNotEmpty(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 处理IPv6的localhost
        if (LOCAL_IP.equals(ip)) {
            ip = LOCALHOST;
        }

        return ip;
    }

    /**
     * 判断是否为内部IP
     */
    public static boolean isInternalIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }

        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isSiteLocalAddress() || address.isLoopbackAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 判断是否为IPv4地址
     */
    public static boolean isIPv4(String ip) {
        if (StringUtils.isEmpty(ip) || ip.length() > MAX_IP_LENGTH) {
            return false;
        }

        String ipv4Regex = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipv4Regex);
    }

    /**
     * 判断是否为IPv6地址
     */
    public static boolean isIPv6(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }

        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getHostAddress().equals(ip) && ip.contains(":");
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 获取本地IP地址
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return LOCALHOST;
        }
    }

    /**
     * 隐藏IP的一部分，用于隐私保护
     */
    public static String maskIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return ip;
        }

        if (isIPv4(ip)) {
            String[] parts = ip.split("\\.");
            if (parts.length == 4) {
                return parts[0] + "." + parts[1] + ".***.***";
            }
        } else if (isIPv6(ip)) {
            // 简单处理IPv6，隐藏后半部分
            if (ip.contains("::")) {
                return ip;
            }
            String[] parts = ip.split(":");
            if (parts.length > 4) {
                return String.join(":", Arrays.copyOfRange(parts, 0, 4)) + ":****:****:****:****";
            }
        }

        return ip;
    }
}