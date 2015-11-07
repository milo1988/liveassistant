package org.liufeng.course.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http请求工具类
 * @author lanp
 * @since 2014-8-24 13:30:56
 * @version v1.0.1
 */
public class HttpRequestUtil {
	
	/**
	 * 编码
	 * @param source
	 * @return
	 */
	public static String urlEncode(String source,String encode) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source,encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "0";
		}
		return result;
	}
	
	/**
	 * 发起http请求获取返回结果
	 * @param requestUrl 请求地址
	 * @return
	 */
	public static String httpRequest(String requestUrl) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(false);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return buffer.toString();
	}
	
	/**
	 * 发送http请求取得返回的输入流
	 * @param requestUrl 请求地址
	 * @return InputStream
	 */
	public static InputStream httpRequestIO(String requestUrl) {
		InputStream inputStream = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			// 获得返回的输入流
			inputStream = httpUrlConn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}
}
