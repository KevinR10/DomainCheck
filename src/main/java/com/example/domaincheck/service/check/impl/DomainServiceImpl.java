package com.example.domaincheck.service.check.impl;

import com.example.domaincheck.service.check.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import org.apache.http.HttpResponse;
import java.net.HttpURLConnection;

@Slf4j
@Service
public class DomainServiceImpl implements DomainService {
    @Override
    public boolean checkDomain(String f5ip, String host, boolean is_http, int f5_code) {
        try {
            // 每隔一秒发送一次请求
            Thread.sleep(1000);
            int responseCode = getHttpURLConnection(f5ip, host, is_http);
            if (responseCode == f5_code) {
                return true;
            } else {
                log.error("域名:{},host:{},f5_code:{} is not match, response code is {}", host, f5ip, f5_code, responseCode);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return false;
    }

    public int getHttpURLConnection(String f5ip, String host, boolean is_http) throws Exception {
        String uri;
        HttpResponse response;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(80000).setSocketTimeout(80000).build();
        if (is_http){
            uri = "http://" + f5ip;
        }else {
            uri = "https://" + f5ip;
        }

        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(HttpHeaders.HOST, host);
        httpGet.setConfig(config);
        response = client.execute(httpGet);
        return response.getStatusLine().getStatusCode();
    }
}
