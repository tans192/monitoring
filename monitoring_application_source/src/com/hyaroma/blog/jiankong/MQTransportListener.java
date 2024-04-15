package com.hyaroma.blog.jiankong;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.activemq.transport.TransportListener;
import org.springframework.jms.core.JmsTemplate;

/**
 * 心跳检测mq服务器开启状态
 * 主要检测服务器是否开启 做相应的消息发送处理
 * @author wstv
 *
 */
public class MQTransportListener implements TransportListener {
	
	private Set<String> toSet;

	public MQTransportListener(){
		toSet = new HashSet<String>();
		ResourceBundle bundle = ResourceBundle.getBundle("conf");
		String emails = bundle.getString("emails");
		System.out.println(emails);
		for(String email : emails.split(",")){
			toSet.add(email);
		}
	}
	/**
	 * 对消息传输命令进行监控     
	 * @param arg0
	 */   
	@Override
	public void onCommand(Object arg0) {
	}
	/**
	 * 对监控到的异常进行触发     
	 * @param arg0
	 */ 
	@Override
	public void onException(IOException arg0) {
		sendEmail("test", "test",toSet);
		System.out.println("消息服务器连接错误");
	}
	/**
	 * 当failover时触发     
	 */  
	@Override
	public void transportInterupted() {
		System.out.println("transportInterupted -> 消息服务器连接发生中断......");
	}


	 /**     
	  * 监控到failover恢复后进行触发     
	 */    
	@Override
	public void transportResumed() {
		// TODO Auto-generated method stub
		System.out.println("transportResumed -> 消息服务器连接已恢复......");
	}
	
	/**
	 * 发送短信
	 * 
	 * @param title 标题
	 * 
	 * @param text 内容
	 * 
	 * @time 2012-8-30
	 */
	public static void sendEmail(String title, String text,Set<String> toSet) {
		SendMail sendMail = new SendMail("smtp.163.com", "18208308039@163.com", "199528", toSet, "18208308039@163.com", title, "\n提示：\n时间：[" + getcurTime() + "]\n" + text);
		boolean result = sendMail.sendMail();
	}
	

	// 时间戳 "年+月+日+时+分+秒+"
	public static String getcurTime() {
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String SECOND = cal.get(Calendar.SECOND) + "";
		StringBuffer buffer = new StringBuffer();
		buffer.append(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + SECOND);
		return buffer.toString().toLowerCase();
	}
}
