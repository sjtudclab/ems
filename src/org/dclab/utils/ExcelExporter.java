package org.dclab.utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.dclab.model.ScoreCollectBean;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by zhaoz on 2016/8/18.
 */
public class ExcelExporter {
    private String fileName;
    private static String[] markHeaders	=	{"专业名", "科目名",  "试卷号", "准考证号", "考生姓名", "成绩"};
    private static String[] subjctHeaders = {"专业名称", "专业编号", "科目名称", "科目编号", "试卷号", "考试时长（分钟）",
    		"最早提前交卷时间（默认为30min)", "最晚登陆时间(默认为30min)", "考试结束后是否立即显示分数（1是,0否,默认为1是）"};

    public ExcelExporter(String fileName){
        this.fileName   =   fileName;
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
            HSSFWorkbook workbook   =   new HSSFWorkbook();
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
            workbook.close();

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
            HSSFWorkbook workbook   =   new HSSFWorkbook();
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
                List  cellList   =   data.get(i-1);
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
                    else
                        cell.setCellValue(o.toString());
                }
            }

            FileOutputStream fos    =   new FileOutputStream(fileName);
            workbook.write(fos);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("导出文件路径不正确！");

        }

        System.out.println("成功导出："+fileName);
    }
    
    public static void main(String args[]){
        //String[] headers = {"专业名", "科目名",  "准考证号", "考生姓名", "成绩","日期"};
        ExcelExporter excel =   new ExcelExporter("D:\\test.xls");
        List<ScoreCollectBean>  list=   new ArrayList<ScoreCollectBean>();
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

        excel.exportMarks(list,"成绩汇总");
    }
}

