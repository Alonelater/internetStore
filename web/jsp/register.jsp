<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head></head>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员注册</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
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

    .container .row div {
        /* position:relative;
        float:left; */
    }

    font {
        color: #3164af;
        font-size: 18px;
        font-weight: normal;
        padding: 0 10px;
    }

    .red {
        color: red;
        font-size: 12px;
    }

    .green {
        color: green;
        font-size: 12px;
    }
</style>

</head>
<body>


<%--头部信息通过动态包含加载header.jsp--%>
<%@include file="/jsp/header.jsp" %>


<div class="container" style="width:100%;background:url('${pageContext.request.contextPath}/img/regist_bg.jpg');">
    <div class="row">

        <div class="col-md-2"></div>


        <div class="col-md-8" style="background:#fff;padding:40px 80px;margin:30px;border:7px solid #ccc;">
            <font>会员注册</font>USER REGISTER
            <form class="form-horizontal" style="margin-top:5px;"
                  action="${pageContext.request.contextPath}/userServlet?method=userRegist" method="post" id="myform">
                <div class="form-group">
                    <label for="username" class="col-sm-2 control-label">用户名</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名"
                               onblur="checkUserName()"><span id="span"></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputPassword3" class="col-sm-2 control-label">密码</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" name="password" id="inputPassword3"
                               placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group">
                    <label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" id="confirmpwd" placeholder="请输入确认密码">
                    </div>
                </div>
                <div class="form-group">
                    <label for="usercaption" class="col-sm-2 control-label">姓名</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="usercaption" name="name" placeholder="请输入姓名">
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-6">
                        <input type="email" class="form-control" id="inputEmail3" name="email" placeholder="Email">
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">电话</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="inputtelephone" name="telephone" placeholder="">
                    </div>
                </div>
                <div class="form-group opt">
                    <label for="inlineRadio1" class="col-sm-2 control-label">性别</label>
                    <div class="col-sm-6">
                        <label class="radio-inline">
                            <input type="radio" name="sex" id="inlineRadio1" value="男" checked="checked"> 男
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="sex" id="inlineRadio2" value="女"> 女
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="date" class="col-sm-2 control-label">出生日期</label>
                    <div class="col-sm-6">
                        <input type="date" name="birthday" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <label for="date" class="col-sm-2 control-label">验证码</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control">

                    </div>
                    <div class="col-sm-2">
                        <img src="${pageContext.request.contextPath}/img/captcha.jhtml"/>
                    </div>

                </div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="button" width="100" value="注册" id="btn"   border="0"
                               style="background: url('${pageContext.request.contextPath}/img/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0);
                                       height:35px;width:100px;color:white;">
                    </div>
                </div>
            </form>
        </div>

        <div class="col-md-2"></div>

    </div>
</div>


<%--尾部信息通过动态包含加载foot.jsp--%>
<%@include file="/jsp/footer.jsp" %>

<script type="text/javascript">

    $(function () {
        /*是否能成功提交表单的标记*/
        var flag = false;
        var username;
        $("#username").blur(function () {
             username = $("#username").val().trim();
            alert(username);
            if (username == '' || username.length == 0) {
                $("#span").html("<font style='color:red'>用户名不能为空</font>");
                return;
            }else {
                checkUserName(username);

            }
        });


        function checkUserName() {

            $.ajax({
                url: "userServlet?username=" + username + "&method=ajax",
                type: "get",
                dataType:"json",
                success: function (data) {
                    alert(data);
                    if (data == 1) {
                        $("#span").html("<font class='red'>用户名已经被注册,请更换</font>");
                        flag= false;
                    } else {
                        $("#span").html("<font class='green'>用户名可用</font>");
                        flag=true;
                    }
                }
                
            });

        };

        $("#btn").click(function () {
            if (flag){
                alert(44444444444444)
                $("#myform").submit();
            }
        });

    });



</script>
</body>
</html>




