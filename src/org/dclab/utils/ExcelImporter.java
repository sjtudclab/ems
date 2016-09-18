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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    private HashSet<String> fileSet;
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
    
    //matching sheet, unique, denote the start of items to be matched
    private static int M_START	=	15;
    
    //short answer sheet unique pdf cell
    private static final int S_PDF		=	9;
    
    //online-judge sheet unique pdf cell
    private static final int O_PDF		=	9;
    
    //blank-filing sheet unique pdf cell
    private static final int B_PDF		=	10;
    
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
    
    public HashSet<String> getFileSet() {
		return fileSet;
	}

	public void setFileSet(HashSet<String> fileSet) {
		this.fileSet = fileSet;
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
		String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
		}
		subjectRow.setProName(pNum);
		
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
    	for (int i = SUBJECT_1_ROW; i < this.sheet.getLastRowNum(); i++) {
    		SubjectRow row =	readSubject(i);
    		if (row == null) 
				break;
			int paperId	=	service.importSubject(row);
			//if (!subjectPaperMap.containsKey(row)) {
				row.setPaperId(paperId);
				subjectPaperMap.put(row, paperId);
				paperIdMap.put(row.getPaperNum(), paperId);
			//}
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
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
    		//throw new RuntimeException("试卷编号不能为空！");
		}
    	System.out.println("paper id map : "+paperIdMap);
    	
    	SingleChoiceRow	singleChoiceRow	=	new SingleChoiceRow(paperIdMap.get(pNum));
    	
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
    	if(cell == null)
    		singleChoiceRow.setImg(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		singleChoiceRow.setImg(file);
    	}
    	
    	cell		=	row.getCell(T_AUDIO);
    	if(cell == null)
    		singleChoiceRow.setAudio(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		singleChoiceRow.setAudio(file);
    	}
    	
    	cell		=	row.getCell(T_VIDEO);
    	if(cell == null)
    		singleChoiceRow.setVideo(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		singleChoiceRow.setVideo(file);
    	}
    	
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
    	//System.out.println(singleChoiceRow);
    	return singleChoiceRow;
    }
    
    public void parseSingleChoice(){
    	this.sheet	=	workbook.getSheet(ExcelImporter.SINGLE_ST);
    	if(this.sheet	==	null)
    		return;	//no such type of topic
    	
    	ImportService service = new ImportService();
    	
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	//System.out.println("Row num : "+rowNum);
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		SingleChoiceRow row = readSingleChoiceRow(i);
    		System.out.println(row);
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
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
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
    		//throw new RuntimeException("试卷编号不能为空！");
		}
    	
    	MultiChoicesRow	topicRow	=	new MultiChoicesRow(paperIdMap.get(pNum));
    	
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
    	if (cell == null) {
			topicRow.setImg(null);
			
		}else{
			String file = cell.getStringCellValue().trim();
			fileSet.add(file);
			topicRow.setImg(file);
			
		}
    	
    	
    	cell		=	row.getCell(M_AUDIO);
    	if (cell == null) {
			topicRow.setAudio(null);
		}else{
			String file = cell.getStringCellValue().trim();
			fileSet.add(file);
			topicRow.setAudio(file);
		}
    	
    	cell		=	row.getCell(M_VIDEO);
    	if (cell == null) {
			topicRow.setVideo(null);
		}else{
			String file = cell.getStringCellValue().trim();
			fileSet.add(file);
			topicRow.setVideo(file);
		}
    	
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
    	//System.out.println(topicRow);
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
    	//System.out.println("Multi-Row num : "+rowNum);
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		MultiChoicesRow row = readMultiChoiceRow(i);
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
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
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
		}
    	
    	JudgementRow	topicRow	=	new JudgementRow(paperIdMap.get(pNum));
    	
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
    	
    	cell		=	row.getCell(T_IMG);
    	if(cell == null)
    		topicRow.setImg(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setImg(file);
    	}
    	
    	cell		=	row.getCell(T_AUDIO);
    	if(cell == null)
    		topicRow.setAudio(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setAudio(file);
    	}
    	
    	cell		=	row.getCell(T_VIDEO);
    	if(cell == null)
    		topicRow.setVideo(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setVideo(file);
    	}
    	
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
    		JudgementRow row = readJudgementLine(i);
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
    	}
    	
    	service.importTopic(topicList);
    	
    }
    
    public MatchingRow readMatchingRow(int lineNo){
    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
		}
    	
    	MatchingRow	topicRow	=	new MatchingRow(paperIdMap.get(pNum));
    	
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
    	if(cell == null)
    		topicRow.setImg(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setImg(file);
    	}
    	
    	cell		=	row.getCell(T_AUDIO);
    	if(cell == null)
    		topicRow.setAudio(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setAudio(file);
    	}
    	
    	cell		=	row.getCell(T_VIDEO);
    	if(cell == null)
    		topicRow.setVideo(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setVideo(file);
    	}
    	
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
    	//int	choicePos	=	T_CHOICE_N	+	choiceN + 1;
    	cell	=	row.getCell(M_START);
    	choiceN	=	(cell == null) ? DEFAUTL_CHOICE_N : (int)cell.getNumericCellValue();
    	
    	List<String> choiceList	=	new ArrayList<String>();
    	for (int i = M_START + 1; i <= M_START + choiceN; i++) {
			cell	=	row.getCell(i);
			if (cell == null) {
				throw new RuntimeException("待匹配选项"+(i - M_START  )+"不能为空！");
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
    	
    	//read title row to locate the column number
    	Row titleRow = sheet.getRow(0);
    	Iterator<Cell> iterator = titleRow.cellIterator();
    	int pos = 0;
    	while(iterator.hasNext()){
    		if("待匹配项个数".equals(iterator.next().getStringCellValue())){
    			M_START	=	pos;
    			break;
    		}
    		pos++;
    	}
    	ImportService service = new ImportService();
    	int	rowNum	=	this.sheet.getLastRowNum() + 1;
    	List<TopicRow> topicList	=	new ArrayList<TopicRow>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		MatchingRow row = readMatchingRow(i);
    		
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
    	}
    	
    	service.importTopic(topicList);
    }
    
    public ShortAnswerRow readShortAnswerRow(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
		}
    	
    	ShortAnswerRow	topicRow	=	new ShortAnswerRow(paperIdMap.get(pNum));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	/*if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}*/
    	topicRow.setCorrectAnswer(cell == null ? null : cell.getStringCellValue());	//reference answer: some words
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	if(cell == null)
    		topicRow.setImg(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setImg(file);
    	}
    	
    	cell		=	row.getCell(T_AUDIO);
    	if(cell == null)
    		topicRow.setAudio(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setAudio(file);
    	}
    	
    	cell		=	row.getCell(T_VIDEO);
    	if(cell == null)
    		topicRow.setVideo(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setVideo(file);
    	}
    	
    	cell		=	row.getCell(S_PDF);
    	if (cell == null) {
			topicRow.setPdf(null);
		}else{
			String file = cell.getStringCellValue().trim();
			fileSet.add(file);
			topicRow.setPdf(file);
			
		}
    	
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
    		ShortAnswerRow row = readShortAnswerRow(i);
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
    		
    	}
    	
    	service.importTopic(topicList);
    }
    
    public MachineTestRow readMachineTestRow(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
		}
    	
    	MachineTestRow	topicRow	=	new MachineTestRow(paperIdMap.get(pNum));
    	
    	cell		=	row.getCell(T_NUMBER);
    	topicRow.setNumber(cell == null ? 0 : (int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_TOPIC_CTX);
    	topicRow.setContent(cell == null ? null : cell.getStringCellValue());
    	
    	cell		=	row.getCell(T_CORRECT_A);
    	/*if (cell == null) {
    		throw new RuntimeException("正确答案不能为空！");
		}*/
    	topicRow.setCorrectAnswerFile(cell == null ? null : cell.getStringCellValue());	//reference answer: some words
    	
    	cell		=	row.getCell(T_FULL_MARK);
    	if (cell == null) {
    		throw new RuntimeException("分值不能为空！");
		}
    	topicRow.setFullMark((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(T_IMG);
    	if(cell == null)
    		topicRow.setImg(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setImg(file);
    	}
    	
    	cell		=	row.getCell(T_AUDIO);
    	if(cell == null)
    		topicRow.setAudio(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setAudio(file);
    	}
    	
    	cell		=	row.getCell(T_VIDEO);
    	if(cell == null)
    		topicRow.setVideo(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setVideo(file);
    	}

    	cell		=	row.getCell(O_PDF);
    	if (cell == null) {
			topicRow.setPdf(null);
		}else{
			String file = cell.getStringCellValue().trim();
			fileSet.add(file);
			topicRow.setPdf(file);
			
		}
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
    		MachineTestRow row = readMachineTestRow(i);
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
    	}
    	
    	service.importTopic(topicList);
    }
    
    public FillBlankRow readFillBlankRow(int lineNo){

    	Row	row		=	this.sheet.getRow(lineNo);
    	Cell cell	=	null;
    	
    	cell		=	row.getCell(T_PAPER_NO);
    	String pNum;
    	if (cell == null || (pNum = cell.getStringCellValue().trim()).length() == 0) {
    		return null;
		}
    	
    	FillBlankRow	topicRow	=	new FillBlankRow(paperIdMap.get(pNum));
    	
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
    	if(cell == null)
    		topicRow.setImg(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setImg(file);
    	}
    	
    	cell		=	row.getCell(T_AUDIO);
    	if(cell == null)
    		topicRow.setAudio(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setAudio(file);
    	}
    	
    	cell		=	row.getCell(T_VIDEO);
    	if(cell == null)
    		topicRow.setVideo(null);
    	else{
    		String file	=	cell.getStringCellValue().trim();
    		fileSet.add(file);
    		topicRow.setVideo(file);
    	}
    	
    	cell		=	row.getCell(T_CHOICE_N);
    	if(cell == null)
    		throw new RuntimeException("填空个数不能为空！");
    	topicRow.setBlankNum((int)cell.getNumericCellValue());
    	
    	cell		=	row.getCell(B_PDF);
    	if (cell == null) {
			topicRow.setPdf(null);
		}else{
			String file = cell.getStringCellValue().trim();
			fileSet.add(file);
			topicRow.setPdf(file);
			
		}
    	
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
    		FillBlankRow row = readFillBlankRow(i);
    		if(row != null)
    			topicList.add(row);
    		else
    			break;
    	}
    	
    	service.importTopic(topicList);
    }
    
    public CandidatePaperRelationRow readCandiateRow(int lineNo){
    	Row	row	=	this.sheet.getRow(lineNo);
    	if (row == null) {
			return null;
		}
    	CandidatePaperRelationRow candidateRow	=	new CandidatePaperRelationRow();    	
    	Cell	cell	=	null;
    	
    	cell	=	row.getCell(C_UID);
    	if (null == cell || cell.getStringCellValue().trim().length() == 0) {
    		return null;
//    		throw new RuntimeException("准考证号不能为空！");
		}
    	candidateRow.setUid(cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_NAME);
    	candidateRow.setUname(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_GENDER);
    	candidateRow.setGender(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_CID);
    	candidateRow.setCid(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(C_PHOTO);
    	if (cell == null) {
    		candidateRow.setPhoto(null);
		}else{
			String file = cell.getStringCellValue();
			candidateRow.setPhoto(file);
			fileSet.add(file);
		}
    	
    	
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
    	System.out.println(candidateRow);
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
    	System.out.println("Row num: "+rowNum);
    	List<CandidatePaperRelationRow> candiateList	=	new ArrayList<>(rowNum);
    	for (int i = TOPIC_1_ROW; i < rowNum; i++) {
    		CandidatePaperRelationRow row = readCandiateRow(i);
    		if(row != null)
    			candiateList.add(row);
    		else
    			break;
    	}

    	//System.out.println(candiateList);
    	ImportService service = new ImportService();
    	//import candiate service
    	service.importCandidatePaper(candiateList);
    }
    
    public CandidateRoomRelationRow readCandidateRoomRow(int lineNO){
    	Row row	=	this.sheet.getRow(lineNO);
    	if (row == null) {
			return null;
		}
    	CandidateRoomRelationRow roomRow	=	new CandidateRoomRelationRow();
    	Cell cell	=	null;
    	
    	cell	=	row.getCell(R_NAME);
    	if (null == cell || cell.getStringCellValue().trim().length() == 0) {
    		return null;
//    		throw new RuntimeException("考场不能为空！");
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
    	
    	try{
    		roomRow.setSeatNum((int)cell.getNumericCellValue());
    	
    	}catch(IllegalStateException exception){
    		String seat	=	cell.getStringCellValue().trim();
    		roomRow.setSeatNum(Integer.parseInt(seat));
    	}
    	cell	=	row.getCell(R_IP);
    	roomRow.setIp(null == cell ? null : cell.getStringCellValue());
    	
    	cell	=	row.getCell(R_UID);
    	if (null == cell) {
    		throw new RuntimeException("准考证号不能为空！");
		}
    	roomRow.setUid(cell.getStringCellValue());
    	System.out.println(roomRow);
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
    		CandidateRoomRelationRow row = readCandidateRoomRow(i);
    		if(row != null)
    			roomList.add(row);
    		else
    			break;
    	}

    	System.out.println(roomList);
    	ImportService service = new ImportService();
    	//import candiate service
    	service.importCandidateRoom(roomList);
    }
    
    public void close(){
    	if(workbook != null){
    		try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public static void main(String[] ags) {
    	HashSet<String> set = new HashSet<String>();
        String fileName = "E:\\综合测试.xls";//试卷模板.xlsx
        ExcelImporter excel = new ExcelImporter(fileName);
        
        excel.setFileSet(set);
        System.out.println("在试卷导入前的set："+set);
        excel.parseSubjectSheet();
        excel.parseSingleChoice();
        excel.parseMultiChoice();
        excel.parseJudgement();
        excel.parseMatching();
        excel.parseShortAnswer();
        excel.parseFillBlank();
        excel.parseMachineTest();
        System.out.println(set);
        excel.close();
        
        String fileName2	=	"E:\\考生试卷及考场安排_综测.xls";
        ExcelImporter excel2 = new ExcelImporter(fileName2);
        set = new HashSet<String>();
        excel2.setFileSet(set);
        excel2.parseCandidatePaper();
        excel2.parseCanidateRoom();
        excel2.close();
        System.out.println(set);
    }
}
