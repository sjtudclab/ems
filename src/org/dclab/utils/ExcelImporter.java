package org.dclab.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dclab.model.SubjectRow;
import org.dclab.service.ImportService;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoz on 2016/8/15.
 */
public class ExcelImporter {
    private Workbook workbook;
    private Sheet sheet;
    private Map<SubjectRow, Integer> paperIdMap;
    
    
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
    
    //default data
    private static final int DEFAULT_EARLIEST_SUB	=	30;	//default: 30 min
    private static final int DEFAULT_LATEST_LOGIN	=	30;
    private static final int DEFAUTL_SHOW_MARK		=	1;	//default show mark: yes
    
    //sheet name
    private static final String SUBJECT_ST	=	"科目";
    private static final String SINGLE_ST	=	"单选题";
    private static final String MULTI_ST	=	"多选题";
    private static final String JUDGE_ST	=	"判断题";
    private static final String MATCH_ST	=	"匹配题";
    private static final String SHORT_ST	=	"简答题";
    private static final String FILL_ST		=	"填空题";
    private static final String MACHINE_ST	=	"上机题";
    
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
                this.paperIdMap	=	new HashMap<SubjectRow, Integer>();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文档不存在!");

            } catch (InvalidFormatException e) {
                e.printStackTrace();
                throw new RuntimeException("文档格式不正确!");

            }
        } else
            throw new RuntimeException("文档格式不正确!");
    }

    public SubjectRow readSubject(int startRow) {
		SubjectRow subjectRow = new SubjectRow();
		Row row 	= 	sheet.getRow(startRow);
		Cell cell 	= 	null;
		
		cell 		=	row.getCell(PRO_NAME);
		subjectRow.setProName(cell == null ? null : cell.getStringCellValue());
		
		cell		=	row.getCell(PRO_ID);
		if(cell == null)
			throw new RuntimeException("专业编号不能为空！");
		subjectRow.setProId(cell.getStringCellValue());
		
		cell		=	row.getCell(SUB_NAME);
		subjectRow.setSubName(cell == null ? null : cell.getStringCellValue());
		
		cell		=	row.getCell(SUB_ID);
		if(cell == null)
			throw new RuntimeException("科目编号不能为空！");
		subjectRow.setSubId(cell.getStringCellValue());
		
		cell		=	row.getCell(PAPER_NO);
		if(cell == null)
			throw new RuntimeException("试卷编号不能为空！");
		subjectRow.setPaperNum(cell.getStringCellValue());
		
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
    	for (int i = SUBJECT_1_ROW; i < this.sheet.getLastRowNum(); i++) {
    		SubjectRow row =	readSubject(i);
			int paperId	=	service.importSubject(row);
			if (!paperIdMap.containsKey(row)) {
				row.setPaperId(paperId);
				paperIdMap.put(row, paperId);
			}
		}
    }

    public static void main(String[] ags) {
        String fileName = "E:\\dclab\\考试管理云平台需求材料\\试卷模板_20160813_丁.xls";//试卷模板.xlsx
        ExcelImporter excel = new ExcelImporter(fileName);
        //System.out.println(excel.readLine(0));
    }
}
