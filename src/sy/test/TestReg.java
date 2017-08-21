package sy.test;

import java.util.regex.*;

public class TestReg {
	public static void main(String[] args) {
		String s = "<td colspan=\"3\" data-header=\"��ҵ��Ӫ��ַ\">�ൺ��������������·�����Ŷ���</td>";
		String reg = "<td colspan=\"3\" data-header=\"��ҵ��Ӫ��ַ\">([^<]+)</td>";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(s);
		while(m.find()){
			String str = m.group();
            System.out.println(str.replaceAll("<([^<^>]+)>", ""));
            
		}
	}
}
