package org.dclab.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.common.Constants;
import org.dclab.mapping.CanAnswerMapperI;
import org.dclab.mapping.ChoiceMapperI;
import org.dclab.mapping.MatchItemMapperI;
import org.dclab.mapping.SessionCanMapperI;
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
	public static Map<String, UUID> idTokenMap = new HashMap<>();
	
	/**
	 * 考生token 到 examBean的映射
	 */
	public static Map<UUID, ExamBean> tokenExamMap = new HashMap<>();
	
	
	public static void newLoad(List<Timestamp> list){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		ChoiceMapperI choiceMapperI = sqlSession.getMapper(ChoiceMapperI.class);
		MatchItemMapperI matchItemMapperI = sqlSession.getMapper(MatchItemMapperI.class);
		String statement = "org.dclab.mapping.paperMapper.getDuration";
		String statement1= "org.dclab.mapping.paperMapper.getEarliest";
		
		idTokenMap = new HashMap<>();
		idTokenMap = new HashMap<>();
		
		List<Integer> sidList = new ArrayList<>();
		for(Timestamp timestamp : list)
		{
			sidList.addAll(sessionMapperI.getSidByTime(timestamp));
		}
		List<String> uidList = new ArrayList<>();
		System.out.println(sidList);
		for(int sid : sidList)
		{
			uidList.addAll(sessionCanMapperI.getUidListBySid(sid));
		}
		System.out.println(uidList);
		
		Map<Integer, ExamBean> map = new HashMap<>();//用这个map确保每个paperId对应的试卷只从数据库去一次
		
		for(String uid : uidList)
		{
			
			idTokenMap.put(uid, TokenGenerator.generate());
			int paperId = userMapperI.getPaperIdByUid(uid);
			System.out.println("装载的考生的uid："+uid);
			if(!map.containsKey(paperId))
			{
				ExamBean examBean = new ExamBean();
				
				List<SingleChoiceBean> sList = topicMapperI.getSingleByPaperId(paperId);
				for(SingleChoiceBean bean : sList){
					int topicId = bean.getId();
					bean.setChoiceList(choiceMapperI.getChoice(topicId));
					bean.setSingleNum(sList.size());
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
				}
				
				List<MultiChoicesBean> mList = topicMapperI.getMultiByPaperId(paperId);
				for(MultiChoicesBean bean : mList){
					int topicId = bean.getId();
					bean.setChoiceList(choiceMapperI.getChoice(topicId));
					bean.setMultiNum(mList.size());
					
					bean.setChoiceIdList(new ArrayList<Integer>());//初始化一下考生的答案，奇怪的要求
					
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
				}

				List<JudgementBean> jList = topicMapperI.getJudgeByPaperId(paperId);
				for(JudgementBean bean : jList){
					int topicId = bean.getId();
					int right = Constants.JUDGEMENT_TRUE;
					int error = Constants.JUDGEMENT_FALSE;
					
					bean.setChoiceList(choiceMapperI.getJudgeChoice(right,error));
					bean.setJudgeNum(jList.size());
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
				}
				
				List<MatchingBean> mlist1 = topicMapperI.getMatchByPaperId(paperId);
				for(MatchingBean bean : mlist1){
					int topicId= bean.getId();
					bean.setContentList(matchItemMapperI.getItem(topicId));
					bean.setChoiceList(choiceMapperI.getChoice(topicId));
					bean.setMatchNum(mlist1.size());
					
					bean.setChoiceIdMap(new HashMap<>());//初始化一下考生的答案，奇怪的要求
					
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
				}
				
				List<ShortAnswerBean> saList = topicMapperI.getShortByPaperId(paperId);
				for(ShortAnswerBean bean : saList){
					bean.setShortNum(saList.size());
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
		    		if(bean.getPdf()!=null&&bean.getPdf().length()!=0)
		    			{
		    				bean.setShowPdf(true);
		    			}
				}
				
				List<FillBlankBean> fList = topicMapperI.getFillBlankByPaperId(paperId);
				for(FillBlankBean bean : fList){
					bean.setFillNum(choiceMapperI.getFillNumById(bean.getId()));
					bean.setGapNum(fList.size());
					bean.setAnswerList(new ArrayList<>());
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
		    		if(bean.getPdf()!=null&&bean.getPdf().length()!=0)
		    			{
		    				bean.setShowPdf(true);
		    			}
				}
				
				List<MachineTestBean> mList2 = topicMapperI.getMachineByPaperId(paperId);
				for(MachineTestBean bean : mList2){
					bean.setMachineNum(mList2.size());
					if(bean.getImg()!=null&&bean.getImg().length()!=0)
		    			bean.setImg(Constants.multiMediaDir+bean.getImg());
		    		if(bean.getAudio()!=null&&bean.getAudio().length()!=0)
		    			bean.setAudio(Constants.multiMediaDir+bean.getAudio());
		    		if(bean.getVideo()!=null&&bean.getVideo().length()!=0)
		    			bean.setVideo(Constants.multiMediaDir+bean.getVideo());
		    		if(bean.getPdf()!=null&&bean.getPdf().length()!=0)
		    			{
		    				bean.setShowPdf(true);
		    			}
		    		
				}
				
				
				examBean.setEXAM_TIME(sqlSession.selectOne(statement, paperId));
				examBean.setEarliestSubmit(sqlSession.selectOne(statement1, paperId));
				examBean.setSingleChoiceList(sList);
				examBean.setMultiChoicesList(mList);
				examBean.setJudgementList(jList);
				examBean.setMatchingList(mlist1);
				examBean.setShortAnswerList(saList);
				examBean.setFillBlankList(fList);
				examBean.setMachineList(mList2);
				examBean.setTopicNum(sList.size()+mList.size()+jList.size()+mlist1.size()+saList.size()+fList.size()+mList2.size());
				examBean.setPaperId(paperId);
				map.put(paperId, examBean);
			}
			
			
			
			ExamBean examBean = map.get(paperId);
			ExamBean examBean2 = new ExamBean();
			
				examBean2.setEXAM_TIME(examBean.getEXAM_TIME());
				examBean2.setEarliestSubmit(examBean.getEarliestSubmit());
				examBean2.setPaperId(examBean.getPaperId());
				
				List<SingleChoiceBean> sList = new ArrayList<>();
				for(SingleChoiceBean singleChoiceBean : examBean.getSingleChoiceList())
				{
					Collections.shuffle(singleChoiceBean.getChoiceList());
					sList.add((SingleChoiceBean) singleChoiceBean.clone());
				}
				
				List<MultiChoicesBean> mList = new ArrayList<>();
				for(MultiChoicesBean multiChoicesBean : examBean.getMultiChoicesList())
				{
					Collections.shuffle(multiChoicesBean.getChoiceList());
					mList.add((MultiChoicesBean) multiChoicesBean.clone());
				}
				
				List<JudgementBean> jList = new ArrayList<>();
				for(JudgementBean judgementBean : examBean.getJudgementList())
				{
					jList.add((JudgementBean) judgementBean.clone());
				}
				
				List<MatchingBean> mList2 = new ArrayList<>();
				for(MatchingBean matchingBean : examBean.getMatchingList()){
					Collections.shuffle(matchingBean.getChoiceList());
					mList2.add((MatchingBean) matchingBean.clone());
				}
				
				List<ShortAnswerBean> sList2 = new ArrayList<>();
				for(ShortAnswerBean shortAnswerBean : examBean.getShortAnswerList()){
					sList2.add((ShortAnswerBean) shortAnswerBean.clone());
				}
				
				List<FillBlankBean> fList = new ArrayList<>();
				for(FillBlankBean fillBlankBean : examBean.getFillBlankList()){
					fList.add((FillBlankBean) fillBlankBean.clone());
				}
				
				List<MachineTestBean> mList3 = new ArrayList<>();
				for(MachineTestBean machineTestBean : examBean.getMachineList()){
					mList3.add((MachineTestBean) machineTestBean.clone());
				}
				
				examBean2.setSingleChoiceList(sList);
				examBean2.setMultiChoicesList(mList);
				examBean2.setJudgementList(jList);
				examBean2.setMatchingList(mList2);
				examBean2.setShortAnswerList(sList2);
				examBean2.setFillBlankList(fList);
				examBean2.setMachineList(mList3);
				examBean2.setTopicNum(examBean.getTopicNum());
				examBean2.setFinishTopic(new HashSet<>());
				examBean2.setUid(uid);
			
			tokenExamMap.put(idTokenMap.get(uid), examBean2);
		}
		sqlSession.close();
	}
	
	/**
	 * 从数据库加载数据装填考生id与token及试卷
	 */
	/*public static void load(int sid场次的id){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		UserMapperI userMapper=sqlSession.getMapper(UserMapperI.class);
		SessionMapperI sessionMapper=sqlSession.getMapper(SessionMapperI.class);
	    TopicMapperI topicMapper=sqlSession.getMapper(TopicMapperI.class);
	    ChoiceMapperI choiceMapper=sqlSession.getMapper(ChoiceMapperI.class);
	    MatchItemMapperI matchMapper=sqlSession.getMapper(MatchItemMapperI.class);
	    SubjectMapperI subMapper=sqlSession.getMapper(SubjectMapperI.class);
		
	    int subId=sessionMapper.getSubIdById(sid);
		ExamBean.setEXAM_TIME(subMapper.getDurationBySubId(subId));//设置考试时长，所有考生是一样的
		ExamBean.setEarliestSubmit(subMapper.getEarSubmitBySubId(subId));
	    
	    boolean flag=false;//因为多媒体资源的前缀path也循环叠加了5次，所以用这个flag控制
	    
	    List<ArrayList<SingleChoiceBean>> singleLists=new ArrayList<>();//这个list中放5个不同的单选题list
	    for(int i=0;i<5;i++)//往singlelists中加载5套不同的单选题list
	    {
	    	//TODO: 优化随机策略，不需要每次都重新从数据库拿题目，题目是一样的，直接使用clone函数
	    	
	    	ArrayList<SingleChoiceBean> slist=topicMapper.getSingleBeanBySubId(subId);//获得单选题题干,id和多媒体资源
	    	for(SingleChoiceBean bean: slist)
	    	{//对于每一个 bean，根据topicid填充选项的id和content
		    	int topicId=bean.getId();
		    	List<ChoicesBean> list=choiceMapper.getChoice(topicId);//随机化题目选项
		    	Collections.shuffle(list);
		    	bean.setChoiceList(list);
		    	bean.setSingleNum(slist.size());
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
	    
	    List<ArrayList<MultiChoicesBean>> multiLists=new ArrayList<>();//这个list中放5个不同的多选题list
	    for(int i=0;i<5;i++)
	    {
	    	//TODO: 优化随机策略，不需要每次都重新从数据库拿题目，题目是一样的，直接使用clone函数

	    	ArrayList<MultiChoicesBean> mlist=topicMapper.getMultiBeanBySubId(subId);//获得多选题题干和id
	    	
	    	System.out.println("get multichoices list: "+mlist);
	    	
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
	    
	    List<ArrayList<MatchingBean>> matchLists=new ArrayList<>();//这个list中放5个不同的匹配题list
	    for(int i=0;i<5;i++)
	    {
	    	//TODO: 优化随机策略，不需要每次都重新从数据库拿题目，题目是一样的，直接使用clone函数

		    List<Integer> tlist=topicMapper.getMatchTopicIdBySubId(subId);//由于一道匹配题有多个topicid,所以根据number识别
		    ArrayList<MatchingBean>  mlist=new ArrayList<MatchingBean>();
	    	
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
		    	bean.setMatchNum(mlist.size());
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

	    ArrayList<JudgementBean> jlist=topicMapper.getJudgeBeanBySubId(subId);//判断题list
	    
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
	    
	    ArrayList<ShortAnswerBean> salist=topicMapper.getShortBeanBySubId(subId);//简答题list
	    for(ShortAnswerBean bean : salist)
	    {
	    	bean.setShortNum(salist.size());
	    }
	    
	    int count=singleLists.get(0).size()+multiLists.get(0).size()+matchLists.get(0).size()+jlist.size()+salist.size();
		System.out.println("总题数"+count);
	    List<Integer> uidList=userMapper.getUid();	//添加subjectId
		Random rand=new Random();
		for(int id:uidList){
			idTokenMap.put(id, TokenGenerator.generate());
			int i=rand.nextInt(5);
			ExamBean exambean=new ExamBean(id,subId);
			
			List<SingleChoiceBean> singleList = new ArrayList<>();//克隆每一个bean
			for (SingleChoiceBean singleChoiceBean : singleLists.get(i)) {
				singleList.add((SingleChoiceBean) singleChoiceBean.clone());
			}
			exambean.setSingleChoiceList(singleList);
			
			List<MultiChoicesBean> multiList=new ArrayList<>();
			for	(MultiChoicesBean multiChoicesBean : multiLists.get(i)){
				multiList.add((MultiChoicesBean) multiChoicesBean.clone());
			}
			exambean.setMultiChoicesList(multiList);
			
			List<MatchingBean> matchList=new ArrayList<>();
			for(MatchingBean matchingBean : matchLists.get(i)){
				matchList.add((MatchingBean) matchingBean.clone());
			}
			exambean.setMatchingList(matchList);
			
			List<JudgementBean> judgeList=new ArrayList<>();
			for(JudgementBean judgementBean : jlist)
			{
				judgeList.add((JudgementBean) judgementBean.clone());
			}
			exambean.setJudgementList(judgeList);
			
			List<ShortAnswerBean> shortList=new ArrayList<>();
			for(ShortAnswerBean shortAnswerBean : salist)
			{
				shortList.add((ShortAnswerBean) shortAnswerBean.clone());
			}
			exambean.setShortAnswerList(shortList);
			exambean.setTopicNum(count);
			
			tokenExamMap.put(idTokenMap.get(id), exambean);
		}

		sqlSession.close();
		
	}*/
	
	/**
	 * 交卷之后将考生答题信息写入数据库
	 */
	public static void persist(UUID token,int mark){
		ExamBean examBean=ExamOperator.tokenExamMap.get(token);
		String uid=examBean.getUid();
		List<CandidateAnswerBean> candidateList=new ArrayList<>();
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		CanAnswerMapperI canAnswerMapperI = sqlSession.getMapper(CanAnswerMapperI.class);
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		
		for(SingleChoiceBean sbean: examBean.getSingleChoiceList()){
			CandidateAnswerBean candidateAnswerBean = new CandidateAnswerBean(uid,sbean.getId(),String.valueOf(sbean.getChoiceId()));
			if(canAnswerMapperI.insertAnswer(candidateAnswerBean)!=1)//???????????
				System.err.println("写入数据库失败");
			sqlSession.commit();
		}
		
		for(MultiChoicesBean mbean : examBean.getMultiChoicesList()){
			String str="";
			for(int i : mbean.getChoiceIdList())
			{
				str=str+String.valueOf(i)+",";
			}
			CandidateAnswerBean candidateAnswerBean = new CandidateAnswerBean(uid, mbean.getId(), str);
			if(canAnswerMapperI.insertAnswer(candidateAnswerBean)!=1)
				System.err.println("写入数据库失败");
			sqlSession.commit();
		}
		
		for(JudgementBean jBean : examBean.getJudgementList())
		{
			CandidateAnswerBean candidateAnswerBean = new CandidateAnswerBean(uid, jBean.getId(), String.valueOf(jBean.getChoiceId()));
			if(canAnswerMapperI.insertAnswer(candidateAnswerBean)!=1)
				System.err.println("写入数据库失败");
			sqlSession.commit();
		}
		
		for(MatchingBean mBean : examBean.getMatchingList()){
			String str="";
			Map<Integer,Integer> map=mBean.getChoiceIdMap();
			for(Object object : map.keySet()){
				str=str+object+":"+map.get(object)+",";
				
			}
			CandidateAnswerBean candidateAnswerBean = new CandidateAnswerBean(uid, mBean.getId(), str);
			if(canAnswerMapperI.insertAnswer(candidateAnswerBean)!=1)
				System.err.println("写入数据库失败");
			sqlSession.commit();
		}
		
		if(userMapperI.updateMark(mark, uid)!=1)
			System.err.println("写入考生成绩失败");
		sqlSession.commit();
		
		sqlSession.close();
	}
	
	public static void main (String[] args){
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		Timestamp timestamp = sessionMapperI.getStartTimeById(6);
		long time = timestamp.getTime();
		System.out.println("转换得到的long："+time);
		Timestamp timestamp2 = new Timestamp(time);
		System.out.println("long转换timestamp:"+timestamp2);
		System.out.println(timestamp);
/*		
		ExamOperator.newLoad(timestamp);*/
		System.out.println(ExamOperator.idTokenMap);
		System.out.println(ExamOperator.tokenExamMap);
	}
}
