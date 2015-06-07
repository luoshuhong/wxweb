package com.lsh.wxweb.action;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.lsh.wxweb.common.Constants;
import com.lsh.wxweb.common.EncryptUtils;

/**
 * 公众好验证
 * @author Luoshuhong
 * @Company 
 * 2015年6月6日
 *
 */
@SuppressWarnings("serial")
public class VerifyAction extends BasicAction {
	private Logger logger = Logger.getLogger("VerifyAction");
	
	/**
	 * wx 认证
	 * @return
	 */
	public void verify() {
		//获取请求的参数map
		try {
			System.out.println("Weixin Request Map:" + this.getRequest().getParameterMap().keySet());
			String signature = this.getRequest().getParameter("signature");
			String timestamp = this.getRequest().getParameter("timestamp");
			String nonce = this.getRequest().getParameter("nonce");
			String echostr = this.getRequest().getParameter("echostr");
			
			if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp)
					|| StringUtils.isEmpty(nonce) || StringUtils.isEmpty(echostr)) {
				this.getResponse().getWriter().write("check error");
				return ;
			}
			/**
			 * 加密/校验流程如下：
				1. 将token、timestamp、nonce三个参数进行字典序排序
				2. 将三个参数字符串拼接成一个字符串进行sha1加密
				3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
			 * */
			String[] arrayDic={timestamp,nonce,Constants.TOKEN};
            Arrays.sort(arrayDic);
            String newSig="";
            for(int i=0; i<arrayDic.length; i++){
            	newSig += arrayDic[i];
            }
            System.out.println(("signature=" + signature + ";timestamp=" + timestamp 
            		+ ";nonce=" + nonce + ";echostr=" + echostr));
            String mysgin = EncryptUtils.SHA1(newSig);
            System.out.println("aftersign=" + mysgin + ";parmsign:" + signature);
            if(mysgin.trim().equals(signature.trim())){
            	System.out.println("---success.." + echostr);
//            	String menuData = wxService.getConfigProperty().getProperty("wxMenuConfig");
//            	wxService.createMenu(menuData);
            	this.getResponse().getWriter().write(echostr);
            }else{
            	System.out.println("---error--" + echostr);
            	this.getResponse().getWriter().write("check error");
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} 
//		return "success";
	}
}
