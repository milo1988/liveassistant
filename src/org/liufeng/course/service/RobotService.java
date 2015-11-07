package org.liufeng.course.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RobotService {

	/**
	 * 调用图灵机器人平台接口
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(chatWithRobet("北京今日天气"));
	}

	public static String chatWithRobet(String inputStr) throws IOException {
		String replyStr ="Sorry, I can't understand what you just said.";
		String APIKEY = "c7fd2009579fe931110ab8ae8b5e8ed5";
		String INFO = URLEncoder.encode(inputStr, "utf-8");
		String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY
				+ "&info=" + INFO;
		URL getUrl = new URL(getURL);
		HttpURLConnection connection = (HttpURLConnection) getUrl
				.openConnection();
		connection.connect();

		// 取得输入流，并使用Reader读取
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		// 断开连接
		connection.disconnect();
		replyStr = sb.toString();
		return replyStr;
	}
}
