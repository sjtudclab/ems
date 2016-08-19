package org.dclab.model;

import java.util.List;

/**
 * 每道题的格式
 * 1. 题干
 * 2. 考生答案 （简答、填空、上机题答案可写在选项里面）
 * 3. 选项  （List 单选。多选，判断，匹配 ，填空size>1, 简答，上机（如果是文本）size =1）
 * @author zhaoz
 *
 */
public class Topic4PDF {
	public String content;
	public String answer;
	public List<String> items;
	
	public Topic4PDF(String content, String answer, List<String> items) {
		this.content	=	content;
		this.answer		=	answer;
		this.items		=	items;
	}

}
