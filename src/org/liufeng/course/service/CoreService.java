package org.liufeng.course.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.liufeng.course.message.resp.Article;
import org.liufeng.course.message.resp.Music;
import org.liufeng.course.message.resp.MusicMessage;
import org.liufeng.course.message.resp.NewsMessage;
import org.liufeng.course.message.resp.TextMessage;
import org.liufeng.course.util.MessageUtil;

import org.liufeng.weixin.pojo.AccessToken;
import org.liufeng.weixin.util.Utils;
import org.liufeng.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心服务类
 * 
 * @author liufeng
 * @date 2013-07-25
 */
public class CoreService {

	private static Logger log = LoggerFactory.getLogger(CoreService.class);
	
//	private static String location = null;
	
	private static Map<String,String> locationMap = new HashMap<String,String>();

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 默认回复此文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			// 由于href属性值必须用双引号引起，这与字符串本身的双引号冲突，所以要转义
			StringBuffer contentMsg = new StringBuffer();
			contentMsg.append("您好，我是您的生活助理，请回复数字选择服务：").append("\n");
			contentMsg.append("1  天气预报").append("\n");
			contentMsg.append("2  歌曲点播").append("\n");
			contentMsg.append("3  周边搜索").append("\n");
			contentMsg.append("4  菜谱查询").append("\n");
			contentMsg.append("5  快递查询").append("\n");
			contentMsg.append("6  百科查询").append("\n");
			contentMsg.append("7  人脸识别").append("\n");
			contentMsg.append("8  聊天唠嗑").append("\n");
			contentMsg.append("点击查看 <a href=\"http://milotian.imwork.net/liveassistant/help.html\">帮助手册</a>");

			// 默认返回的文本消息内容
			String respContent = contentMsg.toString();

			log.info("Message Type " + msgType + " fromUserName " + fromUserName + " toUserName " + toUserName);
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 接收用户发送的文本消息内容
				String content = requestMap.get("Content").trim();

