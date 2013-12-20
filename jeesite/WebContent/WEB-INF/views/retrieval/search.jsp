<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>普通搜索</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		var titleField = "TITLE";
		var resumeField = "CONTENT";
		$("#btn").click(function(){
			var keyword = $("#keyword").val();
			var searchQuery = {'keyword':keyword,'titleField':titleField,'resumeField':resumeField,'resumeLength':200,'simpleItems':[{'field':titleField,'relationType':''},{'field':resumeField}],'queryFields':['PAGE_URL','CREATETIME']};
			$.ajax(
		            {
		              url:"list/json", 
		              type: "POST", 
		              data: JSON.stringify( searchQuery ), 
		              dataType: "json",
		              contentType: "application/json",
		              success:function(data){
		            	  $('.tcf').remove();
		            	  $.each(data.retrievalPageList, function(i,retrievalPage){  
		            	      $("#main").append("<div class='tcf' id='s"+i+"'>"+
		            	    		  "<div id='t"+i+"' style='font-size:medium;font-weight:normal'><a href='"+retrievalPage.retrievalResultFields['PAGE_URL']+"' target='_blank'>"+retrievalPage.title+"</a></div>"+
		            	    		  "<div id='c"+i+"'>"+retrievalPage.content+"...</div>"+
		            	    		  "<div id='f"+i+"'><font color='#008000'>"+retrievalPage.retrievalResultFields['PAGE_URL']+retrievalPage.retrievalResultFields['CREATETIME']+"</font></div>"
		            	      +"</div>");
		            	  });   
		            	 $("#info").html("<font color='#999999'>找到约"+data.count+"条结果 (用时"+data.time+"秒)</font>");
					  },
					  error:function(XMLHttpRequest, textStatus, errorThrown){
						alert(errorThrown);	
					  }
		            } );  
		});
		
		$("#keyword").autocomplete("auto", {
			width: 450,
			selectFirst: false
		});
		$("#keyword").result(function(event, data, formatted) {
			if (data)
				$(this).parent().next().find("input").val(data[1]);
		});
	});
	</script>
	<style type="text/css">
		a:link {
		color:'#0000d5';
		text-decoration:underline;
		}
		font{
		size:3;
		}
		.tcf{
		margin-left:30px;
		margin-top:10px;
		width:65%;
		}
	</style>
</head>
<body>
	<div id="top" style="clear:both">
		<div id="logo" style="margin-left:30px;margin-top:15px;float:left"><font size="5" color="#0064CC">全文检索</font></div>
		<div style="margin-left:30px;margin-top:10px;float:left">
			<form id="searchForm" class="form-search">
				<input id="keyword"  type="text" maxlength="200" class="input-medium" style="width:450px" value="${simpleQuery.keyword}"/>
				<input id="btn" class="btn btn-primary" type="button" value="搜索"/>
			</form>
		</div>
		<div id="info" style="margin-left:30px;clear:both"></div>
	</div>
	<div id="main" ></div>
</body>
</html>