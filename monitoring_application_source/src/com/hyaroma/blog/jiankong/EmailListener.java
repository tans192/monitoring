package com.hyaroma.blog.jiankong;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;

/**
 * 监听消息，发送邮件
 * 
 * 
 * 
 */
@SuppressWarnings("static-access")
public class EmailListener implements MessageListener {
	private JmsTemplate jmsTemplate;
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void onMessage(Message message) {
		try {
			TextMessage txtMsg = (TextMessage) message;
			String str = txtMsg.getText();
			System.out.println(MailUtil.getcurTime() + "=========" + str);
			if (str.contains("cpu")) {
				// 格式：ip~cpu~1
				String[] mess = str.split("~");
				if (Integer.parseInt(mess[2]) > 80) {
					String title = "服务器CPU告警";
					String text = mess[0] + "服务器CPU使用率超过" + mess[2] + "%，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("mem")) {
				// 格式:ip~mem~14
				String[] mess = str.split("~");
				if (Integer.parseInt(mess[2]) > 80) {
					String title = "服务器内存告警";
					String text = mess[0] + "服务器内存使用率超过" + mess[2] + "%，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("disk") && str.contains("mnt") == false) {
				// 格式：ip~disk~/dev/shm~0
				String[] mess = str.split("~");
				if (Integer.parseInt(mess[3]) > 80) {
					String title = "服务器磁盘告警";
					String text = mess[0] + "服务器，" + mess[2] + "目录磁盘使用率超过" + mess[3] + "%，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("tomcat")) {
				// 格式：ip~tomcat8010~1
				String[] mess = str.split("~");
				if (mess[2].equals("0")) {
					String title = "服务器Tomcat告警";
					String text = mess[0] +" "+ mess[1]+ "服务器端口异常，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("tomcat8080")) {
				// 格式：ip~tomcat8080~1
				String[] mess = str.split("~");
				if (mess[2].equals("0")) {
					String title = "服务器Tomcat告警";
					String text = mess[0] + "服务器tomcat8080端口异常，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("mysql3306")) {
				// 格式：ip~mysql3306~1
				String[] mess = str.split("~");
				if (mess[2].equals("0")) {
					String title = "mysql数据库告警";
					String text = mess[0] + "服务器mysql3306端口异常，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("netstat3306")) {
				// 格式：ip~netstat3306~5
				String[] mess = str.split("~");
				if (Integer.parseInt(mess[2]) > 400) {
					String title = "mysql数据库连接数告警";
					String text = mess[0] + "服务器，netstat3306端口连接数超过" + mess[2] + "，请立刻检查！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			} else if (str.contains("ok")) {
				// 格式：ip~ok
				String[] mess = str.split("~");
				if (mess[1].equals("ok")) {
					String title = "服务器每日巡检正常";
					String text =mess[0] + "服务器每天8点巡检，目前状态正常！";
					MailUtil.getInstance().sendEmail(title, text);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}



	
	public static void main(String[] args) {
		MailUtil.getInstance().sendEmail("test","test");
	}

}
