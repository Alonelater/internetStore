<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>关于我们</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
</head>

<body>


<!--
        描述：菜单栏
    -->
<div class="container-fluid">
    <div class="col-md-4">
        <img src="${pageContext.request.contextPath}/img/logo6.png"/>
    </div>
    <div class="col-md-5">
        <img src="${pageContext.request.contextPath}/img/header.png"/>
    </div>
    <div class="col-md-3" style="padding-top:20px">
        <ol class="list-inline">
            <c:if test="${empty userLogin}">
                <li><a href="${pageContext.request.contextPath}/userServlet?method=loginUI">登录</a></li>
                <li><a href="${pageContext.request.contextPath}/userServlet?method=registerUI">注册</a></li>
            </c:if>
            <c:if test="${not empty userLogin}">
                <li>欢迎${userLogin.username}</li>
                <li><a href="${pageContext.request.contextPath}/userServlet?method=loginOut">退出</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/cart.jsp">购物车</a></li>
                <li><a href="${pageContext.request.contextPath}/orderServlet?method=findMyOrdersWithPage&num=1">我的订单</a></li>
            </c:if>

        </ol>
    </div>
</div>
<!--
    描述：导航条
-->
<div class="container-fluid">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">首页</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav" id="myUL">
                    <%--我们发现只是通过这种遍历存在request域里面的信息并不能将我们所有包含的页面的信息给展示到位 所以我们不用这种方式--%>
                    <%--详细看本页面最后一段代码 有注释 好好领悟--%>
                    <%--<c:forEach items="${allCategory}" var="cate">
                        <li class="active"><a href="#">${cate.cname}</a></li>
                    </c:forEach>--%>
                </ul>
                <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>

            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
</div>



<%--当浏览器解析到这里的时候 也就是要加载的地方都已经加载完了  我们这时候就要向服务器发起ajax请求 然后服务器经过处理
将我们需要的信息以json的格式发给我们  我们现在已经获取到了相应的json格式的分类信息 我们要做的就是将其通过标签拼接代码显示在
我们要显示的数据到我们指定的位置上就行
--%>
<script type="text/javascript">
    /*下面利用jquery写一个页面加载完的函数  就是$(function(){});*/
    $(function () {
        //在进行表单提交 其实并没有什么表单提交  就是你要告诉服务器你需要那个servlet帮你处理 所以我们就用jquery这样写$.post("");
        /*代表表单这样提交 第一个是路径 就是你提交上去谁帮你处理 第二个是参数 这里我们提供的是method  因为我们的所有的servlet类
        * 都是继承BaseServlet的 所以会根据你写的method方法去找到你值并通过反射调用该方法*/
        var url = "/categoryServlet";//一定要注意我们这里不要用/InternetStore因为我们的虚拟目录里面是/
        var obj = {"method":"findAllCats"};
        /*具体的功能代码就到CategoryServlet里面去查看 */
        $.post(url,obj,function (data) {
            //为了看到效果我们打印一下得到的数据
            //alert(data);

            /*前面通过CategoryServlet里面的findAllCats我们会得到相应的json数据  被封装在我们的data里面 现在我么要将其取出封装在我们的
            * 标签和变量里面  我们先用jquery遍历*/
            //jquery里面的遍历函数  第一个参数 是遍历的数据 也就是我们这里的data  第二个是个函数 里面的第一个参数是下表
            //第二个是遍历出来的对象

            $.each(data,function (i,obj) {
                //我们手动创建一个li标签 将其数据插入到里面 此时不能再用el表达式了 因为他不是从四大域里面取值
                var  li = "<li><a href='${pageContext.request.contextPath}/productServlet?method=findProductsByCidWithPage&num=1&cid="+obj.cid+"'>"+obj.cname+"</a></li>";
                //再将我们要添加的区域 设置一个id 将其添加进去
                $("#myUL").append(li);
            })

        },"json");
        
    });




</script>
</body>

</html>