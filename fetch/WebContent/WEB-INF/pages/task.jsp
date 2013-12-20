<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css" />
		<style type="text/css" title="currentStyle">
			@import "css/demo_page.css";
			@import "css/demo_table.css";
		</style>
		<script type="text/javascript"  src="js/jquery.js"></script>
		<script type="text/javascript"  src="js/jquery.dataTables.js"></script>
		<script type="text/javascript" charset="utf-8">
			$(document).ready(function() {
				$('#tasks').dataTable();
			} );
			function send(taskid){
 				$.ajax({
					type : "POST",
					url : "send.do",
					data : "id=" + taskid,
					success : function(msg) {
						if(msg=='false'){
							alert("send failed!");
						}else{
							alert("send ok!");
						}
					},
					error : function(msg){
						alert("send error!");
					}
				}); 
			}
		</script>
<title>Tasks</title>
</head>
<body>
	<div>
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="tasks" width="100%">
			<thead>
				<tr>
					<th  width="50%">title</th>
					<th >url</th>
					<th >status</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="task">
					<tr>
						<td class="field">${task.title}</td>
						<td class="field">${task.url}</td>
						<td class="field">
						<c:choose>
							<c:when test="${task.state ==0}">
								<button id="send" onclick="send(${task.id})">send</button>
							</c:when>
						</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>