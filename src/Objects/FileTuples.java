package Objects;

import java.util.Date;

public class FileTuples {
	String name, hash;
	Date updateDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public FileTuples(String name, Date updateDate){
		setName(name);
		setUpdateDate(updateDate);
	}
	@Override
	public String toString() {
		return "FileTuples [name=" + name + ", updateDate=" + updateDate + "]";
	}
	
}
