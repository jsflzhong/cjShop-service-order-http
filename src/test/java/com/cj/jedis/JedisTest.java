package com.cj.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cj.core.utils.JedisClient;

public class JedisTest {
	
	//使用spring容器,从spring容器中获得对象,再连接Redis.
	@Test
	public void testJedisClientSpring() throws Exception {
		//创建一个spring容器,加载spring/下的所有以applicationContext-开头的配置文件.
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//从容器中获得JedisClient对象
		//这样拿的话,IOC那边配置的是单机的话,这里拿的就是单机的. 那边是集群的话,这里拿的就是集群的.
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//jedisClient操作redis
		//往Redis单机版那边,设置了一个key!
		jedisClient.set("cliet1", "1000");
		String string = jedisClient.get("cliet1");
		System.out.println(string);
	}
}
