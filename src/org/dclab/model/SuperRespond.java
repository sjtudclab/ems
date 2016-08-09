package org.dclab.model;

/**
 * @author alvis
 *监考操作中返回的对象
 *flag是操作是否成功的标志
 *detail是具体的错误信息
 */
public class SuperRespond {
	private boolean flag;
	private String detail;
	
	public SuperRespond(boolean sign)
	{
		flag=sign;
	}
	public SuperRespond(boolean sign,String str)
	{
		flag=sign;
		detail=str;
	}
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
}
