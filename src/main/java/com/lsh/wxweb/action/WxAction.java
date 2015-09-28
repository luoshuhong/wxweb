package com.lsh.wxweb.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.lsh.wxweb.common.Constants;
import com.lsh.wxweb.common.EncryptUtils;
import com.lsh.wxweb.common.HttpUtils;
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
//		try {
//			this.getRequest().setCharacterEncoding("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		this.getResponse().setCharacterEncoding("UTF-8");
		
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
		//下面与发送消息有关
//		String encrypt_type = this.getRequest().getParameter("encrypt_type");
//		String msg_signature = this.getRequest().getParameter("msg_signature");
//		String signature = this.getRequest().getParameter("signature");
//		String timestamp = this.getRequest().getParameter("timestamp");
//		String nonce = this.getRequest().getParameter("nonce");
//		String echostr = this.getRequest().getParameter("echostr");
		
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
			
			if ("你好".equals(revMsg.getContent())) {
				System.out.println("---if: 你好！--" + revMsg.getContent());
			} else {
				System.out.println("---else: 你好！--" + revMsg.getContent());
			}
			
			ReceiveXmlMsg recMsg = new ReceiveXmlMsg();
//			recMsg.setContent("你好" + new String(revMsg.getContent().getBytes("utf-8"),"ISO-8859-1"));
			recMsg.setContent("你好" + revMsg.getContent());
			recMsg.setFromUserName(revMsg.getToUserName());
			recMsg.setToUserName(revMsg.getFromUserName());
			String revXmlText = XMLParse.wrapText(recMsg);
			// 回复
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
	
	/*********************************************************/
	/**
	 * 测试通过微信浏览器打开获取用户信息
	 */
	public void getUserInfo() {
		String appid = "wx7da410e7b5d045fe"; // 微信公众号
		String secret = "296133f3ae1c0fb26daff3596014ec6c";// 应用密钥
        
		String channelId = this.getRequest().getParameter("code");
		String code = this.getRequest().getParameter("state");
		System.out.println("method=getUserInfo,step=start,code=" + code + ",cId=" + channelId);

		code = code == null ? "null" : code;
		channelId = channelId == null ? "null" : channelId;
		
		//通过code获取openid
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		url.replace("APPID", appid);
		url.replace("SECRET", secret);
		url.replace("CODE", code);
		try {
			System.out.println("method=getUserInfo,step=getOpenId start");
			String result = HttpUtils.doHttpGet(url);
			System.out.println("method=getUserInfo,step=getOpenId end,result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//重定向
		try {
			this.getResponse().sendRedirect("http://www.zhe800.com/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLEncoder.encode("http://lovinging.duapp.com/wx/getUserInfo"));
//		https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7da410e7b5d045fe&redirect_uri=http%3A%2F%2Flovinging.duapp.com%2Fwx%2FgetUserInfo&response_type=code&scope=snsapi_base&state=zhilian#wechat_redirect
	}
}
