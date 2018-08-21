package com.lee.freebook.ippool.htmlparse;

import com.lee.freebook.ippool.httpbrowser.MyHttpResponse;
import com.lee.freebook.ippool.ipmodel.IpMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;



/**
 * Created by paranoid on 17-4-10.
 */

public class URLFecter {
    private static final Logger log = LoggerFactory.getLogger(URLFecter.class);

    //使用代理进行爬取
    public static boolean urlParse(String url, String ip, String port,
                                   List<IpMessage> IpMessages1) {
        //调用一个类使其返回html源码
        String html = MyHttpResponse.getHtml(url, ip, port);

        if(html != null) {
            //将html解析成DOM结构
            Document document = Jsoup.parse(html);

            //提取所需要的数据
            Elements trs = document.select("table[id=ip_list]").select("tbody").select("tr");
            packageIpMessage(trs,IpMessages1);
            return true;
        } else {
            log.info(ip+ ": " + port + " 代理不可用");

            return false;
        }
    }

    //使用本机IP爬取xici代理网站的第一页
    public static List<IpMessage> urlParse(List<IpMessage> IpMessages) {
        String url = "http://www.xicidaili.com/nn/1";
        String html = MyHttpResponse.getHtml(url);

        //将html解析成DOM结构
        Document document = Jsoup.parse(html);

        //提取所需要的数据
        Elements trs = document.select("table[id=ip_list]").select("tbody").select("tr");

        return packageIpMessage(trs,IpMessages);
    }
    public static List<IpMessage> packageIpMessage(Elements trs,List<IpMessage> IpMessages){
        for (int i = 1; i < trs.size(); i++) {
            IpMessage IpMessage = new IpMessage();
            String ipAddress = trs.get(i).select("td").get(1).text();
            String ipPort = trs.get(i).select("td").get(2).text();
            String ipType = trs.get(i).select("td").get(5).text();
            String ipSpeed = trs.get(i).select("td").get(6).select("div[class=bar]").
                    attr("title");

            IpMessage.setIPAddress(ipAddress);
            IpMessage.setIPPort(ipPort);
            IpMessage.setIPType(ipType);
            IpMessage.setIPSpeed(ipSpeed);

            IpMessages.add(IpMessage);
        }
        return IpMessages;
    }
}
