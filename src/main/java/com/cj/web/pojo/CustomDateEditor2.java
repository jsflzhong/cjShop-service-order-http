package com.cj.web.pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 自定义的日期转换器
 * <p>Title:CustomDateEditor.java</p>
 * <p>Description:</p>
 * <p>Company: www.chinacoaltj.com</p>
 * @author 崔健
 * @date 2016年7月23日下午10:01:58
 * @version 1.0
 */
public class CustomDateEditor2 implements WebBindingInitializer{

	public void initBinder(WebDataBinder binder, WebRequest request) {
		//转换日期格式
		//如果页面传过来的是参数中的格式,那么就转换成Date格式.
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
				
		
	}
	
}
