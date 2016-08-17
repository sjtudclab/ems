package org.dclab.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dclab.model.CandidatePaperRelationRow;
import org.dclab.model.CandidateRoomRelationRow;
import org.dclab.model.FillBlankRow;
import org.dclab.model.JudgementRow;
import org.dclab.model.MachineTestRow;
import org.dclab.model.MatchingRow;
import org.dclab.model.MultiChoicesRow;
import org.dclab.model.ShortAnswerRow;
import org.dclab.model.SingleChoiceRow;
import org.dclab.model.SubjectRow;
import org.dclab.model.TopicRow;
import org.dclab.service.ImportService;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoz on 2016/8/15.
 */
public class ExcelImporter {
    private Workbook workbook;
    private Sheet sheet;								
    private Map<String, Integer>	paperIdMap;			//get paper id by paper number, used in topic parsing
    													//get paper id by subject info, used in room parsing
    private static Map<SubjectRow, Integer> subjectPaperMap	=	new HashMap<SubjectRow, Integer>();;	
    
    //subject sheet column index
    private static final int PRO_NAME	=	0;
    private static final int PRO_ID		=	1;
    private static final int SUB_NAME	=	2;
    private static final int SUB_ID		=	3;
    private static final int PAPER_NO	=	4;
    private static final int DURATION	=	5;
    private static final int EARLIEST_SUB	=	6;
    private static final int LATEST_LOGIN	=	7;
    private static final int SHOW_MARK	=	8;
    
    //common TOPIC sheet column index
    private static final int T_PAPER_NO	=	0;
    private static final int T_NUMBER	=	1;
    private static final int T_TOPIC_CTX=	3;
    private static final int T_CORRECT_A=	4;
    private static final int T_FULL_MARK=	5;
    private static final int T_IMG		=	6;
    private static final int T_AUDIO	=	7;
    private static final int T_VIDEO	=	8;
    private static final int T_CHOICE_N	=	9;
    private static final int T_CHOICE_1	=	10;
    
    //multi-choice sheet unique column index
    private static final int M_HALF_MARK=	6;
    private static final int M_IMG		=	7;
    private static final int M_AUDIO	=	8;
    private static final int M_VIDEO	=	9;
    private static final int M_CHOICE_N	=	10;
    private static final int M_CHOICE_1	=	11;
    
    //candidate-paper sheet unique column index
    private static final int C_UID		=	0;
    private static final int C_NAME		=	1;
    private static final int C_GENDER	=	2;
    private static final int C_CID		=	3;
    private static final int C_PHOTO	=	4;
    private static final int C_PRO_ID	=	5;
    private static final int C_SUB_ID	=	7;
    private static final int C_PAPER_NO	=	9;
    
    //candidate-room sheet unique column index
    private static final int R_NAME		=	0;
    private static final int R_START_TM	=	1;
    private static final int R_SEAT_NO	=	2;
    private static final int R_IP		=	3;
    private static final int R_UID		=	4;
    
    //default data
    private static final int DEFAULT_EARLIEST_SUB	=	30;	//default: 30 min
    private static final int DEFAULT_LATEST_LOGIN	=	30;
    private static final int DEFAUTL_SHOW_MARK		=	1;	//default show mark: yes
    private static final int DEFAUTL_CHOICE_N		=	4;	//default 4 choices for single choice topic
    
    //sheet name
    private static final String SUBJECT_ST	=	"科目";
    private static final String SINGLE_ST	=	"单选题";
    private static final String MULTI_ST	=	"多选题";
    private static final String JUDGE_ST	=	"判断题";
    private static final String MATCH_ST	=	"匹配题";
    private static final String SHORT_ST	=	"简答题";
    private static final String FILL_ST		=	"填空题";
    private static final String MACHINE_ST	=	"上机题";
    private static final String CAN_PAPER_ST=	"考生试卷安排";
    private static final String CAN_ROOM_ST=	"考生考场安排";
    //start row in each sheet
    private static final int SUBJECT_1_ROW	=	1;
    private static final int TOPIC_1_ROW	=	1;
    
