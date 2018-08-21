package com.lee.freebook.ippool.ipfilter;


import com.lee.freebook.ippool.ipmodel.IpMessage;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;



/**
 * Created by paranoid on 17-4-21.
 * 测试此IP是否有效
 */

public class IPUtils {
    private static final Logger log = LoggerFactory.getLogger(IPUtils.class);
    public static void IPIsable(List<IpMessage> IpMessages1) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        for(int i = 0; i < IpMessages1.size(); i++) {
            String ip = IpMessages1.get(i).getIPAddress();
            String port = IpMessages1.get(i).getIPPort();

            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(5000).
                    setSocketTimeout(5000).build();
            HttpGet httpGet = new HttpGet("https://www.baidu.com");
            httpGet.setConfig(config);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;" +
                    "q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit" +
                    "/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

            try {
                response = httpClient.execute(httpGet);
            } catch (IOException e) {
                log.info("不可用代理已删除" + IpMessages1.get(i).getIPAddress()
                        + ": " + IpMessages1.get(i).getIPPort());
                IpMessages1.remove(IpMessages1.get(i));
                i--;
            }
        }

        try {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
