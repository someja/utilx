package io.github.someja.utilsx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用DAO实现
 * 查询List
 * 查询单个
 * @author Administrator
 *
 */
public class CommonDao {
	
	public static final List<Map<String, Object>> list(String sql) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Map<String, Object>> result = new ArrayList<>();
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				
				ResultSetMetaData meta = rs.getMetaData();
				
				for (int i = 0; i < meta.getColumnCount(); i++) {
					String column = meta.getColumnName(i+1);
					Object value = rs.getObject(i+1);
					row.put(column, value);
				}
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBUtil.closeConnection();
		}
		
		return result;
	}
	
//	public static final List<Map<String, Object>> list(String sql, boolean ) {
//		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		List<Map<String, Object>> result = new ArrayList<>();
//		
//		try {
//			con = DBUtil.getConnection();
//			pstmt = con.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//			
//			while (rs.next()) {
//				Map<String, Object> row = new HashMap<>();
//				
//				ResultSetMetaData meta = rs.getMetaData();
//				
//				for (int i = 0; i < meta.getColumnCount(); i++) {
//					String column = meta.getColumnName(i+1);
//					Object value = rs.getObject(i+1);
//					row.put(column, value);
//				}
//				result.add(row);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBUtil.closeConnection();
//		}
//		
//		return result;
//	}
	
	public static final Map<String, Object> one(String sql) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Map<String, Object> result = new HashMap<>();
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ResultSetMetaData meta = rs.getMetaData();
				
				for (int i = 0; i < meta.getColumnCount(); i++) {
					String column = meta.getColumnName(i+1);
					Object value = rs.getObject(i+1);
					result.put(column, value);
				}
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBUtil.closeConnection();
		}
		
		return result;
	}
	
	public static final long oneLong(String sql) {
		return oneLong(sql, true);
	}
	
	public static final long oneLong(String sql, boolean closeAfterFinish) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		long result = 0;
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				result = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (closeAfterFinish) {
				DBUtil.closeConnection();
			}
		}
		
		return result;
	}

}
