<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/18 0018
  Time: 17:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

<head>
    <meta charset="UTF-8">
    <title>上传文件</title>
    <script src="https://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
    <script>
       function UpladFile() {
           alert(111);
           var fileObj = document.getElementById("file").files[0];  //js获取文件对象
           alert(fileObj);
           var fd=new FormData();   //FormData对象
           fd.append("file",fileObj); //文件对象
           var xhr=new XMLHttpRequest(); //XmlHttpRequest对象
           xhr.open("post","/UploadFilesServlet",true);
           xhr.onload=uploadComplete(evt);   //请求完成
           xhr.onerror=uploadFailed();    //请求失败
           xhr.upload.onprogress=progressFunction(); //上传进度调用方法实现
           xhr.upload.onloadstart=function () { //上传开始执行方法
                ot=new Date().getTime();    //设置上传开始时间
                oloaded=0;  //设置上传开始时,以上传的文件大小为0.
           };
           xhr.send(fd);    //开始上传,发送form数据

           //上传进度实现方法,上传过程中会频繁调用该方法
           function progressFunction(evt) {
               var progressBar=$("#progressBar");
               var percentageDiv=$("#percentage");
               // event.total是需要传输的总字节，event.loaded是已经传输的字节。
               // 如果event.lengthComputable不为真，则event.total等于0
               if(evt.lengthComputable){
                   progressBar.max =evt.total;
                   progressBar.value=evt.loaded;
                   percentageDiv.innerHTML=Math.round(evt.loaded/evt.total*100)+"%";
               }
               var time=$("#time");
               var nt=new Date().getTime(); //获取当前时间
               var pertime=(nt-ot)/1000;    //计算时间差,单位为秒
               ot=new Date().getTime(); //重新赋值时间，用于下次计算

               //上传速度计算
               var speed=perload/pertime;   //单位b/s
               var bspeed=speed;
               var units='b/s'; //单位名称
               if(speed/1024>1){
                   speed=speed/1024;
                   units='k/s';
               }
               if(speed/1024>1){
                   speed=speed/1024;
                   units='M/s';
               }
               speed=speed.toFixed(1);
               //剩余时间
               var resttime=((evt.total-evt.loaded)/bspeed).toFixed(1);
               time.innerHTML='，速度：'+speed+units+'，剩余时间：'+resttime+'s';
               if(bspeed==0){
                   time.innerHTML="上传已取消";
               }
               //上传成功响应
               function uploadComplete(evt) {
                   //服务器接收完文件返回的结果
                   alert(evt.target.responseText);
                   alert("上传成功");
               }
               //上传失败
               function uploadFailed(evt) {
                   alert("上传失败!")
               }
               //取消上传
               function cancleUploadFile() {
                   xhr.abort();
               }
           }

           xhr.onreadystatechange=function (){
               if(this.readyState==4){
                   document.getElementById('precent').innerHTML=this.responseText;
               }
           }

       }

    </script>
</head>
<body>
<h3>上传文件到阿里云OSS</h3>
<form action="UploadFilesServlet" method="post" enctype="multipart/form-data">
   <%-- <input type="file" name="file" id="file1" onchange="upfile1(this);" multiple>
    <div id="wai1">
        <div id="nei1"></div>
    </div>
    <span id="precent1"></span><button type="button" id="btn1">上传</button>
    <hr />
    <input type="file" name="file2" id="file2">
    <div id="wai2">
        <div id="nei2"></div>
    </div>
    <span id="precent2"></span><button type="button" id="btn2">上传</button>
    <hr />--%>
       <progress id="progressBar" value="0" max="100" style="width: 300px;"></progress>
       <span id="percentage"></span><span id="time"></span>
       <br /><br />
       <input type="file" id="file" name="myfile" />
       <input type="button" onclick="UpladFile()" value="上传" />
       <input type="button" onclick="cancleUploadFile()" value="取消" />
    <input type="submit" value="全部上传" id="form">
</form>
</body>
</html>
