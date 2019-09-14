//微信分享结果
let shareResult = false;
/**
 * @description  获取链接参数值
 * @author zhangwei
 * @date 2019-03-12
 * @param {String} 参数名
 * @returns 参数值
 */
function getLinkParam(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  let paramValue = window.location.search.substr(1).match(reg);
  if (paramValue != null) {
    return decodeURIComponent(paramValue[2]);
  }
  return null;
}

/**
 * @description  埋点上报
 * @author zhangwei
 * @date 2019-02-20
 * @param {*} options 埋点相关配置信息
 */
function collectData(options) {
  $.ajax({
    type: "post",
    url: "https://h5.da.eebbk.net/h5/scrm",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify({
      machineId: options.machineId,
      moduleName: options.moduleName,
      appver: options.appver,
      // moduleDetail: options.moduleDetail,
      functionName: options.functionName,
      extend: JSON.stringify(options.extend)
    }),
    success: function(data) {}
  });
}

/**
 * @description 微信分享配置
 * @author zhangwei
 * @date 2019-02-20
 * @param {*} params 分享配置信息（标题、描述、图标,用户信息）
 */
function wxConfig(params) {
  $.ajax({
    url: "https://wxapi.okii.com/wx/jsconfig?" + location.href,
    type: "get",
    async: false,
    cache: true,
    dataType: "jsonp",
    jsonp: "jsoncallback",
    success: function(data) {
      wx.config({
        debug: false,
        appId: data.appId,
        timestamp: data.timestamp,
        nonceStr: data.nonceStr,
        signature: data.signature,
        jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage"]
      });
    }
  });
  wx.ready(function() {
    wx.checkJsApi({
      jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage"],
      success: function(res) {}
    });
    wx.onMenuShareAppMessage({
      title: params.shareTitle,
      desc: params.descContent,
      link: params.linkUrl,
      imgUrl: params.imgUrl,
      type: "link",
      dataUrl: "",
      success: function() {
        collectData({
          machineId: params.machineId || "1",
          moduleName: params.pageName || "页面名获取失败",
          appver: "1.0",
          functionName: "分享到朋友次数",
          extend: {
            channel: getLinkParam("channel"),
            fromId: getLinkParam("fromId"),
            pageUrl: location.href
          }
        });
      },
      cancel: function() {},
      fail: function(res) {}
    });

    wx.onMenuShareTimeline({
      title: params.shareTitle,
      link: params.linkUrl,
      imgUrl: params.imgUrl,
      success: function() {
        shareResult = true;
        localStorage.setItem("shareResult" , 1);
        collectData({
          machineId: params.machineId || "1",
          moduleName: params.pageName || "页面名获取失败",
          appver: "1.0",
          functionName: "分享到朋友圈次数",
          extend: {
            channel: getLinkParam("channel"),
            fromId: getLinkParam("fromId"),
            pageUrl: location.href
          }
        });
      },
      cancel: function() {},
      fail: function(res) {}
    });
  });
}

