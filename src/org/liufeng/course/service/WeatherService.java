package org.liufeng.course.service;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.liufeng.course.util.HttpRequestUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 天气预报服务
 * @author lanp
 * @since 2014-7-29 20:56:16
 * @version v1.0.1
 */
public class WeatherService {
	
	
	/**
	 * GBK编码
	 * @param source
	 * @return
	 */
	public static String urlEncodeGBK(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取天气预报XML信息并返回
	 * @param source
	 * @return
	 */
	public static String getWeatherJson(String source ) {
		String dst = null;
		// 组装查询地址
//		String requestUrl = "http://php.weather.sina.com.cn/xml.php?city={keyWord}&password=DJOYnieT8234jlsK&day="+day;
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location={keyWord}&output=json&ak=8OjaGkLpzgRlTIHvP1E8mtdX";
		// 对参数q的值进行urlEncode utf-8编码
		requestUrl = requestUrl.replace("{keyWord}", source);
//		System.out.print(requestUrl);
		dst = HttpRequestUtil.httpRequest(requestUrl);
		return dst;
	}
	
	/**
	 * 获取今天、明天和后天的天气预报信息并返回
	 * @param source
	 * @return
	 */
	public static String getWeatherInfo(String source) {
		StringBuffer buffer = new StringBuffer();
		String jsonResult = getWeatherJson(source);
		buffer.append(source).append(" 今明后三天天气情况如下：\n\n");
		JSONArray respContent =  JSONObject.fromObject(jsonResult).getJSONArray("results");
		JSONArray weatherdata = respContent.getJSONObject(0).getJSONArray("weather_data");
//		System.out.println(respContent);
//		System.out.println(weatherdata);
		for(int i=0; i< weatherdata.size();i++) {
			JSONObject daydata = weatherdata.getJSONObject(i);
			buffer.append(daydata.getString("date")).append(" ")
			.append("天气:").append(daydata.getString("weather")).append(" ")
			.append("风力:").append(daydata.getString("wind")).append(" ")
			.append("温度:").append(daydata.getString("temperature")).append("\n");
		}
		return (null==buffer?"":buffer.toString());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getWeatherInfo("广州"));
		
	}

}
