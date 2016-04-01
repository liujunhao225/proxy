package com.proxy.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * DB operation likes insert ,update ,find ,delete
 * 
 * @author mayibo
 *
 */
public class DBTOOL {

	private final static String USER = "root";
	private final static String PASSWORD = "123456";
	private final static String DBURL = "jdbc:mysql://192.168.1.175:3306/my_proxy";
	private static Logger logger = Logger.getLogger("DBTOOL");
	static {
		try {
			logger.info("mysql driver load");
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("mysql load error:" + e.getMessage());
		}
	}

	private static Connection getConnection() {

		int time = 0;
		Connection connection = null;
		logger.info("get db connection");
		while (time < 3) {
			try {
				connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
			} catch (SQLException e) {
				logger.error("connection db error message:" + e.getMessage());
				connection = null;
			}
			if (connection == null && time < 3) {
				time++;
			} else {
				break;
			}
		}
		if (connection == null) {
			logger.error("get db connection error");
			System.exit(0);
		}

		return connection;
	}

	public static Object execute(SQLTYPE type, String sql, Object[] params) {
		Connection conn = getConnection();
		Object result = null;
		try {
			switch (type) {
			case FINDONE: {
				result = findOne(conn, sql, params);
				break;
			}
			case FINDLIST: {
				result = findList(conn, sql, params);
				break;
			}
			case INSERT: {

				result = insert(conn, sql, params);
				break;
			}
			case UPDATE: {
				result = update(conn, sql, params);
			}

			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}

	private static JSONObject findOne(Connection conn, String sql, Object[] params) {
		try {
			logger.info("execute find list sql starting");
			PreparedStatement ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 1; i <= params.length; i++) {
					ps.setString(i, (String) params[i]);
				}
			}
			ResultSet rs = ps.executeQuery();
			JSONObject job = new JSONObject();
			if (rs != null) {
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();

				String columnNames[] = new String[columnCount + 1];
				String columnType[] = new String[columnCount + 1];
				for (int i = 1; i <= columnCount; i++) {
					columnNames[i] = md.getColumnName(i);
					columnType[i] = md.getColumnTypeName(i);
				}
				if (rs.next()) {
					for (int i = 1; i <= columnCount; i++) {
						if ("int".equalsIgnoreCase(columnType[i])) {
							job.put(columnNameToProperty(columnNames[i]), rs.getInt(columnNames[i]));
						} else if ("varchar".equalsIgnoreCase(columnType[i])) {
							job.put(columnNameToProperty(columnNames[i]), rs.getString(columnNames[i]));
						} else {
							//
						}
					}

				}
			}
			logger.info("execute find list sql ending");
			return job;
		} catch (SQLException e) {
			logger.error("execute find list sql error message:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static List<JSONObject> findList(Connection conn, String sql, Object[] params) {
		try {
			logger.info("execute find list sql starting");
			PreparedStatement ps = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 1; i <= params.length; i++) {
					ps.setString(i, (String) params[i]);
				}
			}
			ResultSet rs = ps.executeQuery();
			List<JSONObject> jlist = new ArrayList<JSONObject>();
			if (rs != null) {
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();

				String columnNames[] = new String[columnCount + 1];
				String columnType[] = new String[columnCount + 1];
				for (int i = 1; i <= columnCount; i++) {
					columnNames[i] = md.getColumnName(i);
					columnType[i] = md.getColumnTypeName(i);
				}
				while (rs.next()) {
					JSONObject job = new JSONObject();
					for (int i = 1; i <= columnCount; i++) {
						if ("int".equalsIgnoreCase(columnType[i])) {
							job.put(columnNameToProperty(columnNames[i]), rs.getInt(columnNames[i]));
						} else if ("varchar".equalsIgnoreCase(columnType[i])) {
							job.put(columnNameToProperty(columnNames[i]), rs.getString(columnNames[i]));
						} else {
							//
						}
					}
					jlist.add(job);
				}
			}
			logger.info("execute find list sql ending");
			return jlist;
		} catch (SQLException e) {
			logger.error("execute find list sql error message:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static int insert(Connection conn, String sql, Object[] params) {

		return 0;
	}

	private static int update(Connection conn, String sql, Object[] params) {

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.getGeneratedKeys();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}

		return 0;
	}

	private static String columnNameToProperty(String columnName) {
		String splitStr[] = columnName.split("-");
		StringBuffer sb = new StringBuffer();
		sb.append(splitStr[0]);
		for (int i = 1; i < splitStr.length; i++) {
			// uppercase first letter and connect to
			String tempUpper = splitStr[i].substring(0, 1).toUpperCase();
			String tempLastStr = splitStr[i].substring(1);
			sb.append(tempUpper);
			sb.append(tempLastStr);
		}
		return sb.toString();
	}

}
