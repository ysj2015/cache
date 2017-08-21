<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
<h2>Hello World!</h2>
<div id="content"></div>
<input id="search" name="search" type="text"/>
<input id="last_max_id" name="last_max_id" type="hidden"/>

<button id="submit" onclick="search()">搜索</button>
<br>
<div id="result_table"></div>
<br>
<div id="more" style="display:none">
<button onclick="getMore()">查看更多</button>
</div>
</body>
</html>
<script language="javascript" src="js/jquery-2.0.3.js"></script>
<script language="javascript">
var ids = "";//已经显示的数据的wid
var getData = new Array();//保存后台返回的数据
var j = 0;//点击更多，显示数据的起点
function Company(name,wid,code,place,boss) {
	this.name = name;
	this.wid = wid;
	this.code = code;
	this.place = place;
	this.boss = boss;
}
function showUnreadNews()
{
    /* $(document).ready(function() {
        $.ajax({
            type: "GET",
            url: "test1.do",
            dataType: "json",
            success: function(msg) {
                $("#content").append(msg.data.name);
            }
        });
    }); */
}
function getMore() {
	alert(getData.length);
	var ss="";
	j+=10;
	
	for(var k = j;k < j+10;k ++) {
		if(k>=getData.length){
			$("#more").hide();
			break;
		}
		$("#content").append(getData[k].name+"<br>");
	}
	//alert(ss);
	/* $.ajax({
        type: "POST",
        url: "search_company.do",
        data: "search="+$('#search').val()+"&already_showed_id="+ids,
        dataType: "json",
        success: function(msg) {
            //$("#content").append(msg.data.name);
            //alert(msg.result);
            if(msg.result == 1) {
            	for(var i = 0;i < msg.data.length;i ++) {
            		$("#result_table").append(msg.data[i].name+"<br>");
            	}
            }
        }
    }); */
}
function search() {
	$.ajax({
        type: "POST",
        url: "search_company.do",
        data: "search="+$('#search').val(),
        dataType: "json",
        success: function(msg) {
        	if(msg.result == 1) {
        		$("#more").show();
        	}
        	if(msg.result == 2) {
        		$("#more").show();
        		for(var i = 0;i < msg.data.length;i ++) {
        			var com = new Company(msg.data[i].name,
        					msg.data[i].wid,
        					msg.data[i].code,
        					msg.data[i].place,
        					msg.data[i].boss
        					);
        			getData[i] = com;
        			if(i<10){
        				$("#content").append(msg.data[i].name+"<br>");
        			}
        			/* if(i<10) {
        				ids = ids+ msg.data[i].wid+ " ";
        			} */
        		}
        	}
        	
            //$("#content").append(msg.data.name);
            //alert(msg.result);
            //if(msg.result == 1) {
            	//for(var i = 0;i < msg.data.length;i ++) {
            		
            		//$("#content").append(msg.data[i].name+"<br>");
            	//}
            //}
        }
    });
}
//setInterval('showUnreadNews()',5000);
</script>