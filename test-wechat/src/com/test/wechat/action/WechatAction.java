package com.test.wechat.action;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.wechat.message.MessageUtil;
import com.test.wechat.message.resp.TextMessage;


@Controller
@RequestMapping("wechat")
public class WechatAction {
	
	private static Logger log = Logger.getLogger(WechatAction.class);
	
	@Autowired
	private  HttpServletRequest request;
	
	/**
	 * 微信验证URL有效性
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET,produces="text/plain")
	public String checkSignature() {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (MessageUtil.checkSignature(signature, timestamp, nonce)) {
			return echostr;
		} 
		return null;
	}
	
	/**
	 * 处理微信用户发来的请求 
	 */
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST,produces="text/plain; charset=utf-8")
	public String handleRequest() throws Exception {
		if(!MessageUtil.checkSignature(request)) {
			return null;
		}
		Map<String, String> map = MessageUtil.parseReqXml(request);
		if (log.isInfoEnabled()) {
			log.info(map);
		}
		String msgType = map.get("MsgType");
		TextMessage msg = new TextMessage();
		msg.setCreateTime(new Date().getTime());
		msg.setFromUserName(map.get("ToUserName"));
		msg.setToUserName(map.get("FromUserName"));
		msg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		String content = "";
		if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
			content = "你发送的是文本消息!";
		} else if (MessageUtil.REQ_MESSAGE_TYPE_VOICE.equals(msgType)) {
			content = "你发送的是语音消息!";
		} else if (MessageUtil.REQ_MESSAGE_TYPE_VIDEO.equals(msgType)) {
			content = "你发送的是视频消息!";
		} else if (MessageUtil.REQ_MESSAGE_TYPE_IMAGE.equals(msgType)) {
			content = "你发送的是图片消息!";
		} else if (MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equals(msgType)) {
			content = "你发送的是位置消息";
		} else if (MessageUtil.REQ_MESSAGE_TYPE_LINK.equals(msgType)) {
			content = "你发送的是连接消息!";
		} else {
			log.error("unknow msgType: " + msgType);
		}
		msg.setContent(content);
		return MessageUtil.messageToXml(msg);
	}

}
