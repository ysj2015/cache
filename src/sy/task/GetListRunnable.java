package sy.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import sy.model.Company;

public class GetListRunnable implements Runnable {
	String search;
	String reg = "<td colspan=\"3\" data-header=\"企业经营地址\">([^<]+)</td>";
	String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/easyui?useUnicode=true&characterEncoding=UTF-8";
    String username = "root";
    String password = "phpmysql";
    Connection conn = null;
    long time;
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	@Override
	public void run() {
		time = new Date().getTime() + 3600*1000;
		try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        return;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return;
	    }
		int page = 1;
		
		while(page < 30){
			PrintWriter pout = null;
			BufferedReader pin = null;
			try {
				String encode_search = new String(java.net.URLEncoder.encode(search,"utf-8").getBytes());
				System.out.println(search + "----" +encode_search);
			
			
			    URL realUrl = new URL("http://jzsc.mohurd.gov.cn/dataservice/query/comp/list");
			    // 打开和URL之间的连接
			    URLConnection conn = realUrl.openConnection();
			    // 设置通用的请求属性
			    conn.setRequestProperty("accept", "*/*");
			    conn.setRequestProperty("connection", "Keep-Alive");
			    conn.setRequestProperty("user-agent",
			            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			    // 发送POST请求必须设置如下两行
			    conn.setDoOutput(true);
			    conn.setDoInput(true);
			    // 获取URLConnection对象对应的输出流
			    pout = new PrintWriter(conn.getOutputStream());
			    //pout = new PrintWriter(conn.getOutputStream());
			    // 发送请求参数
			    pout.print("complexname="+encode_search+"&$reload=0&$pg="+page+"&$pgsz=15");
			    // flush输出流的缓冲
			    pout.flush();
			    // 定义BufferedReader输入流来读取URL的响应
			    pin = new BufferedReader(
			            new InputStreamReader(conn.getInputStream(),"utf-8"));
			    String line;
			    boolean print = false;
			    StringBuilder sb = new StringBuilder("");
			    while ((line = pin.readLine()) != null) {
			    	if(line.contains("<tbody")) {
			    		//sb.append(line);
			    		print = true;
			    		continue;
			    	}
			    	
			    	if(line.contains("</tbody")) {
			    		break;
			    	}
			    	if(print) {
			    		System.out.println(line);
			    		sb.append(line);
			    	}
			    	//System.out.println(line);
			    }
			    System.out.println(sb.toString());
			    String html_code = "<td class=\"text-left complist-num\" data-header=\"统一社会信用代码\">";
			    String html_name = "</td><td class=\"text-left primary\" data-header=\"企业名称\">";		
			    String html_people = "</td><td data-header=\"企业法定代表人\">";
			    String html_place = "</td><td data-header=\"企业注册属地\">";
			    String html_a = "<a target=\"_blank\" href=\"/dataservice/query/comp/compDetail/";
			    String html_td = "</td>";
			    String html_tr = "</tr>";
			    String json = sb.toString().replace("\t", "");
			    json = json.replace("\n", "");
			    json = json.replace("\r", "");
			    System.out.println(json);
			    String[] arr = json.split("</tr><tr>");
			    for(int i = 0;i <arr.length;i ++) {
			    	String str_json = arr[i].replace("<em class='matched'>", "").replace("</em>", "")
			    		.replace("<tr>", "").replace("</tr>", "").replace("</a>", "")
			    		.replaceAll("<td data-header=\"序号\">([0-9]{1,3})</td>", "")
			    		.replace(html_code, "{\"code\":\"")
			    		.replace(html_name, "")
			    		.replace(html_a, "\",\"wid\":\"")
			    		.replace(html_people, "\",\"boss\":\"")
			    		.replace(html_place, "\",\"place\":\"")
			    		.replace(html_td, "\"}")
			    		.replace(">", ",\"name\":\"");
			    	System.out.println(i+":"+str_json);
			    	JSONObject jn = JSON.parseObject(str_json);
			    	Company dto = new Company();
			    	dto.setName(jn.getString("name"));
			    	dto.setWid(jn.getString("wid"));
				    dto.setBoss(jn.getString("boss"));
				    dto.setPlace(jn.getString("place"));
				    dto.setCode(jn.getString("code"));
				    insert(dto);
			    }
			    
			} catch (Exception e) {
			    //pout.println("发送 POST 请求出现异常！"+e);
			    e.printStackTrace();
			}
			//使用finally块来关闭输出流、输入流
			finally{
			    try{
			        if(pout != null){
			            pout.close();
			        }
			        if(pin != null){
			            pin.close();
			        }
			    }
			    catch(IOException ex){
			        ex.printStackTrace();
			    }
			}
			page++;
		}
		try	{
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private int insert(Company com) {
	    int i = 0;
	    java.sql.Date d = new java.sql.Date(time);
	    String sql = "insert into Company(boss,name,code,place,wid,expire_time) values(?,?,?,?,?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, com.getBoss());
	        pstmt.setString(2, com.getName());
	        pstmt.setString(3, com.getCode());
	        pstmt.setString(4, com.getPlace());
	        pstmt.setString(5, com.getWid());
	        pstmt.setDate(6, d);
	        i = pstmt.executeUpdate();
	        pstmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
	
}
