package org.dclab.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.ChoiceMapperI;
import org.dclab.mapping.MatchItemMapperI;
import org.dclab.mapping.SessionMapperI;
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
	 * 
	 */
	private static final String imgPath="EMSdata\\img\\";
	private static final String audioPath="EMSdata\\audio\\";
	private static final String vedioPath="EMSdata\\video\\";
	public static Map<Integer, UUID> idTokenMap = new HashMap<>();
	
	/**
	 * 考生token 到 examBean的映射
	 */
	public static Map<UUID, ExamBean> tokenExamMap = new HashMap<>();
	
	/**
	 * 从数据库加载数据装填考生id与token及试卷
	 */
	public static void load(int sid/*场次的id*/){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		UserMapperI userMapper=sqlSession.getMapper(UserMapperI.class);
		SessionMapperI sessionMapper=sqlSession.getMapper(SessionMapperI.class);
	    TopicMapperI topicMapper=sqlSession.getMapper(TopicMapperI.class);
	    ChoiceMapperI choiceMapper=sqlSession.getMapper(ChoiceMapperI.class);
	    MatchItemMapperI matchMapper=sqlSession.getMapper(MatchItemMapperI.class);
	    SubjectMapperI subMapper=sqlSession.getMapper(SubjectMapperI.class);
		
	    int subId=sessionMapper.getSubIdById(sid);
		ExamBean.setEXAM_TIME(subMapper.getDurationBySubId(subId));//设置考试时长，所有考生是一样的
	    
	    boolean flag=false;//因为多媒体资源的前缀path也循环叠加了5次，所以用这个flag控制
	    
	    List<List<SingleChoiceBean>> singleLists=new ArrayList<>();//这个list中放5个不同的单选题list
	    for(int i=0;i<5;i++)//往singlelists中加载5套不同的单选题list
	    {
		    List<SingleChoiceBean> slist=topicMapper.getSingleBeanBySubId(subId);//获得单选题题干,id和多媒体资源
	    	for(SingleChoiceBean bean: slist)
	    	{//对于每一个 bean，根据topicid填充选项的id和content
		    	int topicId=bean.getId();
		    	bean.setSingleNum(slist.size());
		    	List<ChoicesBean> list=choiceMapper.getChoice(topicId);//随机化题目选项
		    	Collections.shuffle(list);
		    	bean.setChoiceList(list);
		    	
		    	if(flag==false)
		    	{ 
		    		if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(imgPath+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(audioPath+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(vedioPath+bean.getVideo());
		    	}
		    	
		    }
	    	flag=true;
	    	singleLists.add(slist);
	    }
	    
	    flag=false;
	    
	    List<List<MultiChoicesBean>> multiLists=new ArrayList<>();//这个list中放5个不同的多选题list
	    for(int i=0;i<5;i++)
	    {
	    	List<MultiChoicesBean> mlist=topicMapper.getMultiBeanBySubId(subId);//获得多选题题干和id
	    	for(MultiChoicesBean bean: mlist){
		    	int topicId=bean.getId();
		    	List<ChoicesBean> list=choiceMapper.getChoice(topicId);
		    	Collections.shuffle(list);
		    	bean.setChoiceList(list);
		    	List<Integer> choiceIdList=new ArrayList<>();
		    	bean.setChoiceIdList(choiceIdList);
		    	bean.setMultiNum(mlist.size());
		    	if(flag==false)
		    	{ 
		    		if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(imgPath+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(audioPath+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(vedioPath+bean.getVideo());
		    	}
		    }
	    	flag=true;
	    	multiLists.add(mlist);
	    }
	    flag=false;
	    
	    List<List<MatchingBean>> matchLists=new ArrayList<>();//这个list中放5个不同的匹配题list
	    for(int i=0;i<5;i++)
	    {
		    List<Integer> tlist=topicMapper.getMatchTopicIdBySubId(subId);//由于一道匹配题有多个topicid,所以根据number识别
		    List<MatchingBean>  mlist=new ArrayList<MatchingBean>();
		    for(int topicId : tlist)
		    {
		    	MatchingBean bean=new MatchingBean();
		    	bean.setId(topicId);
		    	List<ContentBean> list=matchMapper.getItem(topicId);
		    	Collections.shuffle(list);
		    	bean.setContentList(list);//对于每一道匹配题，都有一个内容和id的list
		    	List<ChoicesBean> list1=choiceMapper.getChoice(topicId);
		    	Collections.shuffle(list1);
		    	bean.setChoiceList(list1);
		    	Map<Integer, Integer> choiceIdMap=new HashMap<>();
		    	bean.setChoiceIdMap(choiceIdMap);
		    	bean.setMatchNum(tlist.size());
		    	if(flag==false)
		    	{ 
		    		if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(imgPath+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(audioPath+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(vedioPath+bean.getVideo());
		    	}
		    	mlist.add(bean);
		    }
		    flag=true;
		    matchLists.add(mlist);
	    }

	    List<JudgementBean> jlist=topicMapper.getJudgeBeanBySubId(subId);//判断题list
	    for(JudgementBean bean: jlist){
	    	int topicId=bean.getId();
	    	List<ChoicesBean> list=choiceMapper.getJudgeChoice();
	    	Collections.shuffle(list);
	    	bean.setChoiceList(list);
	    	bean.setJudgeNum(jlist.size());
	    	if(bean.getImg()!=null&&bean.getImg().length()!=0)
	    		bean.setImg(imgPath+bean.getImg());
	    	if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
	    		bean.setAudio(audioPath+bean.getAudio());
	    	if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
	    		bean.setVideo(vedioPath+bean.getVideo());
	    }
	    
	    List<shortAnswerBean> salist=topicMapper.getShortBeanBySubId(subId);//简答题list
	    for(shortAnswerBean bean: salist){
	    	bean.setShortNum(salist.size());	
	    }
	    int count=singleLists.get(0).size()+multiLists.get(0).size()+matchLists.get(0).size()+jlist.size()+salist.size();
	    ExamBean.setTopicNum(count);
		List<Integer> uidList=userMapper.getUid();
		for(int id:uidList){
			idTokenMap.put(id, TokenGenerator.generate());
			Random rand=new Random();
			int i=rand.nextInt(5);
			ExamBean exambean=new ExamBean(id,subId);
			exambean.setSingleChoiceList(singleLists.get(i));
			exambean.setMultiChoicesList(multiLists.get(i));
			exambean.setMatchingList(matchLists.get(i));
			exambean.setJudgementList(jlist);
			exambean.setShortAnswerList(salist);
			
			tokenExamMap.put(idTokenMap.get(id), exambean);
		}

		sqlSession.close();
		
	}
	
	/**
	 * 交卷之后将考生答题信息写入数据库
	 */
	public static void persist(UUID token){
		ExamBean examBean=ExamOperator.tokenExamMap.get(token);
		int uid=examBean.getUid();
		List<CandidateAnswerBean> candidateList=new ArrayList<>();
		
		for(SingleChoiceBean sbean: examBean.getSingleChoiceList()){
			new CandidateAnswerBean(uid,sbean.getId(),String.valueOf(sbean.getChoiceId()));
		}
	}
}
