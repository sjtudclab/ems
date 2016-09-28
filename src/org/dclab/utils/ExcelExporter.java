package org.dclab.utils;

import org.apache.ibatis.jdbc.Null;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.dclab.model.ScoreCollectBean;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhaoz on 2016/8/18.
 */
public class ExcelExporter {
    private String fileName;
    private HSSFWorkbook workbook;
    private static String[] markHeaders	=	{"专业名", "专业编号", "科目名", "科目编号", "试卷号", "准考证号", "考生姓名", "成绩"};
    private static String[] subjctHeaders = {"专业名称", "专业编号", "科目名称", "科目编号", "试卷号", "考试时长（分钟）",
    		"最早提前交卷时间（默认为30min)", "最晚登陆时间(默认为30min)", "考试结束后是否立即显示分数（1是,0否,默认为1是）"};

    private static String[] singleHeader 	= {"试卷号","序号","题号","题干","正确答案编号","考生答案编号 ","本题分值","所含图片文件名",
    		"所含音频文件名","所含视频文件名","本题选项个数","选项1","选项2","选项3","选项4"};
    private static String[] mChoiceHeader 	= {"试卷号","序号","题号	","题干","正确答案编号","考生答案编号 ","满分分值","少选分值","所含图片文件名（无则空）",
    		"所含音频文件名","所含视频文件名","本题选项个数","选项1","选项2","选项3","选项4"};
    private static String[] judgeHeader		= {"试卷号","序号","题号","题干","正确答案","考生答案编号 ","本题分值","所含图片文件名","所含音频文件名","所含视频文件名"};
    private static String[] matchHeader		= {"试卷号","序号","题号","题干","正确答案编号","考生答案编号 ","单个匹配正确分值","所含图片文件名","所含音频文件名","所含视频文件名",
    		"匹配选项个数","匹配条目1","匹配条目2","匹配条目3","匹配条目4","待匹配项个数","待匹配项1","待匹配项2","待匹配项3","待匹配项4","待匹配项5"};
    private static String[] shortHeader		= {"试卷号","序号","题号","题干","参考答案","考生答案 ","本题分值","所含图片文件名","所含音频文件名","所含视频文件名","PDF"};
    private static String[] fillBlankHeader = {"试卷号","序号","题号","题干","参考答案","考生答案 ","本题分值","所含图片文件名","所含音频文件名","所含视频文件名","填空个数","PDF"};
    private static String[] machineHeader	= {"试卷号","序号","题号","题干","参考答案","考生答案 ","本题分值","所含图片文件名","所含音频文件名","所含视频文件名","PDF"};
    
    private static Map<String, String[]>  headerMap;
    static {
    	headerMap	=	new HashMap<>();
    	headerMap.put("科目",   subjctHeaders);
    	headerMap.put("单选题",  singleHeader);
    	headerMap.put("多选题",  mChoiceHeader);
    	headerMap.put("判断题",  judgeHeader);
    	headerMap.put("匹配题",  matchHeader);
    	headerMap.put("简答题",  shortHeader);
    	headerMap.put("填空题",  fillBlankHeader);
    	headerMap.put("上机题",  machineHeader);
    }
    
    public ExcelExporter(String fileName){
        this.fileName   =   fileName;
        this.workbook   =   new HSSFWorkbook();
    }

    /**
     * export score collection to excel file
     * @param markList
     */
    public void exportMarks(List<? extends Object> markList, String sheetName){
    	export(markList, markHeaders, sheetName);
    }
    
