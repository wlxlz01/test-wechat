package com.test.wechat.message;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.test.wechat.common.WechatCfg;
import com.test.wechat.message.resp.BaseMessage;

public class MessageUtil {
	
	private static Logger log = Logger.getLogger(MessageUtil.class);
	
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	public static final String REQ_MESSAGE_TYPE_LINK = "link";
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	public static final String EVENT_TYPE_SCAN = "scan";
	public static final String EVENT_TYPE_LOCATION = "location";
	public static final String EVENT_TYPE_CLICK = "click";
	
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	
	public static boolean checkSignature(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		return checkSignature(signature, timestamp, nonce);
	}
	
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		if (signature == null || timestamp == null || nonce == null) {
			return false;
		}
		String[] arr = new String[] {WechatCfg.TOKEN, timestamp, nonce};
		Arrays.sort(arr);
		String cipher = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(StringUtils.join(arr).getBytes());
			cipher = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		}
		return cipher.equalsIgnoreCase(signature);
	}
	
	private static String byteToStr(byte[] bytes) {
		String digest = "";
		for (byte b : bytes) {
			digest += byteToHexStr(b);
		}
		return digest;
	}

	private static String byteToHexStr(byte b) {
		char[] digit = {'0','1','2','3','4','5','6','7','8','9',
				'A','B','C','D','E','F'};
		char[] temp = new char[2];
		temp[0] = digit[(b >>> 4) & 0X0F];
		temp[1] = digit[b & 0X0F];
		return new String(temp);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseReqXml(HttpServletRequest request) throws Exception {
		ServletInputStream in = request.getInputStream();
		Document doc = new SAXReader().read(in);
		List<Element> elements = doc.getRootElement().elements();
		HashMap<String,String> map = new HashMap<String, String>();
		for (Element e : elements) {
			map.put(e.getName(), e.getText());
		}
		in.close();
		return map;
	}
	
	public static String messageToXml(BaseMessage msg) throws Exception {
		JAXBContext content = JAXBContext.newInstance(msg.getClass());
		Marshaller m = content.createMarshaller();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CDataContentHandler handler = new CDataContentHandler();
		handler.setOutputByteStream(out);
		OutputFormat format = new OutputFormat();
		format.setIndenting(true);
		handler.setOutputFormat(format);
		m.marshal(msg, handler);
		return out.toString();
	}

	private static class CDataContentHandler extends XMLSerializer {
		
		private boolean useCData;
		
		@Override
		public void startElement(String namespaceURI, String localName, String rawName,
				Attributes attrs) throws SAXException {
			if ("CreateTime".equals(localName)) {
				useCData = false;
			} else {
				useCData = true;
			}
			super.startElement(namespaceURI, localName, rawName, attrs);
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (useCData) {
				super.startCDATA();
			}
			super.characters(ch, start, length);
			if (useCData) {
				super.endCDATA();
			}
		}
	}
	
}

