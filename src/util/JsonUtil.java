package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Relationship;
import bean.TableBean;

public class JsonUtil {

	public static Connection conn = null;

	public static void initializeConnection() {
		try {
			String db = "database.mdb";
			conn = DriverManager.getConnection("jdbc:ucanaccess://" + db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}

	public static String getJson() {
		initializeConnection();
		ArrayList<TableBean> tableList = getDBTables();
		String json = "{\"tables\":[";
		for (TableBean table : tableList) {
			json = json + table + ",";
		}
		json = json + "]}";
		json = json.replaceAll(",]", "]");
		json = json.replaceAll(",}", "}");
		System.out.println(json);
		return json;
	}

	public static ArrayList<TableBean> getDBTables() {
		ArrayList<TableBean> tableList = null;
		try {

			if (null != conn) {
				DatabaseMetaData dbMetaData = conn.getMetaData();
				ResultSet rsTableList = dbMetaData.getTables(null, null, null,
						null);
				tableList = new ArrayList<TableBean>();
				while (null != rsTableList && rsTableList.next()) {
					String tableName = rsTableList.getString("TABLE_NAME");
					String tableType = rsTableList.getString("TABLE_TYPE");

					TableBean table = new TableBean(tableName, tableType);
					if (!table.getTableType().equalsIgnoreCase("SYSTEM TABLE")) {
						String query = "SELECT * FROM " + tableName
								+ " WHERE 1=2";
						PreparedStatement ps = conn.prepareStatement(query);
						ResultSet rsTable = ps.executeQuery();
						ResultSetMetaData rsmdTable = rsTable.getMetaData();
						for (int i = 1; i <= rsmdTable.getColumnCount(); i++) {
							String columnName = rsmdTable.getColumnName(i);
							String columnType = rsmdTable.getColumnTypeName(i);
							table.getColumns().put(columnName, columnType);
						}

						ArrayList<Relationship> rList = printForeignKeys(conn,
								tableName);
						table.setForeignKeys(rList);
						tableList.add(table);

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableList;
	}

	private static ArrayList<Relationship> printForeignKeys(
			Connection connection, String tableName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet foreignKeys = metaData.getImportedKeys(
				connection.getCatalog(), null, tableName);
		ArrayList<Relationship> rList = new ArrayList<Relationship>();
		while (foreignKeys.next()) {
			String fkTableName = foreignKeys.getString("FKTABLE_NAME");
			String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
			String pkTableName = foreignKeys.getString("PKTABLE_NAME");
			String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");

			Relationship r = new Relationship(fkTableName, fkColumnName,
					pkTableName, pkColumnName);
			rList.add(r);
		}
		return rList;
	}

}