    /**
     * subjectRow list
     * @param subjectList
     */
    public void exportSubject(List<? extends Object> subjectList, String sheetName){
    	export(subjectList, subjctHeaders, sheetName);
    	try {
			this.workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 每种题型装在一个List<List<String>>当中，是一张二维表；
     * 一种题型的内容填写在一个sheet当中；以sheet name 作为区分
     * 
     * @param paperMap 输入参数为  <题型名，题型内容>
     */
    public void exportStudentPaper(Map<String, List<List<? extends Object>> > paperMap){
    	//遍历每种题型，将其内容写入相应的sheet
    	for(Map.Entry<String, List<List<? extends Object>>> entry : paperMap.entrySet()){
    		
    		exportList(entry.getValue(), headerMap.get(entry.getKey()), entry.getKey());
    	}
    	
    	try {
			this.workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * export bean directly by using Reflection of Java
     * Notice:
     * 1. if this bean has super class(not Object), only public fields are presented
     * 2. the order of fields are: this class' fields in declare order + super class' fields in declare order
     *
     * @param beansList
     */
    public void export(List<? extends Object> beansList, String[] headers, String sheetName){
        try {
            SimpleDateFormat    sf  =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            HSSFSheet   sheet       =   workbook.createSheet(sheetName);
            Row row =   sheet.createRow(0);
            Cell cell   =   null;
            for (int i = 0; i < headers.length; i++){
                String  content     =   headers[i];
                cell    =   row.createCell(i);
                cell.setCellValue(content);
            }

            //data record starts from line 1
            for (int i = 1; i <= beansList.size(); i++) {
                Object  bean   =   beansList.get(i-1);
                row     =   sheet.createRow(i);

                Field[] fields  =   bean.getClass().getFields();    //all public fields, include superclass'
                for (int j = 0; j < fields.length; j++) {
                    Field field =   fields[j];
                    //field.setAccessible(true);        //it must be public
                    Object  o   =   field.get(bean);
                    cell        =   row.createCell(j);
                    if (o instanceof Integer){
                        cell.setCellValue((Integer)o);

                    }else if(o instanceof Date){
                        cell.setCellValue(sf.format((Date)o));  //to string

                    }else if (o instanceof Double) {
                        cell.setCellValue((Double)o);

                    } else if (o instanceof Long) {
                        cell.setCellValue((Long)o);
                    }
                    else
                        cell.setCellValue(o.toString());
                }
            }

            FileOutputStream fos    =   new FileOutputStream(fileName);
            workbook.write(fos);
            //workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("导出文件路径不正确！");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("成功导出："+fileName);

    }

    /**
     * //export list<list>
     *
     * When you need a special order of fields to export, change it to List manually
     * Then use this method to export them
     * @param data
     * @param headers
     */
    public void exportList(List<List<? extends Object>> data, String[] headers, String sheetName){
        try {
            SimpleDateFormat    sf  =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            HSSFSheet   sheet       =   workbook.createSheet(sheetName);
            Row row =   sheet.createRow(0);
            Cell cell   =   null;
            for (int i = 0; i < headers.length; i++){
                String  content     =   headers[i];
                cell    =   row.createCell(i);
                cell.setCellValue(content);
            }

            //data record starts from line 1
            for (int i = 1; i <= data.size(); i++) {
                List<? extends Object>  cellList   =   data.get(i-1);
                row     =   sheet.createRow(i);

                for (int j = 0; j < cellList.size(); j++) {
                    Object  o   =   cellList.get(j);
                    cell        =   row.createCell(j);
                    if (o instanceof Integer){
                        cell.setCellValue((Integer)o);

                    }else if(o instanceof Date){
                        cell.setCellValue(sf.format((Date)o));  //to string

                    }else if (o instanceof Double) {
                        cell.setCellValue((Double)o);

                    } else if (o instanceof Long) {
                        cell.setCellValue((Long)o);
                    }
                    else{
                    	if(o == null) 
                    		cell.setCellValue("");
                    	else
                    		cell.setCellValue(o.toString());
                        
                    }
                }
            }

            FileOutputStream fos    =   new FileOutputStream(fileName);
            workbook.write(fos);
            //workbook.close(); //controlled at outside

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("导出文件路径不正确！");

        }

        System.out.println("成功导出："+fileName);
    }
    
    public void close(){
    	if(workbook != null)
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public static void main(String args[]){
        //String[] headers = {"专业名", "科目名",  "准考证号", "考生姓名", "成绩","日期"};
        ExcelExporter excel =   new ExcelExporter("D:\\test.xls");
       /* List<ScoreCollectBean>  list=   new ArrayList<ScoreCollectBean>();
        for (int i = 0; i < 4; i++) {
        	ScoreCollectBean bean = new ScoreCollectBean();
            bean.setProName("软件工程");
            bean.setSubName("政治");
            bean.setPaperNum(i+"");
            bean.setUid("s_p_"+i);
            bean.setUname("test"+i);
            bean.setMark(new Random().nextInt(100));

            list.add(bean);
        }

        excel.exportMarks(list,"成绩汇总");*/
        
        //测试导出考生试卷
        String[] subject = {"软件工程","a80001","政治","c1","a1","120","30","100","1"};
        List<List<? extends Object>> subjectRow  = new ArrayList<>();
        subjectRow.add(Arrays.asList(subject));
        
        System.out.println(subjectRow);
        
        List<List<? extends Object>> singleList  = new ArrayList<>();
        String[] singleRow	={"a1","2","4","韩国首都是？","2","2","3","","","","4","釜山","首尔","全州","济州岛"};
        singleList.add(Arrays.asList(singleRow));
        
        System.out.println(singleList);
        
        Map<String, List<List<? extends Object>>> map = new HashMap<>();
        map.put("科目", subjectRow);
        map.put("单选题", singleList);
        
        excel.exportStudentPaper(map);
    }
}

