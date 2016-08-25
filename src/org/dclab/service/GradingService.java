/**
 * 
 */
package org.dclab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.CorrectAnswerMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.model.CorrectAnswerBean;
import org.dclab.model.ExamBean;
import org.dclab.model.JudgementBean;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.SingleChoiceBean;
import org.dclab.utils.MyBatisUtil;
import org.springframework.stereotype.Service;

/**
 * 阅卷评分
 * 1. 单选，比对两个id
 * 2。 多选，少选得分，多选不选不得分，一致则不得分
 * 3. 匹配， item顺序是固定，逐个比较
 * 4. 客观题总分是上述三类题目叠加
 * 
 * @author zhaoz
 *
 */

@Service
public class GradingService {

	public int gradeSingleChoice(Integer candidateChoice, CorrectAnswerBean correctAnswer){
		if (correctAnswer == null) {
			System.err.println("Error Topic ID in candidate answer");
			return -1;
		}
		
		if(correctAnswer.getChoiceId().equals(candidateChoice.toString()))
			return Integer.parseInt(correctAnswer.getPoints().trim());
		
		return 0;
	}
	
	public int gradeJudgement(Integer candidateChoice,  CorrectAnswerBean correctAnswer){
		if (correctAnswer == null) {
			System.err.println("Error Topic ID in candidate answer");
			return -1;
		}
		String correctChoice	=	correctAnswer.getChoiceId().trim();
		
		return correctChoice.equals(candidateChoice.toString()) ? Integer.parseInt(correctAnswer.getPoints().trim()) : 0;
	}
	
	/**
	 * Grading multichoices, 0 point if error occur, almost but not completely correct also get points 
	 * eg: 3,5 ; 3 stands for almost correct answer & 5 for full mark
	 * @param candidateAnswer
	 * @param correctAnswer
	 * @return
	 */
	public int gradeMultiChoices(List<Integer> candidateChoices,  CorrectAnswerBean correctAnswer){
		if (correctAnswer == null) {
			System.err.println("Error Topic ID in candidate answer");
			return -1;
		}
		
		//random order in candidate's answer, so compare them one by one
		boolean isError = false;
		String[] points				= correctAnswer.getPoints().trim().split(",");	
		String correctChoices		= correctAnswer.getChoiceId().trim();
		String[] choices			= correctChoices.split(",");
		
		if (candidateChoices.size() > choices.length || candidateChoices.size() == 0) {
			isError = true;
		
		}else{
			for (int cnt=0; cnt < candidateChoices.size(); cnt++) {
				//traverse correct answers to judge whether it's right
				if(!correctChoices.contains(candidateChoices.get(cnt).toString())){
					isError = true;
					break;
					
				}
			}
		}
		
		if(isError){
			return 0;
			
		}else if(choices.length == candidateChoices.size()){
			return Integer.parseInt(points[1]);		//full mark
			
		}else 
			return Integer.parseInt(points[0]);		//partly correct
	}
	
	/**
	 * every correctly matched get 1 unit point, error doesn't stop 
	 * @param candidateAnswer
	 * @param correctAnswer
	 * @return
	 */
	public int gradeMatching(List<Integer> collection, CorrectAnswerBean correctAnswer){
		if (correctAnswer == null) {
			System.err.println("Error Topic ID in candidate answer");
			return -1;
		}
		
		String[] correctChoices		= correctAnswer.getChoiceId().trim().split(",");
		int correctNum = 0;
		int unitPoint  = Integer.parseInt(correctAnswer.getPoints());
		
		for (int i = 0; i < collection.size(); i++) {
			if(correctChoices[i].equals(collection.get(i).toString()))
				correctNum ++;
			
		}
		
		return correctNum * unitPoint;
	}
	
	/**
	 * Grading the whole paper for a candidate
	 * 1. grading single choice
	 * 2. grading multi-choices
	 * 3. grading judgment choice
	 * 4. grading matching
	 * @param examBean
	 * @return
	 */
	public int gradePaper(ExamBean examBean){
		int singleChoiceScore	=	0;
		int multiChoicesScore	=	0;
		int judgeChoiceScore	=	0;
		int matchingScore		=	0;
		
		//load correct answer from database
		
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		int paperId=examBean.getPaperId();
		List<CorrectAnswerBean> correctAnswerBeans = topicMapperI.getCorrectAnswerByPaperId(paperId);	//TO DO: mapping from DB
		sqlSession.close();
		

		Map<Integer, CorrectAnswerBean> correctAnswerMap = new HashMap<Integer, CorrectAnswerBean>(128);
		for (CorrectAnswerBean correctAnswerBean : correctAnswerBeans) {
			correctAnswerMap.put(correctAnswerBean.getTopicId(), correctAnswerBean);	//for easily fetching
		}
		
		//single choice grading
		for(SingleChoiceBean singleChoice : examBean.getSingleChoiceList()){
			int topicId = singleChoice.getId();
			
			singleChoiceScore += gradeSingleChoice(singleChoice.getChoiceId(), correctAnswerMap.get(topicId));
			
		}
		System.out.println("single choice grade: "+singleChoiceScore);
		//judgment grading
		for(JudgementBean judgementBean : examBean.getJudgementList()){
			int topicId = judgementBean.getId();
			
			judgeChoiceScore += gradeJudgement(judgementBean.getChoiceId(), correctAnswerMap.get(topicId));
			
		}
		
		//multi-choice grading
		for(MultiChoicesBean multiChoicesBean : examBean.getMultiChoicesList()){
			int topicId = multiChoicesBean.getId();
			
			multiChoicesScore += gradeMultiChoices(multiChoicesBean.getChoiceIdList(), correctAnswerMap.get(topicId));
			
		}
		
		//matching grading
		for(MatchingBean matchingBean : examBean.getMatchingList()){
			int topicId = matchingBean.getId();
			//get answer map, ordered by item id
			List<Integer> matchList=new ArrayList<>();
			TreeMap<Integer, Integer> orderedChoiceMap = new TreeMap<>(matchingBean.getChoiceIdMap());
			
			matchList.addAll(orderedChoiceMap.values());	//key is item id, value is candidate's answer
			
			System.out.println("Matching Topic "+topicId+" : answers: " +matchList);
			matchingScore += gradeMatching(matchList, correctAnswerMap.get(topicId));
		}
		
		return singleChoiceScore + matchingScore + multiChoicesScore + judgeChoiceScore;
	}
	
	//Unit Test
	public static void main (String [] args) {
		CorrectAnswerBean correctAnswer = new CorrectAnswerBean("1", "3 ");
		GradingService service = new GradingService();
		System.out.println(service.gradeSingleChoice(1, correctAnswer)); //single choice test
		
		correctAnswer.setChoiceId("1");
		System.out.println(service.gradeJudgement(1, correctAnswer));	//judge choice test
		
		correctAnswer.setChoiceId("1,2,3");
		correctAnswer.setPoints("3,5");
		List<Integer> candidateChoices = new ArrayList<>();
		candidateChoices.add(1);
		candidateChoices.add(2);
		//candidateChoices.add(3);
		
		System.out.println(service.gradeMultiChoices(candidateChoices, correctAnswer)); //multi-choices test
		
		correctAnswer.setPoints("2"); 	//2 points for each correct item
		correctAnswer.setChoiceId("1,3,4,2");
		candidateChoices.add(4);
		candidateChoices.add(5);	//candidate answer: 1,2,4,5 only two correct choices
		System.out.println(service.gradeMatching(candidateChoices, correctAnswer));
	}
	
}
