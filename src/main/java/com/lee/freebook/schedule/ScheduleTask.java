package com.lee.freebook.schedule;

import com.lee.freebook.ippool.htmlparse.IPPool;
import com.lee.freebook.ippool.htmlparse.IPThread;
import com.lee.freebook.ippool.htmlparse.URLFecter;
import com.lee.freebook.ippool.ipfilter.IPFilter;
import com.lee.freebook.ippool.ipfilter.IPUtils;
import com.lee.freebook.ippool.ipmodel.IpMessage;
import com.lee.freebook.ippool.redis.MyRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleTask {
    private final Logger log = LoggerFactory.getLogger(ScheduleTask.class);
    MyRedis redis = new MyRedis();

    @Scheduled(fixedRate = 24*60*60*1000)
    public void executeIpCrawl(){
        //首先清空redis数据库中的key
        redis.deleteKey("IPPool");

        //存放爬取下来的ip信息
        List<IpMessage> IpMessages = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        //对创建的子线程进行收集
        List<Thread> threads = new ArrayList<>();

        //首先使用本机ip爬取xici代理网第一页
        IpMessages = URLFecter.urlParse(IpMessages);

        //对得到的IP进行筛选，将IP速度在两秒以内的并且类型是https的留下，其余删除
        IpMessages = IPFilter.Filter(IpMessages);

        //对拿到的ip进行质量检测，将质量不合格的ip在List里进行删除
        IPUtils.IPIsable(IpMessages);

        //构造种子url(4000条ip)
        for (int i = 2; i <= 41; i++) {
            urls.add("http://www.xicidaili.com/nn/" + i);
        }

        /**
         * 对urls进行解析并进行过滤,拿到所有目标IP(使用多线程)
         *
         * 基本思路是给每个线程分配自己的任务，在这个过程中List<IpMessage> IpMessages
         * 应该是共享变量，每个线程更新其中数据的时候应该注意线程安全
         */
        IPPool ipPool = new IPPool(IpMessages);
        for (int i = 0; i < 20; i++) {
            //给每个线程进行任务的分配
            Thread IPThread = new IPThread(urls.subList(i*2, i*2+2), ipPool);
            threads.add(IPThread);
            IPThread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(IpMessage IpMessage : IpMessages){
            log.info(IpMessage.getIPAddress());
            log.info(IpMessage.getIPPort());
            log.info(IpMessage.getIPType());
            log.info(IpMessage.getIPSpeed());
        }

        //将爬取下来的ip信息写进Redis数据库中(List集合)
        redis.setIPToList(IpMessages);

        //从redis数据库中随机拿出一个IP
        IpMessage IpMessage = redis.getIPByList();
        log.info(IpMessage.getIPAddress());
        log.info(IpMessage.getIPPort());
        redis.close();
    }
}
