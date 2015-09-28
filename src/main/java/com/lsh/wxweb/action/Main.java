package com.lsh.wxweb.action;

import java.io.UnsupportedEncodingException;

import com.lsh.wxweb.common.Constants;

public class Main {
	public static void main(String[] args) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET;
//		try {
//			JSONObject job = JSONObject.fromObject(HttpUtils.doHttpGet(url));
//			System.out.println(job.get("access_token"));
//			System.out.println(job.get("expires_in"));
			
//			try {
//				System.out.println(new String("中文".getBytes("utf-8"),"utf-8"));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		String a = new String("abc");
		String b = "abc";
		
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
