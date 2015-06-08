package com.lsh.wxweb.action;

import java.io.IOException;
import java.net.MalformedURLException;

import net.sf.json.JSONObject;

import com.lsh.wxweb.common.Constants;
import com.lsh.wxweb.common.HttpUtils;

public class Main {
	public static void main(String[] args) {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET;
		try {
			JSONObject job = JSONObject.fromObject(HttpUtils.doHttpGet(url));
			System.out.println(job.get("access_token"));
			System.out.println(job.get("expires_in"));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
