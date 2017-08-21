package sy.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.*;
import sy.model.Company;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FetchWebController {
    @RequestMapping("/data")
    public ModelAndView getData(HttpServletRequest req) throws UnsupportedEncodingException {
    	String page = req.getParameter("page");
    	String search = req.getParameter("search");
    	String encode_search = new String(java.net.URLEncoder.encode(search,"utf-8").getBytes());
    	System.out.println(search + "----" +encode_search);
    	ModelAndView result = new ModelAndView("list");
    	List<Company> list = new ArrayList<Company>();
    	PrintWriter pout = null;
		BufferedReader pin = null;
		try {
			for(int k = 1;k < 31;k ++){
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
		    //pout.print("complexname=杭州&$reload=0&$pg=3&$pgsz=15");
		    if(page == null) {
		    	page = "1";
		    	pout.print("complexname="+encode_search+"&$reload=0&$pg=1&$pgsz=15");
		    } else {
		    	pout.print("complexname="+encode_search+"&$reload=0&$pg="+page+"&$pgsz=15");
		    			
		    }
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
		    		
		    		print = true;
		    		continue;
		    	}
		    	if(print) {
		    		if(line.contains("<tr")||line.contains("</tr>")){
		    			sb.append("\n"+line.replace("\t", ""));
		    		} else {
		    			sb.append(line.replace("\t", ""));
		    		}
		    	}
		    	if(line.contains("</tbody")) {
		    		break;
		    	}
		    	//System.out.println(line);
		    }
		    System.out.println(sb.toString());
		    String html_code = "<td class=\"text-left complist-num\" data-header=\"统一社会信用代码\" >";
		    String html_name = "</td><td class=\"text-left primary\" data-header=\"企业名称\">";		
		    String html_people = "</a></td><td data-header=\"企业法定代表人\">";
		    String html_place = "</td><td data-header=\"企业注册属地\">";
		    String html_a = "<a target=\"_blank\" href=\"/dataservice/query/comp/compDetail/([0-9]+)\">";
		    String html_td = "</td>";
		    String html_tr = "</tr>";
		    String json = sb.toString().replaceAll("<tr><td data-header=\"序号\">([0-9]{1,3})</td>","");		    
		    json = json.replaceAll(html_a, "");		    
		    json = json.replace("<em class='matched'>", "");		    
		    json = json.replace("</em>", "");
		    json = json.replaceAll(html_code, "{\"code\":\"");
		    json = json.replaceAll(html_name, "\",\"name\":\"");
		    json = json.replaceAll(html_people, "\",\"boss\":\"");
		    json = json.replaceAll(html_place, "\",\"place\":\"");
		    json = json.replace(html_tr, "%");
		    json = json.replace(html_td, "\"}");
		    String[] arr = json.split("%");
		    
		    for(int i = 0;i < arr.length-1;i ++) {
		    	//System.out.println(i+":"+arr[i]);
		    	JSONObject jn = JSON.parseObject(arr[i]);
		    	Company cmp = new Company();
		    	cmp.setName(jn.getString("name"));
			    cmp.setBoss(jn.getString("boss"));
			    cmp.setPlace(jn.getString("place"));
			    cmp.setCode(jn.getString("code"));
			    list.add(cmp);
		    }
		    Pattern p = Pattern.compile("<a target=\"_blank\" href=\"/dataservice/query/comp/compDetail/([0-9]+)\">");
		    Matcher m = p.matcher(sb.toString());
		    for(int i = 0;i < list.size();i ++) {
		    	m.find();
		    	list.get(i).setWid(m.group(1));
		    }
		    int page_num = Integer.parseInt(page);
		    int pre_page = page_num-1;
		    int next_page = page_num+1;
		    String pre = "<a href=\"http://localhost:8080/JZGSCX/data?page="+pre_page+"&search="+encode_search+"\">上一页</a>";
		    String next = "<a href=\"http://localhost:8080/JZGSCX/data?page="+next_page+"&search="+encode_search+"\">下一页</a>";
		    System.out.println(pre);
		    System.out.println(next);
		    result.addObject("rows", list);
		    result.addObject("pre_page", pre);
		    result.addObject("next_page", next);
			}
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
		return result;
    }
}
