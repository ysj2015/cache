package sy.test;

import java.util.regex.*;

public class TestReg {
	public static void main(String[] args) {
		String s = "<td colspan=\"3\" data-header=\"企业经营地址\">青岛市市南区银川西路８８号二层</td>";
		String reg = "<td colspan=\"3\" data-header=\"企业经营地址\">([^<]+)</td>";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(s);
		while(m.find()){
			String str = m.group();
            System.out.println(str.replaceAll("<([^<^>]+)>", ""));
            
		}
	}
}
