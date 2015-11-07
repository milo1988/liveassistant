package org.liufeng.course.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.liufeng.course.util.HttpRequestUtil;

public class NearbyPOIService {

	private static String requestUrl = "http://api.map.baidu.com/place/v2/search?ak={ak}&output=json&query={carePlace}&page_size=5&page_num=0&scope=2&location={location}&radius=2000&filter=sort_name:distance";
	private static String ak = "8OjaGkLpzgRlTIHvP1E8mtdX";

	/**
	 * 获取百度POI的XML信息并返回
	 * @param source
	 * @return
	 */
	public static String getNearbyPOI(String carePlace ,String location) {
		String dst = null;
		// 对参数q的值进行urlEncode utf-8编码
		requestUrl = requestUrl.replace("{carePlace}", HttpRequestUtil.urlEncode(carePlace, "GBK"));
		requestUrl = requestUrl.replace("{location}", HttpRequestUtil.urlEncode(location, "GBK"));
		requestUrl = requestUrl.replace("{ak}", ak);
		dst = HttpRequestUtil.httpRequest(requestUrl);
		return dst;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String result =  getNearbyPOI("厕所","39.915,116.404");
		JSONArray jsonArray = JSONObject.fromObject(result).getJSONArray("results");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject poiObject = (JSONObject) jsonArray.get(i); 
			System.out.println(poiObject.toString());
		}
	}
}
