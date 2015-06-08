package com.lsh.wxweb.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.lsh.wxweb.common.Constants;
import com.lsh.wxweb.common.EncryptUtils;
import com.lsh.wxweb.common.XMLParse;
import com.lsh.wxweb.model.ReceiveXmlMsg;

/**
 * 公众好验证
 * @author Luoshuhong
 * @Company 
 * 2015年6月6日
 *
 */
@SuppressWarnings("serial")
public class WxAction extends BasicAction {
	private Logger logger = Logger.getLogger("VerifyAction");
	
	/**
	 * wx 认证
	 * @return
	 */
	public void deal() {
		System.out.println("---method:" + this.getRequest().getMethod());
		if (dealMsg()) {
			return;
		}
		
		verify();  //验证
	}
	
	/**
	 * 发消息都是 POSt请求
	 * @return
	 */
	public boolean dealMsg() {
		InputStream input = null;
		BufferedReader reader = null;
		PrintWriter out = null;
		try {
			//GET 是验证消息 直接返回
			if ("GET".equalsIgnoreCase(this.getRequest().getMethod())) {
				return false;
			}
			input = this.getRequest().getInputStream();
			if (null == input) {
				return false;
			}
			
			reader = new BufferedReader(new InputStreamReader(this.getRequest().getInputStream()));
			String xmltext = "";
			String line = null;
			while (null != (line = reader.readLine())) {
				xmltext += line;
			}
			ReceiveXmlMsg revMsg = XMLParse.extract(xmltext);
			System.out.println("---revMsg:" + revMsg.getContent() + ";fromUser=" + revMsg.getFromUserName());
			
			ReceiveXmlMsg recMsg = new ReceiveXmlMsg();
			recMsg.setContent(revMsg.getContent());
			recMsg.setFromUserName(revMsg.getToUserName());
			recMsg.setToUserName(revMsg.getFromUserName());
			String revXmlText = XMLParse.wrapText(recMsg);
			//回复
			out = this.getResponse().getWriter();
			out.write(revXmlText);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != input) {
						input.close();
				} 
				if (null != reader) {
					reader.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	/**
	 * 配置初始化验证
	 */
	public void verify() {
		//获取请求的参数map
		try {
			System.out.println("Weixin Request Map:" + this.getRequest().getParameterMap().keySet());
			String signature = this.getRequest().getParameter("signature");
			String timestamp = this.getRequest().getParameter("timestamp");
			String nonce = this.getRequest().getParameter("nonce");
			String echostr = this.getRequest().getParameter("echostr");
			
			//下面与发送消息有关
//					String encrypt_type = this.getRequest().getParameter("encrypt_type");
//					String msg_signature = this.getRequest().getParameter("msg_signature");
			
			/**
			 * 加密/校验流程如下：
				1. 将token、timestamp、nonce三个参数进行字典序排序
				2. 将三个参数字符串拼接成一个字符串进行sha1加密
				3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
			 * */
			String[] arrayDic={timestamp,nonce,Constants.TOKEN};
            Arrays.sort(arrayDic);
            String newSig= "";
            for(int i=0; i<arrayDic.length; i++){
            	newSig += arrayDic[i];
            }
            System.out.println(("signature=" + signature + ";timestamp=" + timestamp 
            		+ ";nonce=" + nonce + ";echostr=" + echostr));
            String mysgin = EncryptUtils.SHA1(newSig);
            System.out.println("aftersign=" + mysgin + ";parmsign:" + signature);
            if(mysgin.trim().equals(signature.trim())){
            	System.out.println("---success.." + echostr);
//		            	String menuData = wxService.getConfigProperty().getProperty("wxMenuConfig");
//		            	wxService.createMenu(menuData);
            	this.getResponse().getWriter().write(echostr);
            }else{
            	System.out.println("---error--" + echostr);
            	this.getResponse().getWriter().write("check error");
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} 
	}
}
