package com.test.wechat.common;

import java.util.ResourceBundle;

public abstract class WechatCfg {
	
	public static String TOKEN;
	
	public static String appID;
	
	public static String appsecret;
	
	static {
		ResourceBundle rb = ResourceBundle.getBundle("wechatCfg");
		TOKEN = rb.getString("token");
		appID = rb.getString("appID");
		appsecret = rb.getString("appsecret");
	}

}
