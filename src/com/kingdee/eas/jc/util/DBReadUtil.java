package com.kingdee.eas.jc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.kingdee.eas.jc.exception.EASException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * <p>
 * ����:DBUtil
 * </p>
 * <p>
 * ��˵��:�ӿ����ݿ�����ࡣ
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE ����
 */
public class DBReadUtil {

	/** oracle 10g driver */
//	private final static String DEFAULT_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_ENV = "resource/DBConfig.propties";

	Connection conn = null;
	
	private static DBReadUtil dbcputils=null;  
    private ComboPooledDataSource cpds=null;  
    private DBReadUtil(){  
        if(cpds==null){  
            cpds=new ComboPooledDataSource();  
        }  
        try {  
        	PropertiesUtil pu = new PropertiesUtil(DB_ENV);
			String driver = pu.getValue(Constants.READ_DRIVERCLASSNAME);
			String dbSId = pu.getValue(Constants.READ_JDBC_URL);
			String dbUser = pu.getValue(Constants.READ_JDBC_USERNAME);
			String dbPasswd = pu.getValue(Constants.READ_JDBC_PASSWORD);
//		// ��֤���ݿ��ַ���û��Ƿ�Ϊ��
//		if (StringUtil.stringIsEmpty(dbSId) || StringUtil.stringIsEmpty(dbUser)) {
//			throw new EASException("ERR206", "The DB_SID or DB_USER is null.");
//		}
        	cpds.setUser(dbUser);  
        	cpds.setPassword(dbPasswd);  
        	cpds.setJdbcUrl(dbSId);  
            cpds.setDriverClass(driver);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        cpds.setInitialPoolSize(20);  
        cpds.setMaxIdleTime(20);  
        cpds.setMaxPoolSize(20);  
        cpds.setMinPoolSize(5);  
    }  
    public synchronized static DBReadUtil getInstance(){  
    	try {
    		if(dbcputils==null)  
    			dbcputils=new DBReadUtil();  
		} catch (Exception e) {
			LoggerUtil.logger.error(e.getMessage(), e);
		}
        return dbcputils;  
    }  
	
	
	/**
	 * ������ ��getConnection<BR>
	 * ����˵�������ݲ����ĵ����ݿ�����<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @throws EASException
	 */
	public Connection getConnection()
			throws EASException {
		Connection con=null;  
        try {  
            con=cpds.getConnection();  
        } catch (SQLException e) {  
            LoggerUtil.logger.error(e.getMessage(), e);
        }  
        return con;  
	}

	/**
	 * ������ ��getPaginationSQL<BR>
	 * ����˵�����ĵ�Oracle���ݿ�ķ�ҳ���<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param sql
	 *            ���ݿ����
	 * @param startNo
	 *            ��ʼ��¼λ��
	 * @param pageSize
	 *            ��¼��
	 *
	 * @return ���ݿ�����
	 *
	 */
	public String getPaginationSQL(String sql, int startNo, int pageSize) {

		String strSql = "";
		// �õ�������¼λ��
		int endNo = startNo + pageSize;

		// ��ʼλ��Ϊ0ʱ����Ҫƴ����ʼ��¼���
		if (startNo == 0) {
			strSql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + endNo;
		} else {
			// ƴ��SQL ��ҳ���
			strSql = "SELECT * FROM (SELECT ROW_.*,ROWNUM ROWNUM_ FROM (" + sql
					+ ") ROW_ WHERE ROWNUM <= " + endNo + ") WHERE ROWNUM_>"
					+ startNo;
		}

		return strSql;
	}
}
