package org.dclab.model;

/**
 * @author alvis
 *对于匹配题独特需求的bean。
 *每个匹配题有多个item，每个item有多个content及对应的id
 */
public class ContentBean {
	private int contentId;
	private String content;
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
