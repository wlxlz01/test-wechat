package com.test.wechat.message.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class QRCodeEvent extends BaseEvent {
	
	// qrscene_Ϊ�̶�ǰ׺������Ϊ��ά��Ĳ���ֵ������Ticket�����ڻ�ȡ��ά��ͼƬ
	private String EventKey;
	
	private String Ticket;

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

}
