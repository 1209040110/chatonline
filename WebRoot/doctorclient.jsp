<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>医师咨询</title>
    <!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
   <script type="text/javascript" src="js/jquery-1.11.0.js"></script>
    <!--<script src="js/sha1.js"></script>-->
    <script src="js/bootstrap.js"></script>
    <link rel="stylesheet" type="text/css" href="css/font.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/button.css">
    <script src="js/jquery-1.11.0.js"></script>
    <script type="text/javascript">
        var ws = null;
        function startWebSocket() {
            var uname = $('#uname').val();
            var objname='empty';
            console.log(objname);
            if (uname == null || objname == null) {
                alert('请输入用户id！');
            } else {
                if ('WebSocket' in window)
                    ws = new WebSocket("ws://127.0.0.1:8080/chatonline/myShow/" + uname+'/'+objname);
                else if ('MozWebSocket' in window)
                    ws = new MozWebSocket("ws://127.0.0.1:8080/chatonline/myShow/"+uname+'/'+objname);
                else
                    alert("not support");
                if(ws!=null){
                    $("#inputdiv").css("visibility",'hidden');
                }
                ws.onmessage = function (evt) {
                    //alert(evt.data);
                    $("#chatcontent").append("<p>" + evt.data + "</p>");
      /*              var len = obj.unames.length;
                    if (len > 0) {
                        var str = '';
                        for (var i = 1; i <= len; i++) {
                            str += "<li>" + obj.unames[i - 1] + "</li>";
                        }
                        $("#userset").html(str);
                    }*/
                };

                ws.onclose = function (evt) {
                   // alert("close");
                };

                ws.onopen = function (evt) {
                   // alert(evt);
                };
                ws.error = function (evt) {
                    console(evt.data);
                };
            }
        }
        function sendMsg() {
            ws.send(document.getElementById('writeMsg').value);
        }
        function cls() {
            ws.close();
        }
    </script>
    <style>
        .container {
            width: 700px;
        }

        .left,.right {
            float: left;
        }

        .container,.leftTop,.leftBottom,.right {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<div class="container">
    <div id="inputdiv">
        请输入你的用户id(必须为英文或数字，不能为中文)：<input id="uname" type="text" /> 
        
        &nbsp;&nbsp;<button onclick="startWebSocket()">确定</button>&nbsp;&nbsp;&nbsp;
      
    </div>
    <div class="left">
        <div class="leftTop">
            <div id="chatcontent" style="width: 700px;height: 600px; overflow: scroll;"></div>
        </div>
        <div class="leftBottom" style="width:700px; height: 100px;">
            <textarea name="" id="writeMsg" style="width: 580px; height: 100px;"></textarea>
            <button class="button button-pill button-primary"
                    onclick="sendMsg()">发送</button>
        </div>
    </div>
   
</div>




</body>
</html>
