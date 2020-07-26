<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员登录</title>
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

        <div style="margin:0 auto; margin-top:10px;width:950px;">
            <strong>我的订单</strong>
            <table class="table table-bordered">
                <tbody>
                <%--由于我们的pageModel里面是一个集合  我们通过遍历得到一个个的订单 --%>
                <c:forEach items="${page.list}" var="order">
                <tr class="success">
                    <th colspan="5">
                        订单编号:${order.oid}
                        总 金 额:${order.total}
                    <c:if test="${order.state==1}"><a href="${pageContext.request.contextPath}/orderServlet?method=findOrderByOid&oid=${order.oid}">去付款</a></c:if>
                    <c:if test="${order.state==2}">未发货</c:if>
                    <c:if test="${order.state==3}">已发货</c:if>
                    <c:if test="${order.state==4}">已签收</c:if>
                    </th>
                </tr>
                <tr class="warning">
                    <th>图片</th>
                    <th>商品</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>小计</th>
                </tr>
                    <%--循环里面再套循环 得到的是订单项--%>
                <c:forEach items="${order.list}" var="orderItem">
                <tr class="active">
                    <td width="60" width="40%">
                        <input type="hidden" name="id" value="22">
                        <img src="${pageContext.request.contextPath}/${orderItem.product.pimage}" width="70" height="60">
                    </td>
                    <td width="30%">
                        <a target="_blank"> ${orderItem.product.pname}</a>
                    </td>
                    <td width="20%">
                        ￥${orderItem.product.shop_price}
                    </td>
                    <td width="10%">
                            ${orderItem.quantity}
                    </td>
                    <td width="15%">
                        <span class="subtotal">￥${orderItem.total}</span>
                    </td>
                </c:forEach>
                </tr>
                </c:forEach>
                </tbody>

            </table>
        </div>
    </div>
    <%@include file="pageFile.jsp"%>
</div>


<%--尾部信息通过动态包含加载foot.jsp--%>
<%@include file="/jsp/footer.jsp" %>

</body>

</html>