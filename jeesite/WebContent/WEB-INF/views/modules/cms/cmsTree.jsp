<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>栏目列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<style type="text/css">
		.ztree {margin-top:5px;*margin:-5px 0 5px -2px;_position:relative;_margin:0;_top:-10px;overflow:hidden;}<%--
		.ztree li span.button.level0, .ztree li a.level0 {display:none;height:0;}
		.ztree li ul.level0 {padding:0;background:none;}--%>
	</style>
	<script type="text/javascript">
		$(document).ready(function(){
			var setting = {view:{selectedMulti:false},data:{simpleData:{enable:true}}};
			var zNodes=[
		            <c:forEach items="${categoryList}" var="category">{id:${category.id}, pId:${not empty category.parent?category.parent.id:0}, name:"${category.name}", url:"${ctx}/cms/${not empty category.module?category.module:'none'}/?category.id=${category.id}", target:"cmsMainFrame"},
		            </c:forEach>];
			for(var i=0; i<zNodes.length; i++) {
				// 移除父节点
				if (zNodes[i].id == 1){
					zNodes.splice(i, 1);
				}<%--
				// 并将没有关联关系的父节点，改为父节点
				var isExistParent = false;
				for(var j=0; j<zNodes.length; j++) {
					if (zNodes[i].pId == zNodes[j].id){
						isExistParent = true;
						break;
					}
				}
				if (!isExistParent){
					zNodes[i].pId = 1;
				}--%>
			}
			// 初始化树结构
			var tree = $.fn.zTree.init($("#tree"), setting, zNodes);
			// 展开第一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, true, false);
			}
			// 展开第二级节点
			nodes = tree.getNodesByParam("level", 1);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, true, false);
			}
		});
	</script>
</head>
<body>
	<div class="accordion-group">
	    <div class="accordion-heading">
	    	<a class="accordion-toggle">栏目列表</a>
	    </div>
	    <div class="accordion-body">
			<div class="accordion-inner">
				<div id="tree" class="ztree"></div>
			</div>
	    </div>
	</div>
</body>
</html>