package com.lsh.wxweb.common;

import java.io.IOException;
import java.net.MalformedURLException;

import net.sf.json.JSONObject;

/**
 * wx 接口相关调用工具类	
 * @author Luoshuhong
 * @Company 
 * 2015年6月7日
 *
 */
public class WxInterUtils {
	
	/**
	 * 获取access_token
	 * @return
	 */
	public static String getAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET;
		try {
			JSONObject job = JSONObject.fromObject(HttpUtils.doHttpGet(url));
			String accessToken = job.get("access_token").toString();
//			System.out.println(job.get("expires_in"));
			return accessToken;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取微信服务器ip地址
	 * @param accToken
	 * @return
	 */
	public static String getWxIp(String accToken) {
		String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + accToken;
		try {
			JSONObject job = JSONObject.fromObject(HttpUtils.doHttpGet(url));
			String ipList = job.get("ip_list").toString();
			return ipList;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
}
