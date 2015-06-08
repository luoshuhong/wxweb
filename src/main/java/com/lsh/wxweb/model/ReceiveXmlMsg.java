package com.lsh.wxweb.model;

/**
 * 接收到的消息体
 * @author Luoshuhong
 * @Company 
 * 2015年6月7日
 *
 */
public class ReceiveXmlMsg {
//	<xml>
//	<ToUserName><![CDATA[gh_481a819a5075]]></ToUserName>
//	<FromUserName><![CDATA[oTi5buOP4pZ3C7fSPQAWJJGG-ovs]]></FromUserName>
//	<CreateTime>1433675074</CreateTime>
//	<MsgType><![CDATA[text]]></MsgType>
//	<Content><![CDATA[hello]]></Content>
//	<MsgId>6157587556126220640</MsgId>
//	</xml>
	private String toUserName = "";
	private String fromUserName = "";
	private String createTime = "";
	private String msgType = "";
	private String content = "";
	private String msgId = "";
	
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
