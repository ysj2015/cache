package sy.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import sy.service.CompanyService;
import sy.model.Company;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@ContextConfiguration({ "classpath:spring-mybatis.xml", "classpath:spring.xml" })
public class GetDetailRunnable implements Runnable {
	private CompanyService companyService;
	@Autowired
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
	public CompanyService getCompanyService() {
		return companyService;
	}
	String html_code = "<td colspan=\"3\" data-header=\"统一社会信用代码\">";
	String html_boss = "<td data-header=\"企业法定代表人\">";
	String html_place = "<td data-header=\"企业登记注册类型\">";
	
	private String url;	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		PrintWriter pout = null;
		BufferedReader pin = null;
		try {
		    URL realUrl = new URL(url);
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
		    // flush输出流的缓冲
		    pout.flush();
		    // 定义BufferedReader输入流来读取URL的响应
		    pin = new BufferedReader(
		            new InputStreamReader(conn.getInputStream(),"utf-8"));
		    String line;
		    boolean print = false;
		    StringBuilder sb = new StringBuilder("");
		    Company com = new Company();
		    while ((line = pin.readLine()) != null) {
		    	if(line.contains("<tbody>")) {
		    		print = true;
		    		continue;
		    	}
		    	if(print) {
				    System.out.println(line);
		    		if(line.contains(html_code)) {
		    			com.setCode(line.replace(html_code, "")
		    					.replace("</td>", "")
		    					.replace("\t", ""));
		    		}
		    		if(line.contains(html_boss)) {
		    			com.setBoss(line.replace(html_boss, "")
		    					.replace("</td>", "")
		    					.replace("\t", ""));
		    		}
		    	}
		    	if(line.contains("</tbody>")) {
		    		break;
		    	}
		    }
		    insert(com);
//		    companyService.insert(com);
		} catch (Exception e) {
		    pout.println("发送 POST 请求出现异常！"+e);
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
	}
	private Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306/easyui";
	    String username = "root";
	    String password = "phpmysql";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	private int insert(Company com) {
	    Connection conn = getConn();
	    int i = 0;
	    String sql = "insert into Company(boss,name,code,wid,place) values(?,?,?,?,?)";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.setString(1, com.getBoss());
	        pstmt.setString(2, com.getName());
	        pstmt.setString(3, com.getCode());
	        pstmt.setString(4, com.getWid());
	        pstmt.setString(5, com.getPlace());
	        i = pstmt.executeUpdate();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return i;
	}
}
