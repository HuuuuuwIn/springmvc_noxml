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
    <title>添加任务</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <%--<script src="/js/jquery-1.11.3.js" style="text/javascript"/>--%>
</head>
<body>
  <form <c:choose><c:when test="${not empty task.id}">action="/task/update"</c:when>
        <c:otherwise>action="/task/add"</c:otherwise></c:choose> method="post">
    <input type="hidden" name="id" value="${task.id}">
    <table>
      <tr>
        <td align = "center">任务名</td>
        <td>
          <input type="text" name="name" value="${task.name}">
        </td>
      </tr>
      <tr>
        <td align = "center">任务组</td>
        <td>
          <input type="text" name="group" value="${task.group}">
        </td>
      </tr>
      <tr>
        <td align = "center">源地址</td>
        <td>
          <select name="from.id" >
            <c:forEach items="${nodes}" var="node" varStatus="vs">
              <option value="${node.id }">
                  ${node.name}
              </option>
            </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <td align = "center">源文件夹</td>
        <td>
          <input type="text" name="fromDir" value="${task.fromDir}">
        </td>
      </tr>
      <tr>
        <td align = "center">文件名</td>
        <td>
          <input type="text" name="fileName" value="${task.fileName}">
        </td>
      </tr>
      <tr>
        <td align = "center">目标文件夹</td>
        <td>
          <input type="text" name="toDir" value="${task.toDir}">
        </td>
      </tr>
      <tr>
        <td align = "center">执行时间表达式</td>
        <td>
          <input type="text" name="cronTime" value="${task.cronTime}">
        </td>
      </tr>
      <tr>
        <td align = "center">是否启用</td>
        <td>
          <input type="radio" name="enable" value="true" <c:if test="${task.enable}">checked="checked"</c:if>>是
          <input type="radio" name="enable" value="false" <c:if test="${!task.enable}">checked="checked"</c:if>>否
        </td>
      </tr>
      <tr>
        <td align = "center">是否同步</td>
        <td>
          <input type="radio" name="sync" value="true" <c:if test="${task.sync}">checked="checked"</c:if>>是
          <input type="radio" name="sync" value="false" <c:if test="${!task.sync}">checked="checked"</c:if>>否
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <input type="submit" value="确定">
        </td>
      </tr>
    </table>
  </form>
</body>
</html>
