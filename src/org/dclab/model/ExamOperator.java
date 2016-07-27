package org.dclab.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.ChoiceMapperI;
import org.dclab.mapping.SubjectMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.utils.MyBatisUtil;
import org.dclab.utils.TokenGenerator;

/**
 * 所有考试过程中的操作都是对内存中此对象的读写
 * 主要是两个MAP
 * @author alvis
 *
 */
public class ExamOperator {
	/**
	 * 用户 id 到token 的MAP  
	 */
	public static Map<Integer, UUID> idTokenMap = new HashMap<>();
	
	/**
	 * 考生token 到 examBean的映射
	 */
	public static Map<UUID, ExamBean> tokenExamMap = new HashMap<>();
	
	/**
	 * 从数据库加载数据装填考生id与token及试卷
	 */
	public static void load(int sid/*科目的id*/){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		UserMapperI userMapper=sqlSession.getMapper(UserMapperI.class);
		SubjectMapperI subMapper=sqlSession.getMapper(SubjectMapperI.class);
		ExamBean.setEXAM_TIME(subMapper.getDurationById(sid));//设置考试时长，所有考生是一样的
		
	    TopicMapperI topicMapper=sqlSession.getMapper(TopicMapperI.class);
	    ChoiceMapperI choiceMapper=sqlSession.getMapper(ChoiceMapperI.class);
	    
	    List<SingleChoiceBean> slist=topicMapper.getSingleBean();//将topicid和content填入singlechoiceBean
	    for(SingleChoiceBean bean: slist){//对于每一个bean，根据topicid填充选项的id和content
	    	int topicId=bean.getId();
	    	bean.setChoiceList(choiceMapper.getChoice(topicId));
	    }
	    
	    System.out.println("单选题的个数"+slist.size());//********
	    
	    List<MultiChoicesBean> mlist=topicMapper.getMultiBean();//和单选题一样
	    for(MultiChoicesBean bean: mlist){
	    	int topicId=bean.getId();
	    	bean.setChoiceList(choiceMapper.getChoice(topicId));
	    }
	    
	    System.out.println("多选题的个数"+mlist.size());//********
	    
	    List<Integer> numlist=topicMapper.getMatchNum();//由于一道匹配题有多个topicid,所以根据number识别
	    List<MatchingBean>  mlist1=new ArrayList<MatchingBean>();
	    for(int num : numlist)
	    {
	    	MatchingBean bean=new MatchingBean();
	    	bean.setId(num);//
	    	bean.setContentList(topicMapper.getMatchContent(num));//对于每一道匹配题，都有一个内容和id的list
	    	List<ChoicesBean> clist=new ArrayList<ChoicesBean>();
	    	for(ContentBean cbean : bean.getContentList()){//对于匹配题的每一个待选项，都有一个对应的答案
	    		int topicid=cbean.getContentId();
	    		ChoicesBean choicesBean=choiceMapper.getMatchChoice(topicid);
	    		clist.add(choicesBean);
	    	}
	    	bean.setChoiceList(clist);
	    }
	    
	    System.out.println("匹配题的个数"+mlist1.size());//********
	    
	    List<JudgementBean> jlist=topicMapper.getJudgeBean();//和单选题一样
	    for(JudgementBean bean: jlist){
	    	int topicId=bean.getId();
	    	bean.setChoiceList(choiceMapper.getChoice(topicId));
	    }
	    
	    System.out.println("判断题的个数"+jlist.size());//********
	    
	    System.out.println("成功取出题目");
	    
		List<Integer> uidList=userMapper.getUid();
		for(int id:uidList){
			idTokenMap.put(id, TokenGenerator.generate());
			ExamBean exambean=new ExamBean(id,sid);
			exambean.setSingleChoiceList(slist);
			exambean.setMultiChoicesList(mlist);
			exambean.setMatchingList(mlist1);
			exambean.setJudgementList(jlist);
			tokenExamMap.put(idTokenMap.get(id), exambean);
		}
		System.out.println("成功装载到map");
		
	}
	
	/**
	 * 交卷之后将考生答题信息写入数据库
	 */
	public static void persist(){
		
	}
}
