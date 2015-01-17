package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableBean {

	private String tableName;
	private String tableType;
	private HashMap<String, String> columns;
	private ArrayList<Relationship> foreignKeys;

	public String getTableName() {
		return tableName;
	}

	public TableBean(String tableName, String tableType) {
		super();
		this.tableName = tableName;
		this.tableType = tableType;
		columns = new HashMap<String, String>();
		foreignKeys = new ArrayList<Relationship>();
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public HashMap<String, String> getColumns() {
		return columns;
	}

	public void setColumns(HashMap<String, String> columns) {
		this.columns = columns;
	}

	public ArrayList<Relationship> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(ArrayList<Relationship> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

	@Override
	public String toString() {
		String colJson = "";
		for (Map.Entry<String, String> col : columns.entrySet()) {
			colJson = colJson + "\"" + col.getKey() + "\":\"" + col.getValue()
					+ "\",";
		}
		String relationship = "[";

		if (foreignKeys.size() > 0) {
			for (Object r : foreignKeys.toArray()) {
				relationship = relationship + r + ",";
			}
		}
		relationship = relationship + "]";

		String str = "{\"tableName\":\"" + tableName + "\", \"columns\":{"
				+ colJson + "},\"relations\":" + relationship + "}";
		str = str.replaceAll(",]", "]");
		str = str.replaceAll(",}", "}");
		return str;
	}

}
