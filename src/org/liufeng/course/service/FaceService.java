package org.liufeng.course.service;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

import org.liufeng.weixin.pojo.Face;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 人脸检测服务
 * 
 * @author liufeng
 * @date 2013-12-18
 */
public class FaceService {
	
	
	private final static int CONNECT_TIME_OUT = 30000;
	private final static int READ_OUT_TIME = 50000;
	private static String boundaryString = getBoundary();
	  
	/**
	 * 发送http请求
	 * 
	 * @param requestUrl 请求地址
	 * @return String
	 */
	private static String httpRequest(String requestUrl) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
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
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 调用Face++ API实现人脸检测
	 * 
	 * @param picUrl 待检测图片的访问地址
	 * @return List<Face> 人脸列表
	 */
	private static List<Face> faceDetect(String picUrl) {
		List<Face> faceList = new ArrayList<Face>();
		try {
//	      File file = new File("/Users/milo/Desktop/aobama.jpeg");
//			byte[] buff = getBytesFromFile(file);
//		  String picUrl = "http://pic11.nipic.com/20101111/6153002_002722872554_2.jpg";
		  byte[] buff = getFileStream(picUrl);
		  String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
	      HashMap<String, String> map = new HashMap<>();
	      HashMap<String, byte[]> byteMap = new HashMap<>();
	      map.put("api_key", "49Km3pXIAjLPkCL623l3Cv6mvZcyt50O");
	      map.put("api_secret", "ydpSgayRFxP2Ov56E8Yy6pK_PVA6DmWD");
		  map.put("return_landmark", "1");
	      map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
	      byteMap.put("image_file", buff);
          byte[] bacd = post(url, map, byteMap);
          String json = new String(bacd);
          System.out.println(json);
			// 解析返回json中的Face列表
			JSONArray jsonArray = JSONObject.fromObject(json).getJSONArray("faces");
			// 遍历检测到的人脸
			for (int i = 0; i < jsonArray.size(); i++) {
				// face
				JSONObject faceObject = (JSONObject) jsonArray.get(i);
				// attribute
				JSONObject attrObject = faceObject.getJSONObject("attributes");
				// position
				JSONObject posObject = faceObject.getJSONObject("face_rectangle");
				Face face = new Face();
				face.setFaceId(faceObject.getString("face_token"));
				face.setAgeValue(attrObject.getJSONObject("age").getInt("value"));
//				face.setAgeRange(attrObject.getJSONObject("age").getInt("range"));
				face.setGenderValue(genderConvert(attrObject.getJSONObject("gender").getString("value")));
//				face.setGenderConfidence(attrObject.getJSONObject("gender").getDouble("confidence"));
				face.setRaceValue(raceConvert(attrObject.getJSONObject("ethnicity").getString("value")));
//				face.setRaceConfidence(attrObject.getJSONObject("race").getDouble("confidence"));
				face.setSmilingValue(attrObject.getJSONObject("smile").getDouble("value"));
				face.setCenterX(posObject.getDouble("left") + posObject.getDouble("width"));
				face.setCenterY(posObject.getDouble("top") + posObject.getDouble("height"));
				faceList.add(face);
			}
		// 将检测出的Face按从左至右的顺序排序
		Collections.sort(faceList);
		} catch (Exception e) {
			faceList = null;
			e.printStackTrace();
		}
		return faceList;
	}

	/**
	 * 性别转换（英文->中文）
	 * 
	 * @param gender
	 * @return
	 */
	private static String genderConvert(String gender) {
		String result = "男性";
		if ("Male".equals(gender))
			result = "男性";
		else if ("Female".equals(gender))
			result = "女性";

		return result;
	}

	/**
	 * 人种转换（英文->中文）
	 * 
	 * @param race
	 * @return
	 */
	private static String raceConvert(String race) {
		String result = "黄色";
		if ("Asian".equals(race))
			result = "黄色";
		else if ("White".equals(race))
			result = "白色";
		else if ("Black".equals(race))
			result = "黑色";
		return result;
	}

