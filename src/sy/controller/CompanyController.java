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

import com.alibaba.fastjson.*;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import sy.model.Company;
import sy.service.CompanyService;
import sy.task.*;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;

@Controller
public class CompanyController {
	@Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
	private CompanyService companyService;
	public CompanyService getCompanyService() {
        return companyService;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
	@RequestMapping("/search_company")
	public void test1(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String,Object> m = new HashMap<String,Object>();
		String search = request.getParameter("search");
		if(request.getParameter("already_showed_id") !=null) {
			System.out.println(request.getParameter("already_showed_id"));
		}
		System.out.println(search);
		List<Company> list = companyService.select("%"+search+"%");
		response.setContentType("text/html;charset=utf-8");
		if(list.size() == 0) {			
			m.put("result", 1);
			response.getWriter().print(JSON.toJSONString(m));
			GetListRunnable run = new GetListRunnable();
			run.setSearch(search);
			taskExecutor.execute(run);
		} else {
			m.put("result", 2);
			m.put("data", list);
			response.getWriter().print(JSON.toJSONString(m));
		}
		
	//1:db中暂未找到，要到网站去抓取(已实现，测试通过)
	//2:db中有且都未过有效期
	//3:db中有，部分过期
	//4:db中有，全部过期
		//System.out.println(JSON.toJSONString(list));已测试通过
	}
	@RequestMapping("/test2")
	public void test2(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String,Object> searchMap = new HashMap<String,Object>();
		String search = request.getParameter("search");
		searchMap.put("com_name", "%"+search+"%");
		searchMap.put("com_time", new Date().getTime());
		List<Company> list = companyService.searchByNameTime(searchMap);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("result", 2);
		resultMap.put("data", list);
		System.out.println(JSON.toJSONString(list));
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(JSON.toJSONString(resultMap));
	}
	
}
