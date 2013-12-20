<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>普通搜索</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#field0").val("CONTENT");
	});
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
    	return false;
    }
	</script>
	<style type="text/css">
		a:link {
		color:'#0000d5';
		text-decoration:underline;
		}
		font{
		size:3;
		}
	</style>
</head>
<body>
	<div>
		<form:form id="searchForm" modelAttribute="user" action="${ctx}/retrieval/search/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="keyword" name="keyword" type="text" maxlength="200" class="input-medium" style="width:450px" value="${simpleQuery.keyword}"/>
			<!-- 标题字段 -->
			<input id="titleField" name="titleField" type="hidden" value="TITLE"/>
			<!-- 内容字段 -->
			<input id="contentField" name="ResumeField" type="hidden" value="CONTENT"/>
			<!-- 作为条件的查询字段 -->
			<c:forEach items="CONTENT,TITLE" var="field" varStatus="status">
				<input name="simpleItems[${status.index}].field" type="hidden" value="${field}"/>
			</c:forEach>
			<!-- 需要附带查询出的字段 -->
			<c:forEach items="PAGE_URL,CREATETIME" var="field" varStatus="status">
				<input name="queryFields[${status.index}]" type="hidden" value="${field}"/>
			</c:forEach>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="搜索" onclick="return page();"/>
		</form:form>
	</div>
	<div class="pagination">${page}</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<c:forEach items="${page.list}" var="retrievalPage">
			<tr>
				<td>
					<div style="font-size:medium;font-weight:normal"><a href="${retrievalPage.retrievalResultFields['PAGE_URL']}" target="_blank">${retrievalPage.title}</a></div>
					<div>${retrievalPage.content}</div>
					<div><font color="#008000">${retrievalPage.retrievalResultFields['PAGE_URL']} ${retrievalPage.retrievalResultFields['CREATETIME']}</font></div>
				</td>
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>