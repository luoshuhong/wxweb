package com.lsh.wxweb.common;


/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.lsh.wxweb.model.ReceiveXmlMsg;

/**
 * XMLParse class
 *
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParse {
	/**
	 * 提取出xml数据包中的加密消息
	 * @param xmltext 待提取的xml字符串
	 * @return 提取出的加密消息字符串
	 */
	public static ReceiveXmlMsg extract(String xmltext)  {
		ReceiveXmlMsg model = new ReceiveXmlMsg();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList toUserName = root.getElementsByTagName("ToUserName");
			NodeList fromUserName = root.getElementsByTagName("FromUserName");
			NodeList createTime = root.getElementsByTagName("CreateTime");
			NodeList msgType = root.getElementsByTagName("MsgType");
			NodeList content = root.getElementsByTagName("Content");
			NodeList msgId = root.getElementsByTagName("MsgId");
			model.setContent(content.item(0).getTextContent());
			model.setToUserName(toUserName.item(0).getTextContent());
			model.setFromUserName(fromUserName.item(0).getTextContent());
			model.setCreateTime(createTime.item(0).getTextContent());
			model.setMsgType(msgType.item(0).getTextContent());
			model.setMsgId(msgId.item(0).getTextContent());
//			model.setToUserName(new String(toUserName.item(0).getTextContent().getBytes(),"utf-8"));
//			model.setFromUserName(new String(fromUserName.item(0).getTextContent().getBytes(),"utf-8"));
//			model.setCreateTime(new String(createTime.item(0).getTextContent().getBytes(),"utf-8"));
//			model.setMsgType(new String(msgType.item(0).getTextContent().getBytes(),"utf-8"));
//			model.setMsgId(new String(msgId.item(0).getTextContent().getBytes(),"utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String wrapText(ReceiveXmlMsg model) {
		String format = "<xml>\n"
				+ "<ToUserName><![CDATA[%1$s]]></ToUserName>\n"
				+ "<FromUserName><![CDATA[%2$s]]></FromUserName>"
				+ "<CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n"
				+ "<MsgType><![CDATA[text]]></MsgType>\n"
				+ "<Content><![CDATA[%3$s]]></Content>\n"
				+ "</xml>";
		return String.format(format, model.getToUserName(), model.getFromUserName(), model.getContent());
	}
	
	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String wrapText(String toUserName, String fromUserName, String context) {
		String format = "<xml>\n"
				+ "<ToUserName><![CDATA[%1$s]]></ToUserName>\n"
				+ "<FromUserName><![CDATA[%1$s]]></FromUserName>"
				+ "<CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n"
				+ "<MsgType><![CDATA[text]]></MsgType>\n"
				+ "<Content><![CDATA[%1$s]]></Content>\n"
				+ "</xml>";
		return String.format(format, toUserName, fromUserName, context);
	}
	
	/**
	 * 生成xml消息
	 * @param encrypt 加密后的消息密文
	 * @param signature 安全签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @return 生成的xml字符串
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {
		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
				+ "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
}
