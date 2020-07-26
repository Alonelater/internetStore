<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>菜单</title>
<link href="${pageContext.request.contextPath}/css/left.css" rel="stylesheet" type="text/css"/>
<link rel="StyleSheet" href="${pageContext.request.contextPath}/css/dtree.css" type="text/css" />
</head>
<body>
<table width="100" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="12"></td>
  </tr>
</table>
<table width="100%" border="0">
  <tr>
    <td>
<div class="dtree">

	<a href="javascript: d.openAll();">展开所有</a> | <a href="javascript: d.closeAll();">关闭所有</a>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/dtree.js"></script>
	<script type="text/javascript">
	/*下面我们介绍一下下面的第三方插件里面的属性图结构是怎么使用的  然后参数分别代表什么
	*
	* params1   表示当前节点
	* params2   表示当前节点的父节点
	* params3   节点名称
	* params4   节点跳转的信息
	* params5   节点的提示信息  就是你鼠标放上去会有什么信息提示
	* params6   跳转到那块区域 就是你的target里面的name
	* */
		d = new dTree('d');
		d.add('01',-1,'系统菜单');
		
		d.add('0102','01','分类管理','','','mainFrame');
		d.add('010201','0102','分类管理','${pageContext.request.contextPath}/adminCategoryServlet?method=getAllCats','','mainFrame');
		d.add('010202','0102','已下架分类管理','${pageContext.request.contextPath}/adminCategoryServlet?method=getAllLowCats','','mainFrame');


		d.add('0104','01','商品管理');
		d.add('010401','0104','商品管理','${pageContext.request.contextPath}/adminProductServlet?method=getAllProductWithPage&num=1','','mainFrame');
		d.add('010402','0104','已下架商品管理','${pageContext.request.contextPath}/adminProductServlet?method=getAllPushDownWithPage&num=1','','mainFrame');



		d.add('0105','01','订单管理');
		d.add('010501','0105','订单管理',   '${pageContext.request.contextPath}/adminOrderServlet?method=findAllOrder&num=1&state=0','','mainFrame');
		d.add('010502','0105','未付款的订单','${pageContext.request.contextPath}/adminOrderServlet?method=findAllOrder&num=1&state=1','','mainFrame');
		d.add('010503','0105','已付款订单', '${pageContext.request.contextPath}/adminOrderServlet?method=findAllOrder&num=1&state=2','','mainFrame');
		d.add('010504','0105','已发货的订单','${pageContext.request.contextPath}/adminOrderServlet?method=findAllOrder&num=1&state=3','','mainFrame');
		d.add('010505','0105','已完成的订单','${pageContext.request.contextPath}/adminOrderServlet?method=findAllOrder&num=1&state=4','','mainFrame');

		d.add("0106",'01','用户管理');
		d.add("010601",'0106','钻石用户','','','mainFrame');
		d.add("010602",'0106','vip用户','','','mainFrame');
		d.add("010603",'0106','普通用户','','','mainFrame');
		document.write(d);
		
	</script>
</div>	</td>
  </tr>
</table>
</body>
</html>
