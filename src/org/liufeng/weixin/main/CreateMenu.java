package org.liufeng.weixin.main;

import org.liufeng.weixin.pojo.AccessToken;
import org.liufeng.weixin.pojo.Button;
import org.liufeng.weixin.pojo.CommonButton;
import org.liufeng.weixin.pojo.ComplexButton;
import org.liufeng.weixin.pojo.Menu;
import org.liufeng.weixin.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜单管理器类
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class CreateMenu {
	private static Logger log = LoggerFactory.getLogger(CreateMenu.class);

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wxae3bbd935edc0c05";
		// 第三方用户唯一凭证密钥
		String appSecret = "c72de8605345b6d10f0b702ad397f97b";

		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("天气预报");
		btn11.setType("click");
		btn11.setKey("11");


		CommonButton btn21 = new CommonButton();
		btn21.setName("歌曲点播");
		btn21.setType("click");
		btn21.setKey("21");

		CommonButton btn31 = new CommonButton();
		btn31.setName("合作联系");
		btn31.setType("click");
		btn31.setKey("31");

		CommonButton btn32 = new CommonButton();
		btn32.setName("作者介绍");
		btn32.setType("click");
		btn32.setKey("32");

		CommonButton btn33 = new CommonButton();
		btn33.setName("赞助小编");
		btn33.setType("click");
		btn33.setKey("33");


		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("与我联系");
		mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33 });

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { btn11, btn21, mainBtn3 });

		return menu;
	}
}