    public ExcelImporter(String fileName) {
        if (fileName == null || fileName.length() == 0)
            throw new RuntimeException("导入文档为空!");

        else if (fileName.toLowerCase().endsWith("xls") || fileName.toLowerCase().endsWith("xlsx")) {
            try {
                this.workbook = WorkbookFactory.create(new File(fileName));
                //this.sheet = workbook.getSheetAt(0); //default from 0
                this.paperIdMap			=	new HashMap<String, Integer>();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文档不存在或正在被占用!");

            } catch (InvalidFormatException e) {
                e.printStackTrace();
                throw new RuntimeException("文档格式不正确!");

            }
        } else
            throw new RuntimeException("文档格式不正确!");
    }

    /**
     * read 1 line in Subject Sheet to construct a subject row for paperID producing
     * @param startRow
     * @return
     */
    public SubjectRow readSubject(int startRow) {
		SubjectRow subjectRow = new SubjectRow();
		Row row 	= 	sheet.getRow(startRow);
		Cell cell 	= 	null;
		
		cell 		=	row.getCell(PRO_NAME);
		subjectRow.setProName(cell == null ? null : cell.getStringCellValue().trim());
		
		cell		=	row.getCell(PRO_ID);
		if(cell == null)
			throw new RuntimeException("专业编号不能为空！");
		subjectRow.setProId(cell.getStringCellValue().trim());
		
		cell		=	row.getCell(SUB_NAME);
		subjectRow.setSubName(cell == null ? null : cell.getStringCellValue().trim());
		
		cell		=	row.getCell(SUB_ID);
		if(cell == null)
			throw new RuntimeException("科目编号不能为空！");
		subjectRow.setSubId(cell.getStringCellValue().trim());
		
		cell		=	row.getCell(PAPER_NO);
		if(cell == null)
			throw new RuntimeException("试卷编号不能为空！");
		subjectRow.setPaperNum(cell.getStringCellValue().trim());
		
		cell		=	row.getCell(DURATION);
		if(cell == null)
			throw new RuntimeException("考试时长不能为空！");
		subjectRow.setDuration((int)cell.getNumericCellValue());
		
		cell 		=	row.getCell(EARLIEST_SUB);
		subjectRow.setEarliestSubmit(cell == null ? DEFAULT_EARLIEST_SUB : (int)cell.getNumericCellValue());
		
		cell		=	row.getCell(LATEST_LOGIN);
		subjectRow.setLatestLogin(cell == null ? DEFAULT_LATEST_LOGIN : (int)cell.getNumericCellValue());
		
		cell		=	row.getCell(SHOW_MARK);
		subjectRow.setShowMark(cell == null ? DEFAUTL_SHOW_MARK : (int)cell.getNumericCellValue());
		
		return subjectRow;
	}
    
    /**
     * parsing subject sheet, insert to DB, construct map<SubjectRow, paperId> for later use 
     */
    public void parseSubjectSheet(){
    	this.sheet	=	workbook.getSheet(SUBJECT_ST);	//locate in subject sheet
    	if (this.sheet == null) {
    		throw new RuntimeException("科目sheet不能为空！");
		}
    	
    	ImportService service = new ImportService();
    	
    	//from the 1st data line to the end of data line, including the last row (0-based)
    	for (int i = SUBJECT_1_ROW; i <= this.sheet.getLastRowNum(); i++) {
    		SubjectRow row =	readSubject(i);
			int paperId	=	service.importSubject(row);
			if (!subjectPaperMap.containsKey(row)) {
				row.setPaperId(paperId);
				subjectPaperMap.put(row, paperId);
				paperIdMap.put(row.getPaperNum(), paperId);
			}
		}
    }
    
