package com.drj.redis;

import static java.lang.System.out;

import java.util.List;

import javax.swing.JEditorPane;

import redis.clients.jedis.Jedis;

/**
 * 
 * @ClassName: MyRedis
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: drj
 * @date: 2018年10月26日 下午5:59:24
 * 
 * @Copyright: 2018
 *
 */
public class MyRedis {
	private static Jedis jedis = RedisDB.getJedis();
	public static String key = "IPPool";

	// 将ip信息保存在Redis列表中
	public static void setIPToList(List<IPMessage> ipMessages) {
		for (IPMessage ipMessage : ipMessages) {
			// 首先将ipMessage进行序列化
			byte[] bytes = SerializeUtil.serialize(ipMessage);

			jedis.rpush("IPPool".getBytes(), bytes);
		}
		System.out.println(jedis.llen("IPPool"));
	}

	// 将Redis中保存的对象进行反序列化
	public static IPMessage getIPByList() {
		int rand = (int) (Math.random() * jedis.llen("IPPool"));
		System.err.println(rand);
		Object o = SerializeUtil.unserialize(jedis.lindex("IPPool".getBytes(), rand));
		if (o instanceof IPMessage) {
			return (IPMessage) o;
		} else {
			out.println("不是IPMessage的一个实例~");
			return null;
		}
	}

	public static void deleteByIndex(int index) {
		jedis.lrem(key.getBytes(), index, jedis.lindex(key.getBytes(), index));
	}

	public void deleteKey(String key) {
		jedis.del(key);
	}

	public void close() {
		RedisDB.close(jedis);
	}

	public static void main(String[] args) throws InterruptedException {
		jedis.del("token");
		jedis.setex("token", 1, "789789");
		System.out.println(jedis.get("token"));
		Thread.sleep(1000);
		System.out.println(jedis.get("token"));
		jedis.setex("token", 1, "666");
		System.out.println(jedis.get("token"));
		Thread.sleep(100);
		System.out.println(jedis.get("token"));
	}
}
