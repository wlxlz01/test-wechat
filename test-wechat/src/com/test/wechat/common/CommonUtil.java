package com.test.wechat.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonParser;

public abstract class CommonUtil {
	
	private static final Logger log = Logger.getLogger(CommonUtil.class);
	
	private static String access_token;
	private static long ACCESS_TOKEN_UPDATE_TIME;
	private static long ACCESS_TOKEN_EXPIRE_TIME = (7200 - 30) * 1000;
	
	
	public static String getAccessToken() {
		if (access_token == null || System.currentTimeMillis() - 
				ACCESS_TOKEN_UPDATE_TIME > ACCESS_TOKEN_EXPIRE_TIME) {
			access_token = doGetAccessToken();
			ACCESS_TOKEN_UPDATE_TIME = System.currentTimeMillis();
		}
		return access_token;
	}
	
	
	public static String doGetAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?" +
				"grant_type=client_credential&appid=APPID&secret=APPSECRET";
		url = url.replace("APPID", WechatCfg.appID).replace("APPSECRET", WechatCfg.appsecret);
		String res = httpsGetReq(url);
		return new JsonParser().parse(res).getAsJsonObject()
				.get("access_token").getAsString();
	}
	
	public static String httpsPostReq(String url, String content) {
		return httpsReq(url, "POST", content);
	}
	
	public static String httpsGetReq(String url) {
		return httpsReq(url, "GET", null);
	}
	
	public static String httpsReq(String url, String reqMethod, String content) {
		if (log.isDebugEnabled()) {
			log.debug("https请求：\n\t url:" + url + 
					"\n\t method:" + reqMethod + "\n\t content:" + content);
		}
		StringBuffer sb = new StringBuffer();
		try {
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
			TrustManager[] tm = {new SimpleX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			conn.setSSLSocketFactory(ssf);
			conn.setRequestMethod(reqMethod);
			if (StringUtils.isNotBlank(content)) {
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(content.getBytes("UTF-8"));
				os.close();
			}
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String s;
			while ((s=reader.readLine()) != null) {
				sb.append(s);
			}
			reader.close();
			conn.disconnect();
		} catch (Exception e) {
			log.error("https请求异常", e);
		} 
		if (log.isDebugEnabled()) {
			log.debug("https请求返回值：\n\t" + sb.toString());
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(getAccessToken());
	}

}
