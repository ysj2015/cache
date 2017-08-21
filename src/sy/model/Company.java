package sy.model;

public class Company {
	private Integer id;
	private String place;
	private String name;
	private String boss;
	//ÍøÕ¾ÏêÏ¸Ò³ÃæµÄID
	private String wid;
	private String code;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBoss() {
		return boss;
	}
	public void setBoss(String boss) {
		this.boss = boss;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String toString() {
		return String.format("[name:%s,place:%s,code:%s,boss:%s]",name,place,code,boss);
	}
}
