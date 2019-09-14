package org.liufeng.course.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.liufeng.course.message.resp.TextMessage;
import org.liufeng.course.service.CoreService;
import org.liufeng.course.service.NotifyService;
import org.liufeng.course.util.MessageUtil;
import org.liufeng.course.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 核心请求处理类
 * 
 * @author milotian
 * @date 2019-09-12
 */
public class NotifyServlet extends HttpServlet {
	
	private static Logger log = LoggerFactory.getLogger(NotifyServlet.class);
	private static final long serialVersionUID = 4440739483644821986L;
	
//	
//	public static byte[] getRequestPostBytes(HttpServletRequest request) 
//		throws IOException { 
//		int contentLength = request.getContentLength(); 
//		if(contentLength<0){ 
//		return null; 
//		} 
//		byte buffer[] = new byte[contentLength]; 
//		for (int i = 0; i < contentLength;) {
//	
//		        int readlen = request.getInputStream().read(buffer, i,
//		                contentLength - i);
//		        if (readlen == -1) {
//		            break;
//		        }
//		        i += readlen;
//		    }
//		    return buffer;
//		}
//			
//	public static String getRequestPostStr(HttpServletRequest request)
//	        throws IOException {
//	    byte buffer[] = getRequestPostBytes(request);
//	    String charEncoding = request.getCharacterEncoding();
//	    if (charEncoding == null) {
//	        charEncoding = "UTF-8";
//	    }
//	    return new String(buffer, charEncoding);
//	}
	

	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
//		log.info(getRequestPostStr(request));
		// 调用核心业务类接收消息、处理消息
		String contactPhone = request.getParameter("contactPhone");
		String contactName = request.getParameter("contactName");
		String contactAddress = request.getParameter("contactAddress");
		String sendTime = request.getParameter("sendTime");
		String info = contactName + " " + contactPhone + " " + contactAddress + " " + sendTime;
		log.info(info);
		// 默认回复此文本消息
//		TextMessage textMessage = new TextMessage();
//		textMessage.setToUserName("oInp5t-O0Mpt3iSzLTAhkLxbnHy8");
//		textMessage.setFromUserName("gh_c463d5527a41");
//		textMessage.setCreateTime(new Date().getTime());
//		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//		textMessage.setFuncFlag(0);
//		textMessage.setContent(info);
//		String respMessage = MessageUtil.textMessageToXml(textMessage);
		
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print("{\"stateCode\":\"000001\" ,\"result\":\""+ info+"\"}");
		out.close();
	}

}