    /**
     * read one line in SingleChoiceTopic sheet to construct a SingleChoiceRow
     * @param lineNo
     * @return
     */
    public SingleChoiceRow readSingleChoiceRow(int lineNo){
    	
    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	SingleChoiceRow	singleChoiceRow	=	new SingleChoiceRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	singleChoiceRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	singleChoiceRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	singleChoiceRow.setCorrectAnswerIndex((int)cell.getNumericCellValue());	//1-based, remember to change it to 0-based
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	singleChoiceRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	singleChoiceRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_AUDIO);
    	singleChoiceRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_VIDEO);
    	singleChoiceRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_CHOICE_N);
    	int	choiceN	=	(cell == null) ? DEFAUTL_CHOICE_N : (int)cell.getNumericCellValue();
    	
    	List<String> choiceList	=	new ArrayList<String>();
    	for (int i = T_CHOICE_1; i < T_CHOICE_1 + choiceN; i++) {
			cell	=	row.getCell(i);
			if (cell == null) {
				throw new RuntimeException("选项"+(i + 1 - T_CHOICE_1  )+"不能为空！");
			}
			choiceList.add(cell.getStringCellValue());
		}
    	singleChoiceRow.setChoiceList(choiceList);
    	
    	return singleChoiceRow;
    }
    
    public void parseSingleChoice(){
    	this.sheet	=	workbook.getSheet(ExcelImporter.SINGLE_ST);
    	if(this.sheet	==	null)
    		return;	//no such type of topic
    	
    	ImportService service = new ImportService();
    	
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readSingleChoiceRow(i));
    	}
    	
    	service.importTopic(topicList);
    }

    /**
     * Read 1 line of multi-choices topic
     * @param lineNo
     * @return
     */
    public MultiChoicesRow readMultiChoiceRow(int lineNo) {
    	
    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	MultiChoicesRow	topicRow	=	new MultiChoicesRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	topicRow.setCorrectAnswerIndices(cell.getStringCellValue());	//answer: 1,2,3
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(M_HALF_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setHalfMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(M_IMG);
    	topicRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(M_AUDIO);
    	topicRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(M_VIDEO);
    	topicRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(M_CHOICE_N);
    	int	choiceN	=	(cell == null) ? DEFAUTL_CHOICE_N : (int)cell.getNumericCellValue();
    	
    	List<String> choiceList	=	new ArrayList<String>();
    	for (int i = M_CHOICE_1; i < M_CHOICE_1 + choiceN; i++) {
			cell	=	row.getCell(i);
			if (cell == null) {
				throw new RuntimeException("选项"+(i + 1 - M_CHOICE_1  )+"不能为空！");
			}
			choiceList.add(cell.getStringCellValue());
		}
    	topicRow.setChoiceList(choiceList);
    	
    	return topicRow;
	}
    
    /**
     * parsing multi-choice sheet
     */
    public void parseMultiChoice(){
    	this.sheet	=	workbook.getSheet(MULTI_ST);	//multi-choice sheet parsing
    	if (this.sheet == null) {
			return;
		}
    	
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readMultiChoiceRow(i));
    	}
    	
    	service.importTopic(topicList);
    }
    
    /**
     * Read 1 line in Judgement sheet in excel
     * @param lineNo
     * @return
     */
    public JudgementRow readJudgementLine(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	JudgementRow	topicRow	=	new JudgementRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	topicRow.setCorrectAnswer((int)cell.getNumericCellValue());	//answer: 1 true, 0 false
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	topicRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_AUDIO);
    	topicRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_VIDEO);
    	topicRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	return topicRow;
    }
    
    public void parseJudgement(){
    	this.sheet	=	workbook.getSheet(JUDGE_ST);	//judgement sheet parsing
    	if (this.sheet == null) {
			return;
		}
    	
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readJudgementLine(i));
    	}
    	
    	service.importTopic(topicList);
    	
    }
    
    public MatchingRow readMatchingRow(int lineNo){
    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	MatchingRow	topicRow	=	new MatchingRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	topicRow.setCorrectAnswerIndices(cell.getStringCellValue());	//answer: 1,2,3
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	topicRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_AUDIO);
    	topicRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_VIDEO);
    	topicRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	//matching items parsing
    	cell		=	row.getCell(T_CHOICE_N);
    	int	choiceN	=	(cell == null) ? DEFAUTL_CHOICE_N : (int)cell.getNumericCellValue();
    	
    	List<String> itemList	=	new ArrayList<String>();
    	for (int i = T_CHOICE_1; i < T_CHOICE_1 + choiceN; i++) {
			cell	=	row.getCell(i);
			if (cell == null) {
				throw new RuntimeException("匹配条目"+(i + 1 - T_CHOICE_1  )+"不能为空！");
			}
			itemList.add(cell.getStringCellValue());
		}
    	topicRow.setItemList(itemList);
    	
    	//matching choices parsing
    	int	choicePos	=	T_CHOICE_N	+	choiceN + 1;
    	cell	=	row.getCell(choicePos);
    	choiceN	=	(cell == null) ? DEFAUTL_CHOICE_N : (int)cell.getNumericCellValue();
    	
    	List<String> choiceList	=	new ArrayList<String>();
    	for (int i = choicePos + 1; i <= choicePos + choiceN; i++) {
			cell	=	row.getCell(i);
			if (cell == null) {
				throw new RuntimeException("待匹配选项"+(i - choicePos  )+"不能为空！");
			}
			choiceList.add(cell.getStringCellValue());
		}
    	topicRow.setChoiceList(choiceList);
    	
    	return topicRow;
    }
    
    public void parseMatching(){
    	this.sheet	=	workbook.getSheet(MATCH_ST);	//matching sheet parsing
    	if (this.sheet == null) {
			return;
		}
    	
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readMatchingRow(i));
    	}
    	
    	service.importTopic(topicList);
    }
    
    public ShortAnswerRow readShortAnswerRow(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	ShortAnswerRow	topicRow	=	new ShortAnswerRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	topicRow.setCorrectAnswer(cell.getStringCellValue());	//reference answer: some words
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	topicRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_AUDIO);
    	topicRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_VIDEO);
    	topicRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	return topicRow;
    }
    
    public void parseShortAnswer(){
    	this.sheet	=	workbook.getSheet(SHORT_ST);	//short answer sheet parsing
    	if (this.sheet == null) {
			return;
		}
    	
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readShortAnswerRow(i));
    	}
    	
    	service.importTopic(topicList);
    }
    
    public MachineTestRow readMachineTestRow(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	MachineTestRow	topicRow	=	new MachineTestRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	topicRow.setCorrectAnswerFile(cell.getStringCellValue());	//reference answer: some words
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	topicRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_AUDIO);
    	topicRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_VIDEO);
    	topicRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	return topicRow;
    }
    
    public void parseMachineTest(){
    	this.sheet	=	workbook.getSheet(MACHINE_ST);	//(online judge) machine test sheet parsing
    	if (this.sheet == null) {
			return;
		}
    	
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readMachineTestRow(i));
    	}
    	
    	service.importTopic(topicList);
    }
    
    public FillBlankRow readFillBlankRow(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	FillBlankRow	topicRow	=	new FillBlankRow(paperIdMap.get(cell.getStringCellValue().trim()));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}
    	topicRow.setCorrectAnswer(cell.getStringCellValue());	//reference : all blanks' correct answers
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	topicRow.setImg(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_AUDIO);
    	topicRow.setAudio(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_VIDEO);
    	topicRow.setVideo(cell == null ? null : cell.getStringCellValue().trim());
    	
    	cell		=	row.getCell(T_CHOICE_N);
    	if(cell == null)
    		throw new RuntimeException("填空个数不能为空！");
    	topicRow.setBlankNum((int)cell.getNumericCellValue());
    	
    	return topicRow;
    }
    
    public void parseFillBlank(){
    	this.sheet	=	workbook.getSheet(FILL_ST);	//fill blank sheet parsing
    	if (this.sheet == null) {
			return;
		}
    	
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		topicList.add(readFillBlankRow(i));
    	}
    	
    	service.importTopic(topicList);
    }
    
    public CandidatePaperRelationRow readCandiateRow(int lineNo){
    	Row	row	=	this.sheet.getRow(lineNo);
    	CandidatePaperRelationRow candidateRow	=	new CandidatePaperRelationRow();    	
    	Cell	cell	=	null;
    	
    	cell	=	row.getCell(C_UID);
    	if (null == cell) {
    		throw new RuntimeException("准考证号不能为空！");
		}
    	candidateRow.setUid(cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_NAME);
    	candidateRow.setUname(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_GENDER);
    	candidateRow.setGender(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_CID);
    	candidateRow.setCid(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_PHOTO);
    	candidateRow.setPhoto(null == cell ? null : cell.getStringCellValue());
    	
    	//in order to get paper id in DB, assemble subject row, retrieve id in subjectPaperMap
    	SubjectRow	subjectRow	=	new	SubjectRow();
    	cell	=	row.getCell(C_PRO_ID);
    	subjectRow.setProId(cell == null ? null : cell.getStringCellValue());
    	cell	=	row.getCell(C_SUB_ID);
    	subjectRow.setSubId(cell == null ? null : cell.getStringCellValue());
    	cell	=	row.getCell(C_PAPER_NO);
    	if (cell == null) {
    		throw new RuntimeException("试卷编号不能为空！");
		}
    	subjectRow.setPaperNum(cell.getStringCellValue());
    	Integer paperId	=	subjectPaperMap.get(subjectRow);
    	if (null == paperId) {
			throw new RuntimeException("数据库中没有这套试卷！");
		}
    	candidateRow.setPaperId(paperId);
    	
    	return candidateRow;
    }
    
    /**
     * Get all candidates info & their paper id
     */
    public void parseCandidatePaper(){
    	this.sheet	=	workbook.getSheet(CAN_PAPER_ST);
    	if (this.sheet == null) {
			throw new RuntimeException("没有任何考生信息！");
		}
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<CandidatePaperRelationRow> candiateList	=	new ArrayList<>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		candiateList.add(readCandiateRow(i));
    	}

    	System.out.println(candiateList);
    	ImportService service = new ImportService();
    	//import candiate service
    }
    
    public CandidateRoomRelationRow readCandidateRoomRow(int lineNO){
    	Row row	=	this.sheet.getRow(lineNO);
    	CandidateRoomRelationRow roomRow	=	new CandidateRoomRelationRow();
    	Cell cell	=	null;
    	
    	cell	=	row.getCell(R_NAME);
    	if (null == cell) {
    		throw new RuntimeException("考场不能为空！");
		}
    	roomRow.setRoomName(cell.getStringCellValue());
    	
    	cell	=	row.getCell(R_START_TM);
    	if (null == cell) {
    		throw new RuntimeException("开考时间不能为空！");
		}
    	roomRow.setStartTime(new Timestamp(cell.getDateCellValue().getTime()));	//Date -> TimeStamp
    	
    	cell	=	row.getCell(R_SEAT_NO);
    	if (null == cell) {
    		throw new RuntimeException("座位号不能为空！");
		}
    	roomRow.setSeatNum((int)cell.getNumericCellValue());
    	
    	cell	=	row.getCell(R_IP);
    	roomRow.setIp(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(R_UID);
    	if (null == cell) {
    		throw new RuntimeException("准考证号不能为空！");
		}
    	roomRow.setUid(cell.getStringCellValue());
    	
    	return roomRow;
    }
    
    public void parseCanidateRoom(){
    	this.sheet	=	workbook.getSheet(CAN_ROOM_ST);
    	if (this.sheet == null) {
			throw new RuntimeException("没有任何考场信息！");
		}
    	
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<CandidateRoomRelationRow> roomList	=	new ArrayList<>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		roomList.add(readCandidateRoomRow(i));
    	}

    	System.out.println(roomList);
    	ImportService service = new ImportService();
    	//import candiate service
    }
    
    public static void main(String[] ags) {
        String fileName = "E:\\dclab\\考试管理云平台需求材料\\试卷模板_final.xls";//试卷模板.xlsx
        ExcelImporter excel = new ExcelImporter(fileName);
        //System.out.println(excel.readLine(0));
        excel.parseSubjectSheet();
        excel.parseSingleChoice();
        excel.parseMultiChoice();
        excel.parseJudgement();
        excel.parseMatching();
        excel.parseShortAnswer();
        excel.parseFillBlank();
        excel.parseMachineTest();
        
        fileName	=	"E:\\dclab\\考试管理云平台需求材料\\考生试卷及考场安排模板_0815_赵.xls";
        excel		=	new ExcelImporter(fileName);
        excel.parseCandidatePaper();
        excel.parseCanidateRoom();
        
    }
}
