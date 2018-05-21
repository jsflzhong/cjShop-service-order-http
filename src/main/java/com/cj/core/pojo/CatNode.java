package com.cj.core.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 商品分类列表JSON数据中的第二层pojo.
 * @author cj
 */
public class CatNode {
	
	//fields...

	@JsonProperty("u") //本java对象,被转换成JSON数据时,JSON中的key的值,是"u",而不是"url"了.
	private String url;//这里为了见文知意,才写成url.
	
	@JsonProperty("n")
	private String name;
	
	@JsonProperty("i")
	private List items;


	//methods...

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getItems() {
		return items;
	}
	public void setItems(List items) {
		this.items = items;
	}
}
