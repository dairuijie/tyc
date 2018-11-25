package com.drj.redis;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName: RedisTest
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: drj
 * @date: 2018年10月26日 下午5:59:08
 * 
 * @Copyright: 2018
 *
 */
public class RedisTest {
    public static void main(String[] args) {
        System.out.println(RedisDB.getJedis());
        List<IPMessage> list = new ArrayList<IPMessage>();
        IPMessage ipPool = null;
        for (int i = 0; i < 10; i++) {
            ipPool = new IPMessage();
            ipPool.setIPAddress("123" + i);
            ipPool.setIPPort("890" + i);
            list.add(ipPool);
        }
        //MyRedis.setIPToList(list);
        IPMessage ip = MyRedis.getIPByList();
        System.out.println(ip.getIPAddress() +"===="+ ip.getIPPort());
    }
}
