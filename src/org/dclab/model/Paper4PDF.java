package org.dclab.model;

import java.util.List;

/**
 * 单个考生答卷 --> 一个PDF 所需内容（全部都是String），文件名为 考生准考证号.pdf
 * 1. 标题部分 （专业名，科目名，试卷号，准考证号）
 * 2. 题目部分 （题干，考生答案， 选项（或匹配项或填空内容或上机题文本答案））Topic4PDF
 * @author zhaoz
 *
 */
public class Paper4PDF {
	public String fileName;	//准考证号 作为文件名
	public String title;	//eg. "专业： 软件工程          科目： 政治        考生：xx   准考证号： xxx " !!注意字段之间保持一定间隙
	public List<List<Topic4PDF>> topicList;	//每种题型一个List
	public List<String>		topicNameList;	//每种题型的名字，添加顺序必须跟topicList添加顺序一致
	
	public Paper4PDF(String fileName, String title, List<List<Topic4PDF>> topicList, List<String> topicNameList) {
		this.fileName	=	fileName;  
		this.title	=	title;  
		this.topicList	=	topicList; 
		this.topicNameList	=	topicNameList;  
	}

}
