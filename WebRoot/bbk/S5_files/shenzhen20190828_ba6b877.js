window.onload = function () {
    for (var e = document.getElementsByClassName("loading"), t = 0; t < e.length; t++) {
        var a = e[t].getAttribute("data-src");
        e[t].src = a, e[t].addEventListener("load", function () {
            $(this).removeClass("loading")
        })
    }
}, $(function () {
    try {
        wxConfig({
            shareTitle: "孩子开学必备，步步高旗舰S5 ",
            descContent: "学习新工具，AI人工智能辅导，哪里不会指哪里  ",
            linkUrl: location.href,
            imgUrl: "https://shop-file.tiancaixing.com/2019/8/30/1567127799257/YKKOkYox.png",
            pageName: "孩子开学必备，步步高旗舰S5(okii-lift)"
        }), collectData({
            machineId: "",
            moduleName: "孩子开学必备，步步高旗舰S5(okii-lift)",
            appver: "1.0",
            functionName: getLinkParam("channel") ? "朋友圈访问次数" : "二维码访问次数",
            extend: {
                channel: getLinkParam("channel"),
                fromId: getLinkParam("fromId"),
                pageUrl: location.href
            }
        })
    } catch (e) {}
    var t = new WOW({
        boxClass: "wow",
        animateClass: "animated",
        offset: -10,
        mobile: !0,
        live: !0
    });
    $(document).on("click", "#product", function () {
        $(".product").removeClass("hide")
    }).on("click", ".add", function () {
        var e = parseInt($($(this).data("tag")).html()) + 1;
        $($(this).data("tag")).html(e)
    }).on("click", ".sub", function () {
        var e = parseInt($($(this).data("tag")).html()) - 1;
        e = 0 > e ? 0 : e, $($(this).data("tag")).html(e)
    }).on("click", "#colorBtn", function () {
        $(".product").addClass("hide");
        var e = "";
        0 !== parseInt($("#count1").html()) && (e = "步步高旗舰S5 数量" + $("#count1").html(), console.log(e)), $("#product").html(e)
    }).on("click", "#time", function () {
        $(".time-box").removeClass("hide")
    }).on("click", "#timeBtn", function () {
        $(".time-box").addClass("hide");
        var e = $("#date option").eq($("#date")[0].selectedIndex).text() + "  " + $("#date-time option").eq($("#date-time")[0].selectedIndex).text();
        $("#time").html(e)
    }).on("click", "#order", function () {
        var e = $("#product").html(),
            t = $("#time").html(),
            a = $("#address").val(),
            o = $("#tel").val(),
            l = $("#username").val();
        return "" === e || "选择颜色和数量" === e ? void alert("请选择产品型号") : "" === t || "预约送货时间" === t ? void alert("请选择送货时间") : "" === l ? void alert("请输入联系人") : "" !== o && /1[0-9]{10}/g.test(o) ? "" === a ? void alert("请输入收获地址") : ($(".new-page").addClass("hide"), $(".orderDetails").removeClass("hide"), $(".orderDetails #confirmProduct").html(e), $(".orderDetails #confirmTime").html(t), $(".orderDetails #confirmUser").html(l), $(".orderDetails #confirmTel").html(o), void $(".orderDetails #confirmAddress").html(a)) : void alert("请输入正确的电话")
    }).on("click", "#orderConfirm", function () {
        var e = $("#product").html(),
            t = $("#time").html(),
            a = $("#address").val(),
            o = $("#tel").val(),
            l = $("#username").val();
        return "" === e || "选择颜色和数量" === e ? void alert("请选择产品型号") : "" === t || "预约送货时间" === t ? void alert("请选择送货时间") : "" === l ? void alert("请输入联系人") : "" !== o && /1[0-9]{10}/g.test(o) ? "" === a ? void alert("请输入收获地址") : void $.ajax({
            url: "http://localhost:8080/liveassistant/bbk/commitInfo",
            type: "post",
            dataType: "json",
            data: {
                contactName: l,
                contactPhone: o,
                contactAddress: a,
                sendTime: t,
                product: e,
                city: "shenzhen",
                brand: "eebbk"
            },
            success: function (i) {
            	console.log(i.stateCode);
                if ("000001" === i.stateCode) {
                    try {
                        collectData({
                            machineId: "",
                            moduleName: "孩子开学必备，步步高旗舰S5(okii-lift)",
                            appver: "1.0",
                            functionName: "下单成功",
                            extend: {
                                channel: getLinkParam("channel"),
                                fromId: getLinkParam("fromId"),
                                pageUrl: location.href
                            }
                        })
                    } catch (n) {}
                    $(".result").removeClass("hide"), $(".orderDetails").addClass("hide"), $(".result #confirmProduct").html(e), $(".result #confirmTime").html(t), $(".result #confirmUser").html(l), $(".result #confirmTel").html(o), $(".result #confirmAddress").html(a), $(".success").removeClass("hide"), $("#detail").addClass("hide"), document.body.scrollTop = 0, document.documentElement.scrollTop = 0
                } else {
                    alert(i.stateInfo);
                    try {
                        collectData({
                            machineId: "",
                            moduleName: "孩子开学必备，步步高旗舰S5(okii-lift)",
                            appver: "1.0",
                            functionName: "下单失败",
                            extend: {
                                channel: getLinkParam("channel"),
                                fromId: getLinkParam("fromId"),
                                pageUrl: location.href
                            }
                        })
                    } catch (n) {
                    	alert(n);
                    }
                }
            },
            error:function(msg){
            	alert(msg);
	        }
        }) : void alert("请输入正确的电话")
    }).on("click", "#orderRetrun", function () {
        $(".new-page").removeClass("hide"), $(".orderDetails").addClass("hide")
    }).on("click", ".close", function () {
        $(".modal").addClass("hide")
    }).on("click", ".act-rule", function () {
        $(".rule").removeClass("hide")
    }).on("click", ".cancel", function () {
        $(".result").addClass("hide"), $("#detail").removeClass("hide"), $(".new-page").removeClass("hide")
    }), t.init(), $(".back-top").click(function () {
        document.body.scrollTop = 0, document.documentElement.scrollTop = 0
    })
});