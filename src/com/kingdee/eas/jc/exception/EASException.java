package com.kingdee.eas.jc.exception;

/**
 * <p>
 * 	类名:EASException
 * </p>
 * <p>
 * 	类说明:接口所有Exception抽象。
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE 黎亮
 */
public class EASException extends Exception {

	/****************Java 序列化号********************************************/
	private static final long serialVersionUID = 235885091360433251L;
	/****************异常 编码************************************************/
	private String expId;
	/****************异常 描述************************************************/
	private String expDes;

	/**
	 * 方法名 ：EASException<BR>
	 * 方法说明 ：构造函数<BR>
	 * 备注 ：无
	 *
	 * @param None
	 * @return None
	 */
	public EASException() {
		super();
	}

	/**
	 * 方法名 ：EASException<BR>
	 * 方法说明 ：构造函数<BR>
	 * 备注 ：无
	 *
	 * @param expId 异常编码
	 * @param expDes 异常描述
	 *
	 * @return None
	 */
	public EASException(String expId, String expDes) {

		super();
		this.expId = expId;
		this.expDes = expDes;
	}

	/**
	 * 方法名 ：getExpId<BR>
	 * 方法说明：得到异常编号<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param None
	 * @return 异常编号
	 *
	 */
	public String getExpId() {
		return expId;
	}

	/**
	 * 方法名 ：setExpId<BR>
	 * 方法说明：设置异常编号<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param expId 异常编码
	 * @return None
	 *
	 */
	public void setExpId(String expId) {
		this.expId = expId;
	}

	/**
	 * 方法名 ：getExpDes<BR>
	 * 方法说明：得到异常描述<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param None
	 * @return 异常编号
	 *
	 */
	public String getExpDes() {
		return expDes;
	}

	/**
	 * 方法名 ：setExpDes<BR>
	 * 方法说明：设置异常描述<BR>
	 * 备注 ：无
	 *
	 * @author KINGDEE)黎亮
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param expDes 异常描述
	 * @return None
	 *
	 */
	public void setExpDes(String expDes) {
		this.expDes = expDes;
	}

}
