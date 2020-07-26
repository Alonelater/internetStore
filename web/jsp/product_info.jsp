<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>商品详情信息</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>

    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }
    </style>
</head>

<body>


<%--头部信息通过动态包含加载header.jsp--%>
<%@include file="/jsp/header.jsp" %>


<div class="container">
    <div class="row">
        <div style="border: 1px solid #e4e4e4;width:930px;margin-bottom:10px;margin:0 auto;padding:10px;margin-bottom:10px;">
            <a href="${pageContext.request.contextPath}/index.jsp">首页</a>
        </div>

        <div style="margin:0 auto;width:950px;">
            <div class="col-md-6">
                <img style="opacity: 1;width:400px;height:350px;" title="" class="medium"
                     src="${pageContext.request.contextPath}/${product.pimage}">
            </div>

            <div class="col-md-6">
                <form method="post" id="MyForm" action="${pageContext.request.contextPath}/cartServlet?method=addCartItemToCart">
                    <div><strong>${product.pname}</strong></div>
                    <div style="border-bottom: 1px dotted #dddddd;width:350px;margin:10px 0 10px 0;">
                        <div>${product.pid}</div>
                    </div>

                    <div style="margin:10px 0 10px 0;">商城价: <strong
                            style="color:#ef0101;">￥：${product.shop_price}元/份</strong> 市 场 价：
                        <del>￥${product.market_price}元/份</del>
                    </div>

                    <div style="margin:10px 0 10px 0;">促销: <a target="_blank" title="限时抢购 (2014-07-30 ~ 2015-01-01)"
                                                              style="background-color: #f07373;">限时抢购</a></div>

                    <div style="padding:10px;border:1px solid #e7dbb1;width:330px;margin:15px 0 10px 0;;background-color: #fffee6;">
                        <div style="margin:5px 0 10px 0;">白色</div>

                        <div style="border-bottom: 1px solid #faeac7;margin-top:20px;padding-left: 10px;">购买数量:

                            <%--向服务器传递购买数量和pid的值--%>
                            <input id="quantity" name="quantity" value="1" maxlength="4" size="10" type="text"></div>
                            <input type="hidden"  name="pid" value="${product.pid}">


                        <div style="margin:20px 0 10px 0;;text-align: center;">
                            <%--加入到购物车 --%>
                            <%--取消按钮默认事件  这里的类型原本是button  但是提交不了我们的form表单 你可以将其改成了submit 但是可能会破坏美工给的排版 我们就不改 用javascript加上jquery的方式  我们再提供一种javascript他提交表单的方式--%>
                            <%--看最下面的--%>
                            <a href="javascript:void(0)">
                                <input id="btnId" style="background: url('${pageContext.request.contextPath}/img/product.gif') no-repeat scroll 0 -600px rgba(0, 0, 0, 0);height:36px;width:127px;"
                                       value="加入购物车" type="button">
                            </a> &nbsp;收藏商品
                        </div>
                    </div>
                </form>
            </div>
        </div>


        <div class="clear"></div>
        <div style="width:950px;margin:0 auto;">
            <div style="background-color:#d3d3d3;width:930px;padding:10px 10px;margin:10px 0 10px 0;">
                <strong>${product.pdesc}</strong>
            </div>
        </div>
    </div>
</div>


<%--尾部信息通过动态包含加载foot.jsp--%>
<%@include file="/jsp/footer.jsp" %>


</body>
<%--这是提供提交表单的函数--%>
<script>
    /*当页面加载完了执行这个函数*/
    $(function () {//这是jquery的方法
        $("#btnId").click(function () {
            var formObj = document.getElementById("MyForm");
            /*如果你要提交这个表单到多个地址 就在下面写新得地址  不写就是默认提交的上面的action和method*/
            /*formObj.url="";
            formObj.method="get/post"*/
            formObj.submit();
        });
    });



</script>
</html>