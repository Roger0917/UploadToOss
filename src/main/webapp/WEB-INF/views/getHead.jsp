<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/22 0022
  Time: 0:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!--jstl获取所有Header信息 -->
<h2>循环输出Map打印Header</h2
<c:forEach items="${header}" var="name">
    ${name.key }:${name.value }<br/>
</c:forEach>
<h2>打印Cookie</h2>
当前时间:<fmt:formatDate value="${date}" type="date" pattern="yyyy-MM-dd HH:mm:ss E"/>(日期格式化)<hr />
Cookie名:${cookie.color1.name}Cookie值:${cookie.color1.name}<br />
Cookie名:${cookie.color2.name}Cookie值:<fmt:formatNumber type="number">${cookie.color2.value}</fmt:formatNumber>(数字格式化)
</body>
</html>