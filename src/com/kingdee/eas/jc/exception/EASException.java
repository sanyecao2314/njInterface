package com.kingdee.eas.jc.exception;

/**
 * <p>
 * 	����:EASException
 * </p>
 * <p>
 * 	��˵��:�ӿ�����Exception����
 * </p>
 *
 * @version 1.0
 * @since J2SDK1.4.2
 * @author KINGDEE ����
 */
public class EASException extends Exception {

	/****************Java ���л���********************************************/
	private static final long serialVersionUID = 235885091360433251L;
	/****************�쳣 ����************************************************/
	private String expId;
	/****************�쳣 ����************************************************/
	private String expDes;

	/**
	 * ������ ��EASException<BR>
	 * ����˵�� �����캯��<BR>
	 * ��ע ����
	 *
	 * @param None
	 * @return None
	 */
	public EASException() {
		super();
	}

	/**
	 * ������ ��EASException<BR>
	 * ����˵�� �����캯��<BR>
	 * ��ע ����
	 *
	 * @param expId �쳣����
	 * @param expDes �쳣����
	 *
	 * @return None
	 */
	public EASException(String expId, String expDes) {

		super();
		this.expId = expId;
		this.expDes = expDes;
	}

	/**
	 * ������ ��getExpId<BR>
	 * ����˵�����õ��쳣���<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param None
	 * @return �쳣���
	 *
	 */
	public String getExpId() {
		return expId;
	}

	/**
	 * ������ ��setExpId<BR>
	 * ����˵���������쳣���<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param expId �쳣����
	 * @return None
	 *
	 */
	public void setExpId(String expId) {
		this.expId = expId;
	}

	/**
	 * ������ ��getExpDes<BR>
	 * ����˵�����õ��쳣����<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param None
	 * @return �쳣���
	 *
	 */
	public String getExpDes() {
		return expDes;
	}

	/**
	 * ������ ��setExpDes<BR>
	 * ����˵���������쳣����<BR>
	 * ��ע ����
	 *
	 * @author KINGDEE)����
	 * @Date: 2008/12/03
	 * @Version: 1.0
	 *
	 * @param expDes �쳣����
	 * @return None
	 *
	 */
	public void setExpDes(String expDes) {
		this.expDes = expDes;
	}

}
