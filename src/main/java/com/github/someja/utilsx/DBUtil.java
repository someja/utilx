package com.github.someja.utilsx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具类
 * @author zhuawa
 *
 */
public class DBUtil {
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

	static{
		Properties prop = new Properties();
		try {
			prop.load(DBUtil.class.getResourceAsStream("/jdbc.properties"));
			driver = prop.getProperty("driverClassName");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			Class.forName(driver);
		} catch (Exception e) {
			throw new RuntimeException("加载驱动失败", e);
		}
	}
	
	public static Connection getConnection(){
		Connection con = tl.get();
		if(con==null)
			try {
				con = DriverManager.getConnection(url, username, password);
				tl.set(con);
			} catch (Exception e) {
				throw new RuntimeException("获取连接失败", e);
			}
		return con;
	}
	
	public static void closeConnection(){
		Connection con = tl.get();
		if(con!=null)
			try {
				con.close();
				tl.set(null);
			} catch (SQLException e) {
				throw new RuntimeException("关闭连接失败", e);
			}
	}
	
}
