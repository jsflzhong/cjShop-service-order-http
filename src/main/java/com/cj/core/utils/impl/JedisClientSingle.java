package com.cj.core.utils.impl;

import javax.annotation.Resource;

import com.cj.core.utils.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClientSingle implements JedisClient {
	
	@Resource
	private JedisPool jedisPool;
	
	@Override
	public String set(String key, String value) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		//调用jedis的Set方法,设置一个key.	
		String result = jedis.set(key, value);
		//永远需要关闭jedis.
		jedis.close();
		//返回结果.
		return result;
	}

	@Override
	public String get(String key) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String item, String value) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(key, item, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String item) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(key, item);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long decr(String key) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.decr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int second) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		//设置指定key的,指定过期时间.
		Long result = jedis.expire(key, second);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		//判断一下这个key还有多久过期.
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}
	//删除一个指定key的数据
	@Override
	public Long hdel(String key, String item) {
		//从上面注入的连接池里,获得一个jedis链接.
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(key, item);
		jedis.close();
		return result;
	}

}
