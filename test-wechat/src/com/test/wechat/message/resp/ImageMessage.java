package com.test.wechat.message.resp;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageMessage extends BaseMessage {
	
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
	
	public static void main(String[] args) throws JAXBException {
		ImageMessage i = new ImageMessage();
		Image image2 = new Image();
		image2.setMediaId("11");
		i.setImage(image2);
		i.setFromUserName("1");
		JAXBContext context = JAXBContext.newInstance(ImageMessage.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(i, System.out);
		String s = "<xml><createTime>0</createTime><fromUserName>1</fromUserName><Image><mediaId>11</mediaId></Image></xml>";
		Unmarshaller um = context.createUnmarshaller();
		ImageMessage i2 = (ImageMessage) um.unmarshal(new ByteArrayInputStream(s.getBytes()));
		System.out.println("---");
		System.out.println(i2.getImage().getMediaId());
	}

}
