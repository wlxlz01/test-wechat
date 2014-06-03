package com.test.wechat.button;

public class ClickButton  extends Button {
	
	private String type = BUTTON_TYPE_CLICK;
	
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
