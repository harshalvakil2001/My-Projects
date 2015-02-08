package util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
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

	public static JsonUtil INSTANCE = null;
	public static Connection conn = null;

	private JsonUtil() {
	}

	public static JsonUtil getInstance() {
		if (null == INSTANCE)
			INSTANCE = new JsonUtil();
		return INSTANCE;
	}

	public static void main(String[] args) {
		try {
			String strJson = "Please place a valid Access Database in the folder";

			File dir = new File("WebApp\\js\\json.js");
			System.out.println("Current Path = " + dir);
			dir = new File(dir.getAbsolutePath());
			System.out.println("Current Path = " + dir);
			dir = dir.getParentFile().getParentFile().getParentFile();
			System.out.println("Current Path = " + dir);

			for (File file : dir.listFiles()) {
				System.out.println(file.getName());
				if (file.getName().contains(".mdb")) {
					System.out.println("Database Found");
					strJson = JsonUtil.getInstance().getJson(file.getName());
					System.out.println(strJson);
					break;
				}
			}

			// strJson = JsonUtil.getInstance().getJson("database.mdb");
			File jsonFile = new File("WebApp\\js\\json.js");
			FileWriter jsonWriter = new FileWriter(jsonFile);
			jsonWriter.write("var json = " + strJson + ";");
			Desktop.getDesktop().open(new File("WebApp\\db2Diag.html"));
			jsonWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getJson(String db) {
		ArrayList<TableBean> tableList = getDBTables(db);
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

	private ArrayList<TableBean> getDBTables(String db) {
		ArrayList<TableBean> tableList = null;
		try {

			if (null == conn) {
				JsonUtil.getInstance().initializeConnection(db);
			}
			DatabaseMetaData dbMetaData = conn.getMetaData();
			ResultSet rsTableList = dbMetaData
					.getTables(null, null, null, null);
			tableList = new ArrayList<TableBean>();
			while (null != rsTableList && rsTableList.next()) {
				String tableName = rsTableList.getString("TABLE_NAME");
				String tableType = rsTableList.getString("TABLE_TYPE");

				TableBean table = new TableBean(tableName, tableType);
				if (!table.getTableType().equalsIgnoreCase("SYSTEM TABLE")) {
					String query = "SELECT * FROM " + tableName + " WHERE 1=2";
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableList;
	}

	private ArrayList<Relationship> printForeignKeys(Connection connection,
			String tableName) throws SQLException {
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

	private void initializeConnection(String db) {
		try {
			conn = DriverManager.getConnection("jdbc:ucanaccess://" + db);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
