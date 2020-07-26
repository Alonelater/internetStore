<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
<HEAD>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css"/>
    <script language="javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript">
        function showDetail(oid) {
            var $val = $("#but" + oid).val();
            if ($val == "订单详情") {
                // ajax 显示图片,名称,单价,数量
                $("#div" + oid).append("<img width='60' height='65' src='${pageContext.request.contextPath}/products/1/c_0028.jpg'>&nbsp;xxxx&nbsp;998<br/>");

                $("#but" + oid).val("关闭");
            } else {
                $("#div" + oid).html("");
                $("#but" + oid).val("订单详情");
            }
        }
    </script>
</HEAD>
<body>
<br>
<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/user/list.jsp" method="post">
    <table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
        <TBODY>
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3">
                <strong>订单列表</strong>
            </TD>
        </tr>

        <tr>
            <td class="ta_01" align="center" bgColor="#f5fafe">
                <table cellspacing="0" cellpadding="1" rules="all"
                       bordercolor="gray" border="1" id="DataGrid1"
                       style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
                    <tr
                            style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

                        <td align="center" width="5%">
                            序号
                        </td>
                        <td align="center" width="25%">
                            订单编号
                        </td>
                        <td align="center" width="5%">
                            订单金额
                        </td>
                        <td align="center" width="5%">
                            收货人
                        </td>
                        <td align="center" width="5%">
                            订单状态
                        </td>
                        <td align="center" width="55%">
                            订单详情
                        </td>
                    </tr>
                    <c:forEach items="${page.list}" var="o" varStatus="status">
                    <tr onmouseover="this.style.backgroundColor = 'white'"
                        onmouseout="this.style.backgroundColor = '#F5FAFE';">
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="5%">
                            ${status.count}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="25%">
                            ${o.oid}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="5%">
                            ${o.total}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="5%">
                            ${o.name}
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="5%">
                            <c:if test="${o.state==1}">未付款</c:if>
                            <c:if test="${o.state==2}">发货</c:if>
                            <c:if test="${o.state==3}">已发货</c:if>
                            <c:if test="${o.state==4}">订单完成</c:if>
                        </td>
                        <td align="center" style="HEIGHT: 22px" width="55%">
                            <input type="button" value="订单详情" id="${o.oid}" class="orderDetail"/>
                            <%--<div id="div${o.oid}">

                            </div>--%>
                            <table border="1" width="100%" cellpadding="0" cellspacing="0">
                            </table>
                        </td>

                    </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr align="center">
            <td colspan="7">

            </td>
        </tr>
        </TBODY>
    </table>
    <%@include file="/jsp/pageFile.jsp" %>
</form>
<script>
    $(function () {

        $(".orderDetail").click(function () {
            // 现在我们要向服务端发送ajax请求 让他们帮我们查询出我们的每个订单下面的订单项
            //我们需要查询的id 所以我们在上面就要准备一个id
            var oid = this.id;
            var url = "/adminOrderServlet";
            obj= {"method":"findOrderByIdWithAjax","oid":oid};
            //由于我们需要将我们的自己制作的就html加入到我们的table里面 所以我们就要用jquery的提供的方法找到合适的下一个节点
            var $table = $(this).next();

            var txt = this.value;
            if (txt=="订单详情"){

                $.post(url,obj,function (data) {
                    /*当我们每次点击的时候都要清除原来的html省的重复*/
                    $table.html("");
                    var $th = "<tr><td>商品图片</td><td>名称</td><td>数量</td><td>单价</td></tr>";
                    $table.append($th);
                    //利用jquer遍历返回到客户端的数据
                    $.each(data,function (i,obj) {
                        var td ="<tr><td><img src='${pageContext.request.contextPath}/"+obj.product.pimage+"' width='25%'></td><td>"+obj.product.pname+"</td><td>"+obj.quantity+"</td><td>"+obj.product.shop_price+"</td></tr>";
                        $table.append(td)
                    })
                },"json");
                //并且将订单详情改为关闭
                this.value = "关闭";
            }else{
                this.value = "订单详情";
                $table.html("");
            }
        });
    });
</script>
</body>
</HTML>

