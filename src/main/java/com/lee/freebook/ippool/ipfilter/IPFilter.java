package com.lee.freebook.ippool.ipfilter;

import com.lee.freebook.ippool.ipmodel.IpMessage;

import java.util.*;


/**
 * Created by paranoid on 17-4-14.
 * 对得到的IP进行筛选，将IP速度在两秒以内的并且类型是https的留下，其余删除
 */

public class IPFilter {
    //对IP进行过滤
    public static List<IpMessage> Filter(List<IpMessage> IpMessages1) {
        List<IpMessage> newIpMessages = new ArrayList<>();

        for (int i = 0; i < IpMessages1.size(); i++) {
            String ipType = IpMessages1.get(i).getIPType();
            String ipSpeed = IpMessages1.get(i).getIPSpeed();

            ipSpeed = ipSpeed.substring(0, ipSpeed.indexOf('秒'));
            double Speed = Double.parseDouble(ipSpeed);

            if (ipType.equals("HTTPS") && Speed <= 2.0) {
                newIpMessages.add(IpMessages1.get(i));
            }
        }

        return newIpMessages;
    }
}
