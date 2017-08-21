package sy.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import sy.model.Company;
import sy.task.GetDetailRunnable;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;

@Controller
public class TestController {
    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
	@RequestMapping("/getData")
	public Map test()
	{
		Map<String,Object> m = new HashMap<String,Object>();
		String[] arr = new String[] {
			"http://jzsc.mohurd.gov.cn/dataservice/query/comp/compDetail/001607220057278907",
			"http://jzsc.mohurd.gov.cn/dataservice/query/comp/compDetail/001607220057324056",
			"http://jzsc.mohurd.gov.cn/dataservice/query/comp/compDetail/001607220057327206",
			"http://jzsc.mohurd.gov.cn/dataservice/query/comp/compDetail/001607220057288243",
			"http://jzsc.mohurd.gov.cn/dataservice/query/comp/compDetail/001607220057340102"
		};
		for(int i = 0;i < arr.length;i ++) {
			GetDetailRunnable run = new GetDetailRunnable();
			run.setUrl(arr[i]);
			taskExecutor.execute(run);
		}
		System.out.println("提交线程完毕");
		
		return m;
		
	}
	@RequestMapping("/test1")
	public void test1(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("test1");
		Map<String,Object> m = new HashMap<String,Object>();
		Company c = new Company();
		long t = new Date().getTime();
		long num = t%100;
		c.setName("MM"+num);
		m.put("result", true);
		m.put("data", c);
		System.out.println(JSON.toJSONString(m));
		PrintWriter writer = null;
		try{
			writer = response.getWriter(); 
			
			writer.print(JSON.toJSONString(m));
		} catch(Exception e) {
			
		}
		finally {
			if(null != writer) {
				writer.flush();
				writer.close();
			}
		}
	}
}
