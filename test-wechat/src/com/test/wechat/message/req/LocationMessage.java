package com.test.wechat.message.req;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationMessage extends BaseMessage {
	
	private String Location_X;//����λ��γ��
	
	private String Location_Y;//����λ�þ���
	
	private String Scale;// ��ͼ���Ŵ�С
	
	private String Lable; //����λ����Ϣ

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLable() {
		return Lable;
	}

	public void setLable(String lable) {
		Lable = lable;
	}

}
