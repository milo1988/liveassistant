package org.liufeng.weixin.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.liufeng.course.message.resp.Article;

/**
 * @author Administrator
 *
 */
public class Utils {
	public static List<Article> articles = new ArrayList<Article>();
	public static int resultItemNum = 5;
	public static String[] menuJsonKeywords ={"name","info","icon","detailurl"};
	public static String[] trainJsonKeywords ={"trainnum","start","terminal","starttime","endtime","icon","detailurl"};
	

	
	/**
	 * 菜谱返回列表
	 * @param jsonArray
	 * @param keywords
	 * @return
	 */
	public static List<Article> JsonListToMenuArticles(JSONArray jsonArray, String[] keywords){
		articles.clear();
		for (int i = 0; i < resultItemNum; i++) {
			JSONObject article = (JSONObject) jsonArray.get(i);
			String articleTitle = article.getString(keywords[0]);
			String articleDesc = article.getString(keywords[1]);
			String articlePicUrl = article.getString(keywords[2]);
			String articleUrl = article.getString(keywords[3]);
			articles.add(new Article(articleTitle, articleDesc, articlePicUrl, articleUrl));
		}
		return articles;
	}
	
	
	/**
	 * 列车返回列表
	 * @param jsonArray
	 * @param keywords
	 * @return
	 */
	public static List<Article> JsonListToTrainArticles(JSONArray jsonArray){
		articles.clear();
		for (int i = 0; i < resultItemNum; i++) {
			JSONObject article = (JSONObject) jsonArray.get(i);
			String articleTitle = article.getString("trainnum") + "\n" + article.getString("starttime") + " - " +  article.getString("endtime");
			String articleDesc = article.getString("trainnum");
			String articlePicUrl = article.getString("icon");
			String articleUrl = article.getString("detailurl");
			articles.add(new Article(articleTitle, articleDesc, articlePicUrl, articleUrl));
		}
		return articles;
	}
	
	public static List<Article> JsonListToNewsArticles(JSONArray jsonArray){
		articles.clear();
		for (int i = 0; i < resultItemNum; i++) {
			JSONObject article = (JSONObject) jsonArray.get(i);
			String articleTitle = article.getString("article") + "\n" + article.getString("source");
			String articlePicUrl = article.getString("icon");
			String articleUrl = article.getString("detailurl");
			articles.add(new Article(articleTitle, "", articlePicUrl, articleUrl));
		}
		return articles;
	}
}
