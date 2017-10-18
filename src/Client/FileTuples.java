package Client;

import java.io.Serializable;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Date;

public class FileTuples implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String name, hash;
	Date updateDate;
	String consistency;
	double size;
	double sizeInMB;
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
	public FileTuples(String name,String hash, Date updateDate){
		setName(name);
		setUpdateDate(updateDate);
		setHash(hash);
		consistency="ok";
		size=0;
	} 
	
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
		sizeInMB=size/1048576;
	}
	public String getConsistency() {
		return consistency;
	}
	public void setConsistency(String consistency) {
		this.consistency = consistency;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	@Override
	public String toString() {
		return name +" "+ consistency+" " + new DecimalFormat("#.##").format(sizeInMB)+" MB";
	}
	
	
	
	
}