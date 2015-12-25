<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/12/23
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>任务列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <%--<script src="/js/jquery-1.11.3.js" style="text/javascript"/>--%>
</head>
<body>
  <a href="/task/add">添加任务</a>
  <table border="1">
    <thead>
      <td>ID</td>
      <td>名称</td>
      <td>组名</td>
      <td>源地址</td>
      <td>时间</td>
      <td>状态</td>
      <td>是否启用</td>
      <td>是否异步</td>
      <td>操作</td>
    </thead>
    <%--<c:if test="${not empty tasks}">--%>
      <%--sssss--%>
    <%--</c:if>--%>
    <c:forEach items="${tasks}" var="task" varStatus="vs">
      <tr>
        <td align = "center">${task.id}</td>
        <td align = "center">${task.name}</td>
        <td align = "center">${task.group}</td>
        <td align = "center">${task.from.name}</td>
        <td align = "center">${task.cronTime}</td>
        <td align = "center">${task.status.desc}${task.status.code}</td>
        <td align = "center">${task.enable}</td>
        <td align = "center">${task.sync}</td>
        <td align = "center">
          <c:if test="${(empty task.status or task.status.code==0) and task.enable}">
            <a href="/task/disable/${task.id}">禁用</a>
            <a href="/task/start/${task.id}">启动</a>
          </c:if>
          <c:if test="${(empty task.status or task.status.code==0) and !task.enable}">
            <a href="/task/enable/${task.id}">启用</a>
          </c:if>
          <c:if test="${(empty task.status or task.status.code==0)}">
            <a href="/task/update/${task.id}">更新</a>
            <a href="/task/delete/${task.id}">删除</a>
          </c:if>
          <c:if test="${task.status.code==1}">
            <a href="/task/pause/${task.id}">暂停</a>
            <a href="/task/stop/${task.id}">停止</a>
          </c:if>
          <c:if test="${task.status.code==2}">
            <a href="/task/resume/${task.id}">恢复</a>
            <a href="/task/stop/${task.id}">停止</a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
  </table>
</body>
</html>
