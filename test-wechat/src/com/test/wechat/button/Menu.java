package com.test.wechat.button;

import com.google.gson.Gson;
import com.test.wechat.common.CommonUtil;

public class Menu {
	
	private Button[] button;

	public Button[] getButtons() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
	
	public String create() {
		String url =  "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", CommonUtil.getAccessToken());
		String content = new Gson().toJson(this);
		return CommonUtil.httpsPostReq(url, content);
	}
	
	public static String query() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", CommonUtil.getAccessToken());
		return CommonUtil.httpsGetReq(url);
	}
	
	public static String delete() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", CommonUtil.getAccessToken());
		return CommonUtil.httpsGetReq(url);
	}
	
}
