package com.example.domaincheck.service.check.impl;

import com.example.domaincheck.service.check.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class DomainServiceImpl implements DomainService {
    @Override
    public boolean checkDomain(String f5ip, String host, boolean is_http, int f5_code) {
        try {
            // 每隔一秒发送一次请求
            Thread.sleep(1000);
            HttpURLConnection connection = getHttpURLConnection(f5ip, host, is_http);
            int responseCode = connection.getResponseCode();
            if (responseCode == f5_code) {
                return true;
            } else {
                log.error("域名:{},host:{},f5_code:{} is not match, response code is {}", host, f5ip, f5_code, responseCode);
                // TODO 告警, 发送邮件
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return false;
    }

    public static HttpURLConnection getHttpURLConnection(String f5ip, String host, boolean is_http) throws IOException {
        URL url;
        if (is_http) {
            url = new URL("http://" + f5ip);
        } else {
            url = new URL("https://" + f5ip);
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Host", host);
        // 连接超时时间5秒
        connection.setConnectTimeout(5000);
        // 读取超时时间5秒
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        return connection;
    }
}