				// 创建图文消息
				NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				List<Article> articleList = new ArrayList<Article>();
				// 单图文消息
				if (content.length() == 1 && content.matches("[0-8]")) {
					Article article = new Article();
					article.setTitle("生活助理微信公众号功能简介");
					article.setDescription("本公众号一直致力于为广大人民群众提供方便的信息查询和生活服务。");
					article.setPicUrl("http://milotian.imwork.net/liveassistant/help/0"+ content + ".jpg");
					article.setUrl("http://milotian.imwork.net/liveassistant/help/0"+ content + ".html");
					articleList.add(article);
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					// 将图文消息对象转换成xml字符串
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
				}
//				// 单图文消息---不含图片
//				else if ("2".equals(content)) {
//					Article article = new Article();
//					article.setTitle("微信公众帐号开发教程Java版");
//					// 图文消息中可以使用QQ表情、符号表情
//					article.setDescription("柳峰，80后，"
//							+ emoji(0x1F6B9)
//							+ "，微信公众帐号开发经验4个月。为帮助初学者入门，特推出此系列连载教程，也希望借此机会认识更多同行！\n\n目前已推出教程共12篇，包括接口配置、消息封装、框架搭建、QQ表情发送、符号表情发送等。\n\n后期还计划推出一些实用功能的开发讲解，例如：天气预报、周边搜索、聊天功能等。");
//					// 将图片置为空
//					article.setPicUrl("");
//					article.setUrl("http://blog.csdn.net/lyq8479");
//					articleList.add(article);
//					newsMessage.setArticleCount(articleList.size());
//					newsMessage.setArticles(articleList);
//					respMessage = MessageUtil.newsMessageToXml(newsMessage);
//				}
//				// 多图文消息
//				else if ("3".equals(content)) {
//					Article article1 = new Article();
//					article1.setTitle("微信公众帐号开发教程\n引言");
//					article1.setDescription("");
//					article1.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");
//					article1.setUrl("http://blog.csdn.net/lyq8479/article/details/8937622");
//
//					Article article2 = new Article();
//					article2.setTitle("第2篇\n微信公众帐号的类型");
//					article2.setDescription("");
//					article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//					article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8941577");
//
//					Article article3 = new Article();
//					article3.setTitle("第3篇\n开发模式启用及接口配置");
//					article3.setDescription("");
//					article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//					article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8944988");
//
//					articleList.add(article1);
//					articleList.add(article2);
//					articleList.add(article3);
//					newsMessage.setArticleCount(articleList.size());
//					newsMessage.setArticles(articleList);
//					respMessage = MessageUtil.newsMessageToXml(newsMessage);
//				}
//				// 多图文消息---首条消息不含图片
//				else if ("4".equals(content)) {
//					Article article1 = new Article();
//					article1.setTitle("微信公众帐号开发教程Java版");
//					article1.setDescription("");
//					// 将图片置为空
//					article1.setPicUrl("");
//					article1.setUrl("http://blog.csdn.net/lyq8479");
//
//					Article article2 = new Article();
//					article2.setTitle("第4篇\n消息及消息处理工具的封装");
//					article2.setDescription("");
//					article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//					article2.setUrl("http://blog.csdn.net/lyq8479/article/details/8949088");
//
//					Article article3 = new Article();
//					article3.setTitle("第5篇\n各种消息的接收与响应");
//					article3.setDescription("");
//					article3.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//					article3.setUrl("http://blog.csdn.net/lyq8479/article/details/8952173");
//
//					Article article4 = new Article();
//					article4.setTitle("第6篇\n文本消息的内容长度限制揭秘");
//					article4.setDescription("");
//					article4.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//					article4.setUrl("http://blog.csdn.net/lyq8479/article/details/8967824");
//
//					articleList.add(article1);
//					articleList.add(article2);
//					articleList.add(article3);
//					articleList.add(article4);
//					newsMessage.setArticleCount(articleList.size());
//					newsMessage.setArticles(articleList);
//					respMessage = MessageUtil.newsMessageToXml(newsMessage);
//				}
//				// 多图文消息---最后一条消息不含图片
//				else if ("5".equals(content)) {
//					Article article1 = new Article();
//					article1.setTitle("第7篇\n文本消息中换行符的使用");
//					article1.setDescription("");
//					article1.setPicUrl("http://0.xiaoqrobot.duapp.com/images/avatar_liufeng.jpg");
//					article1.setUrl("http://blog.csdn.net/lyq8479/article/details/9141467");
//
//					Article article2 = new Article();
//					article2.setTitle("第8篇\n文本消息中使用网页超链接");
//					article2.setDescription("");
//					article2.setPicUrl("http://avatar.csdn.net/1/4/A/1_lyq8479.jpg");
//					article2.setUrl("http://blog.csdn.net/lyq8479/article/details/9157455");
//
//					Article article3 = new Article();
//					article3.setTitle("如果觉得文章对你有所帮助，请通过博客留言或关注微信公众帐号xiaoqrobot来支持柳峰！");
//					article3.setDescription("");
//					// 将图片置为空
//					article3.setPicUrl("");
//					article3.setUrl("http://blog.csdn.net/lyq8479");
//
//					articleList.add(article1);
//					articleList.add(article2);
//					articleList.add(article3);
//					newsMessage.setArticleCount(articleList.size());
//					newsMessage.setArticles(articleList);
//					respMessage = MessageUtil.newsMessageToXml(newsMessage);
//				}
				
