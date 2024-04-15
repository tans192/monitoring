package com.hyaroma.blog.jiankong;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 这是程序入口,通过spring的配置文件启动监听器
 * 
 * @author chenxl
 * 
 */
public class AppListener {

	public static void main(String[] args) {
		ApplicationContext c = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}
}
