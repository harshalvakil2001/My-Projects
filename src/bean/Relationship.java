package bean;

public class Relationship {
	String fkTableName;
	String fkColumnName;
	String pkTableName;
	String pkColumnName;

	public Relationship(String fkTableName, String fkColumnName,
			String pkTableName, String pkColumnName) {
		super();
		this.fkTableName = fkTableName;
		this.fkColumnName = fkColumnName;
		this.pkTableName = pkTableName;
		this.pkColumnName = pkColumnName;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}

	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	@Override
	public String toString() {
		return "{\"fkTableName\":\"" + fkTableName + "\",\"fkColumnName\":\""
				+ fkColumnName + "\",\"pkTableName\":\"" + pkTableName
				+ "\",\"pkColumnName\":\"" + pkColumnName + "\"}";
	}

}