	/**
	 * 根据人脸识别结果组装消息
	 * 
	 * @param faceList 人脸列表
	 * @return
	 */
	private static String makeMessage(List<Face> faceList) {
		StringBuffer buffer = new StringBuffer();
		// 检测到1张脸
		if (1 == faceList.size()) {
			buffer.append("共检测到 ").append(faceList.size()).append(" 张人脸").append("\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("人种,");
				buffer.append(face.getGenderValue()).append(",");
				buffer.append(face.getAgeValue()).append("岁左右").append("\n");
			}
		}
		// 检测到2-10张脸
		else if (faceList.size() > 1 && faceList.size() <= 10) {
			buffer.append("共检测到 ").append(faceList.size()).append(" 张人脸，按脸部中心位置从左至右依次为：").append("\n");
			for (Face face : faceList) {
				buffer.append(face.getRaceValue()).append("人种,");
				buffer.append(face.getGenderValue()).append(",");
				buffer.append(face.getAgeValue()).append("岁左右").append("\n");
			}
		}
		// 检测到10张脸以上
		else if (faceList.size() > 10) {
			buffer.append("共检测到 ").append(faceList.size()).append(" 张人脸").append("\n");
			// 统计各人种、性别的人数
			int asiaMale = 0;
			int asiaFemale = 0;
			int whiteMale = 0;
			int whiteFemale = 0;
			int blackMale = 0;
			int blackFemale = 0;
			for (Face face : faceList) {
				if ("黄色".equals(face.getRaceValue()))
					if ("男性".equals(face.getGenderValue()))
						asiaMale++;
					else
						asiaFemale++;
				else if ("白色".equals(face.getRaceValue()))
					if ("男性".equals(face.getGenderValue()))
						whiteMale++;
					else
						whiteFemale++;
				else if ("黑色".equals(face.getRaceValue()))
					if ("男性".equals(face.getGenderValue()))
						blackMale++;
					else
						blackFemale++;
			}
			if (0 != asiaMale || 0 != asiaFemale)
				buffer.append("黄色人种：").append(asiaMale).append("男").append(asiaFemale).append("女").append("\n");
			if (0 != whiteMale || 0 != whiteFemale)
				buffer.append("白色人种：").append(whiteMale).append("男").append(whiteFemale).append("女").append("\n");
			if (0 != blackMale || 0 != blackFemale)
				buffer.append("黑色人种：").append(blackMale).append("男").append(blackFemale).append("女").append("\n");
		}
		// 移除末尾空格
		buffer = new StringBuffer(buffer.substring(0, buffer.lastIndexOf("\n")));
		return buffer.toString();
	}

	/**
	 * 提供给外部调用的人脸检测方法
	 * 
	 * @param picUrl 待检测图片的访问地址
	 * @return String
	 */
	public static String detect(String picUrl) {
		// 默认回复信息
		String result = "未识别到人脸，请换一张清晰的照片再试！";
		List<Face> faceList = faceDetect(picUrl);
		if (null != faceList && faceList.size() != 0) {
			result = makeMessage(faceList);
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		String picUrl = "http://pic11.nipic.com/20101111/6153002_002722872554_2.jpg";
		System.out.println(detect(picUrl));
	}
	

  protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
      HttpURLConnection conne;
      URL url1 = new URL(url);
      conne = (HttpURLConnection) url1.openConnection();
      conne.setDoOutput(true);
      conne.setUseCaches(false);
      conne.setRequestMethod("POST");
      conne.setConnectTimeout(CONNECT_TIME_OUT);
      conne.setReadTimeout(READ_OUT_TIME);
      conne.setRequestProperty("accept", "*/*");
      conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
      conne.setRequestProperty("connection", "Keep-Alive");
      conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
      DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
      Iterator iter = map.entrySet().iterator();
      while(iter.hasNext()){
          Map.Entry<String, String> entry = (Map.Entry) iter.next();
          String key = entry.getKey();
          String value = entry.getValue();
          obos.writeBytes("--" + boundaryString + "\r\n");
          obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                  + "\"\r\n");
          obos.writeBytes("\r\n");
          obos.writeBytes(value + "\r\n");
      }
      if(fileMap != null && fileMap.size() > 0){
          Iterator fileIter = fileMap.entrySet().iterator();
          while(fileIter.hasNext()){
              Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
              obos.writeBytes("--" + boundaryString + "\r\n");
              obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                      + "\"; filename=\"" + encode(" ") + "\"\r\n");
              obos.writeBytes("\r\n");
              obos.write(fileEntry.getValue());
              obos.writeBytes("\r\n");
          }
      }
      obos.writeBytes("--" + boundaryString + "--" + "\r\n");
      obos.writeBytes("\r\n");
      obos.flush();
      obos.close();
      InputStream ins = null;
      int code = conne.getResponseCode();
      try{
          if(code == 200){
              ins = conne.getInputStream();
          }else{
              ins = conne.getErrorStream();
          }
      }catch (SSLException e){
          e.printStackTrace();
          return new byte[0];
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buff = new byte[4096];
      int len;
      while((len = ins.read(buff)) != -1){
          baos.write(buff, 0, len);
      }
      byte[] bytes = baos.toByteArray();
      ins.close();
      return bytes;
  }
  private static String getBoundary() {
      StringBuilder sb = new StringBuilder();
      Random random = new Random();
      for(int i = 0; i < 32; ++i) {
          sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
      }
      return sb.toString();
  }
  private static String encode(String value) throws Exception{
      return URLEncoder.encode(value, "UTF-8");
  }
  
  public static byte[] getBytesFromFile(File f) {
      if (f == null) {
          return null;
      }
      try {
          FileInputStream stream = new FileInputStream(f);
          ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
          byte[] b = new byte[1000];
          int n;
          while ((n = stream.read(b)) != -1)
              out.write(b, 0, n);
          stream.close();
          out.close();
          return out.toByteArray();
      } catch (IOException e) {
      }
      return null;
  }
  
  
  public static byte[] getFileStream(String url)throws Exception{
      try {
          URL httpUrl = new URL(url);
          HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
          conn.setRequestMethod("GET");
          conn.setConnectTimeout(5 * 1000);
          InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
          byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
          return btImg;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
  }
  
  public static byte[] readInputStream(InputStream inStream) throws Exception{  
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
      byte[] buffer = new byte[1024];  
      int len = 0;  
      while( (len=inStream.read(buffer)) != -1 ){    
          outStream.write(buffer, 0, len);  
      }  
      inStream.close();   
      return outStream.toByteArray();  
  }  
}