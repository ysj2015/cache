package sy.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sy.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
//∏ÊÀﬂjunit spring≈‰÷√Œƒº˛
@ContextConfiguration({ "classpath:spring-mybatis.xml", "classpath:spring.xml" })
public class CompanyServiceTest {
	@Autowired
	private CompanyService service;
	@Test
	public void testInsert() {
		Company c = new Company();
		c.setName("ppp");
		c.setBoss("yu");
		service.insert(c);
	}
}
