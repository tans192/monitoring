package com.hyaroma.blog.jiankong;

import java.util.Calendar;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 邮件参数类封装
 * @author wstv
 *
 */
public class MailUtil {
	private Set<String> toSet;
	/**
	 * 邮件主机HOST
	 */
	private String mailHost;
	/**
	 * 发件人邮箱	
	 */
	private String fromEmail;
	/**
	 * 发件人密码
	 */
	private String fromPassword;
	
	private static MailUtil mailUtil;
	
	public MailUtil(){
		toSet = new HashSet<String>();
		ResourceBundle bundle = ResourceBundle.getBundle("conf");
		String emails = bundle.getString("emails");
		System.out.println(emails);
		for(String email : emails.split(",")){
			toSet.add(email);
		}
		mailHost = bundle.getString("mailHost");
		fromEmail = bundle.getString("fromEmail");
		fromPassword = bundle.getString("fromPassword");
	}
	
	
	public static MailUtil getInstance(){
		if (mailUtil ==null) {
			return new MailUtil();
		}else{
			return mailUtil;
		}
	}
	
	
	public Set<String> getToSet() {
		return toSet;
	}


	public void setToSet(Set<String> toSet) {
		this.toSet = toSet;
	}


	public String getMailHost() {
		return mailHost;
	}


	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}


	public String getFromEmail() {
		return fromEmail;
	}


	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}


	public String getFromPassword() {
		return fromPassword;
	}


	public void setFromPassword(String fromPassword) {
		this.fromPassword = fromPassword;
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

	

	/**
	 * 发送短信
	 * 
	 * @param title 标题
	 * 
	 * @param text 内容
	 * 
	 * @time 2012-8-30
	 */
	public  void sendEmail(String title, String text) {
		SendMail sendMail = new SendMail(getMailHost(),getFromEmail(), getFromPassword(), getToSet(), getFromEmail(), title, "\n提示：\n时间：[" + getcurTime() + "]\n" + text);
		boolean result = sendMail.sendMail();
		System.out.println(getcurTime() + "=======" + toSet + "=========" + result);
	}
}
