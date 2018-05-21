package com.cj.core.utils;

/**
 * 链接Redis单机版和集群的接口.
 * @author 崔健
 * @date 2016年10月16日下午5:43:41
 */
public interface JedisClient {
	
	//往Redis数据库里设置值.
	public String set(String key, String value);
	//从Redis数据库里取值.
	public String get(String key);
	//哈希的set 
	//例如: hset hash1 key1 1  "hash1 key1"是key, 1是value.
	public Long hset(String key, String item, String value);
	//哈希的get
	//例如:hget hash1 key1   结果为"1". 就是上面最后的1.
	public String hget(String key, String item);
	//加1.
	public Long incr(String key);
	//减1.
	public Long decr(String key);
	//设置过期时间
	//例如: expire hash1 10  10秒后过期
	public Long expire(String key, int second);
	//判断一下这个key还有多久过期.
	public Long ttl(String key);
	//删除一个指定key的数据
	public Long hdel(String key,String item);
}