				// 如果以“歌曲”2个字开头
				else if (content.startsWith("歌曲")) {
					// 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉
					String keyWord = content.replaceAll("^歌曲[\\+ ~!@#%^-_=]?", "");
					// 如果歌曲名称为空
					if ("".equals(keyWord)) {
						respContent = getUsage();
					} else {
						String[] kwArr = keyWord.split("＠");
						// 歌曲名称
						String musicTitle = kwArr[0];
						// 演唱者默认为空
						String musicAuthor = "";
						if (2 == kwArr.length)
							musicAuthor = kwArr[1];

						// 搜索音乐
						Music music = BaiduMusicService.searchMusic(musicTitle, musicAuthor);
						// 未搜索到音乐
						if (null == music) {
							respContent = "对不起，没有找到你想听的歌曲<" + musicTitle + "> " + "演唱by" + musicAuthor;
						} else {
							// 音乐消息
							MusicMessage musicMessage = new MusicMessage();
							musicMessage.setToUserName(fromUserName);
							musicMessage.setFromUserName(toUserName);
							musicMessage.setCreateTime(new Date().getTime());
							musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
							musicMessage.setMusic(music);
							respMessage = MessageUtil.musicMessageToXml(musicMessage);
						}
					}
					
					// 未搜索到音乐时返回使用指南
					if (null == respMessage) {
						if (null == respContent)
						  respContent = getUsage();
					}
				}else if(content.endsWith("天气")){
					String city = content.replace("天气", "");
					respContent = WeatherService.getWeatherInfo(city);
				}else if(content.startsWith("附近")){
					String location = locationMap.get(fromUserName);
					if(location != null && location.length() > 1){
						String carePlace = content.replace("附近", "");
						String result = NearbyPOIService.getNearbyPOI(carePlace, location);
						
						respContent =  JSONObject.fromObject(result).getString("results");
						log.info(" location " + location  + " carePlace " + carePlace + " 周边搜索结果  " + respContent );
						if(respContent.equals("[]")){
							respContent ="不好意思，在您提供的位置没有找到" + carePlace;
						}

					}else{
						respContent = "还不清楚您的具体位置呢，请先发送位置登记一下您的位置信息，再查询。";
					}
				}else{
					//唠嗑
					String robotResponse = RobotService.chatWithRobet(content);
					String robotResCode = JSONObject.fromObject(robotResponse).getString("code");
					int resCode = Integer.valueOf(robotResCode.trim());
					JSONObject robotResJson = JSONObject.fromObject(robotResponse);
					respContent = robotResJson.getString("text");
					log.info("机器人唠嗑啦 " + robotResJson);
					switch(resCode){
					case 100000:
						//文字类
						break;
					case 200000:
						//链接类
						break;
					case 302000:
						//新闻
						JSONArray newsJsonArray = robotResJson.getJSONArray("list");
						List<Article> newsArticles = Utils.JsonListToNewsArticles(newsJsonArray);
						newsMessage.setArticleCount(newsArticles.size());
						newsMessage.setArticles(newsArticles);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						break;
					case 305000:
						//列车
						JSONArray trainJsonArray = robotResJson.getJSONArray("list");
						List<Article> trainArticles = Utils.JsonListToTrainArticles(trainJsonArray);
						newsMessage.setArticleCount(trainArticles.size());
						newsMessage.setArticles(trainArticles);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						break;
					case 306000:
						//航班
					case 308000:
						//菜谱
						JSONArray menuJsonArray = robotResJson.getJSONArray("list");
						List<Article> menuArticles = Utils.JsonListToMenuArticles(menuJsonArray, Utils.menuJsonKeywords);
						newsMessage.setArticleCount(menuArticles.size());
						newsMessage.setArticles(menuArticles);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						break;
					default:
						
					}
				}

			}


			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				// 取得图片地址
				String picUrl = requestMap.get("PicUrl");
				// 人脸检测
				respContent = FaceService.detect(picUrl);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				String location_X  = requestMap.get("Location_X");
				String location_Y  = requestMap.get("Location_Y");
				String location = location_X + "," + location_Y;
                if(locationMap.containsKey(fromUserName)){
					
				}else{
					locationMap.put(fromUserName, location);
				}
				
				
				String detailAddress = requestMap.get("Label");
				respContent = "您发送的是地理位置消息！您的地理位置已更新，纬度为：" + location_X + " 经度为：" + location_Y
						+ " 详细地址为：" + detailAddress;
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//					respContent = "谢谢您的关注！";
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
				}
			}

			textMessage.setContent(respContent);
			
			//回复歌曲外其他都是文本
			if(respMessage == null){
				// 将文本消息对象转换成xml字符串
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}


	/**
	 * 歌曲点播使用指南
	 * 
	 * @return
	 */
	public static String getUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("歌曲点播操作指南").append("\n\n");
		buffer.append("回复：歌曲+歌名").append("\n");
		buffer.append("例如：歌曲存在").append("\n");
		buffer.append("或者：歌曲存在@汪峰").append("\n\n");
		buffer.append("回复“?”显示主菜单");
		return buffer.toString();
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}
}