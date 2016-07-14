package org.dclab.service;

/**
 * 试卷操作
 * （拿到ExamBean，当前tab代表试题类型，默认是单选
 * 考试页面：
 * 1. 根据题目类型和ID得到这一题
 * 2. 下一题 （注意获取检查标志状态 ，答案写入内存； （灾备方案：此次操作距离上次超过一分钟，将用户ExamBean写入commit log
 * 3. 上一题 
 * 
 * 检查页面：
 * 1. 得到所有检查的题目，
 * 2. 点击进入该题目
 * 3. （下一题）
 * 
 * 交卷
 * 1. 写入数据库
 * @author alvis
 *
 */
public class ExamService {

}
