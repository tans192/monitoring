package com.hyaroma.blog.jiankong;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 发送邮件类
 * @author wstv
 *
 */
@SuppressWarnings("all")
public class SendMail {

	/**
	 * smtp主机
	 */
	private String host = "";
	/**
	 * 收件人(集合)
	 */
	private Set<String> toAddress;

	/**
	 * 发件人
	 */
	private String from = "";
	/**
	 * 发送者用户名
	 */
	private String username = "";
	/**
	 * 发送者密码
	 */
	private String password = "";
	/**
	 * 附件文件名
	 */
	private String filename = "";
	/**
	 * 邮件主题
	 */
	private String subject = "";
	/**
	 * 邮件正文
	 */
	private String content = "";
	/**
	 * 附件文件集合
	 */
	private Vector file = new Vector();

	public SendMail() {
	}

	public SendMail(String host, String userName, String password, Set<String> toAddress, String from, String subject, String content) {
		this.host = host;
		this.username = userName;
		this.password = password;
		this.toAddress = toAddress;
		this.subject = subject;
		this.content = content;
		this.from = from;
	}

	public String getHost() {
		return host;
	}

	public Set<String> getToAddress() {
		return toAddress;
	}

	public String getFrom() {
		return from;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFilename() {
		return filename;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public Vector getFile() {
		return file;
	}

	/**
	 * 发送Email
	 */
	public boolean sendMail() {

		// 构造mail session
		Properties props = System.getProperties();
		props.put("mail.smtp.host",host);
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");//设置发送邮件使用的协议
		props.setProperty("mail.smtp.socketFactory.port", "465"); //使用SMTPS协议465端口 
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");//SSL证书Socket工厂 
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// 构造MimeMessage 并设定基本的值
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));

			InternetAddress[] address = new InternetAddress[toAddress.size()];
			int k = 0;
			for (String emailAdd : toAddress) {
				address[k++] = new InternetAddress(emailAdd);
			}
			msg.setRecipients(Message.RecipientType.TO, address);
			// subject = transferChinese(subject);
			msg.setSubject(subject);

			// 构造Multipart
			Multipart mp = new MimeMultipart();

			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setText(content);
			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);

			// 向Multipart添加附件
			Enumeration efile = file.elements();
			while (efile.hasMoreElements()) {

				MimeBodyPart mbpFile = new MimeBodyPart();
				filename = efile.nextElement().toString();
				FileDataSource fds = new FileDataSource(filename);
				mbpFile.setDataHandler(new DataHandler(fds));
				mbpFile.setFileName(MimeUtility.encodeText(fds.getName()));
				// System.out.println(mbpFile.getFileName());
				// 向MimeMessage添加（Multipart代表附件）
				mp.addBodyPart(mbpFile);
			}

			file.removeAllElements();
			// 向Multipart添加MimeMessage
			msg.setContent(mp);
			msg.setSentDate(new Date());
			// 发送邮件
			Transport.send(msg);
		} catch (MessagingException mex) {
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
