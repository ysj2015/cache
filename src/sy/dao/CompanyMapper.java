package sy.dao;

import sy.model.Company;
import java.util.*;

public interface CompanyMapper {
	int insert(Company record);
	int updateByPrimaryKey(Company record);
	List<Company> search(String name);
	List<Company> searchByNameTime(Map<String,Object> map);
}
