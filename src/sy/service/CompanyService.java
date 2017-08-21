package sy.service;

import sy.model.Company;
import java.util.List;
import java.util.Map;

public interface CompanyService {
	public int insert(Company com);
	public List<Company> select(String search);
	public List<Company> searchByNameTime(Map<String,Object> map);
}
