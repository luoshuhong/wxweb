package com.lsh.wxweb.action;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.struts2.interceptor.validation.JSONValidationInterceptor;

import com.lsh.wxweb.common.Constants;
import com.lsh.wxweb.common.HttpUtils;

public class Main {
	public static void main(String[] args) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET;
		try {
			System.out.println(HttpUtils.doHttpGet(url));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
