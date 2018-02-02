<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
</head>
<meta charset="UTF-8">
<body>
<h2>Hello World!</h2>
<a href="${pageContext.request.contextPath}/getRemoteIPServlet?method=setCookie">获取客户端真实ip</a>
<a href="${pageContext.request.contextPath}/UploadFilesServlet?method=uploadFile">上传文件至阿里云OSS</a>
<a href="${pageContext.request.contextPath}/printHeadServlet?method=setCookie">JSTL打印Head,Cookie</a>
</body>
</html>
