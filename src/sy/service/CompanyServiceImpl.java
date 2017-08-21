package sy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import sy.dao.CompanyMapper;
import sy.model.Company;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	private CompanyMapper companyMapper;
	
	public CompanyMapper getCompanyMapper() {
		return companyMapper;
	}
	@Autowired
	public void setCompanyMapper(CompanyMapper companyMapper) {
		this.companyMapper = companyMapper;
	}

	@Override
	public int insert(Company com) {
		// TODO Auto-generated method stub
		return companyMapper.insert(com);
	}

	@Override
	public List<Company> select(String search) {
		return companyMapper.search("%"+search+"%");
	}
	@Override
	public List<Company> searchByNameTime(Map<String,Object> map) {
		return companyMapper.searchByNameTime(map);
	}
}
