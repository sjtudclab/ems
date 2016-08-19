package org.dclab.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.dclab.model.Paper4PDF;
import org.dclab.model.Topic4PDF;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by zhaoz on 2016/8/18.
 */
public class PDFWriter {
	private static BaseFont baseFont;    
    private static Font bigFont;
    private static Font redFont;
    private static Font subFont; 
    private static Font smallBold;
    private static Font	normalFont;

    static{
    	try {
    		//the default path of class is src/
			baseFont	=	BaseFont.createFont("SIMHEI.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
			bigFont		=	new Font(baseFont, 18, Font.BOLD);
			subFont		=	new Font(baseFont, 14, Font.BOLD);
			smallBold	=	new Font(baseFont, 11, Font.BOLD);
			normalFont	=	new Font(baseFont, 11, Font.NORMAL);
			redFont 	= 	new Font(baseFont, 11, Font.NORMAL, BaseColor.RED);
			
    	} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
    	
    }
    
    /**
     * Write paper to a PDF, Saved in path
     * @param paper
     * @param path
     */
    public static void writePaper(Paper4PDF paper, String path){
    	 try {
    		 
             Document document = new Document();
             
             String fileName	=	path +File.separator + paper.fileName + ".pdf";
             PdfWriter.getInstance(document, new FileOutputStream(fileName));
             
             document.addAuthor(System.getProperty("user.name"));
             document.open();
             
             addTitle(document, paper.title);
             addContent(document, paper.topicList, paper.topicNameList);
             
             document.close();
             System.out.println("导出 "+fileName+" 成功！");
             
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
    }
    

    private static void addTitle(Document document, String title)  throws DocumentException {
        Paragraph titlePara = new Paragraph();
        
        //addEmptyLine(titlePara, 1);       //empty line to make it beautiful
		titlePara.add(new Paragraph(title, subFont));
        titlePara.setAlignment(Element.ALIGN_CENTER);        
        addEmptyLine(titlePara, 2);

        document.add(titlePara);       
    }

    private static void addContent(Document document, List<List<Topic4PDF>> topicList, List<String> names) 
    		throws DocumentException {
    	
    	//第一种题型为了跟title 在同一页，不使用chapter
        Anchor anchor = new Anchor("1."+names.get(0), bigFont);
        Paragraph firstPara	=	new Paragraph(anchor);
        List<Topic4PDF> firstTopic	=	topicList.get(0);
        for (int i = 0; i < firstTopic.size(); i++) {
        	Topic4PDF topic = firstTopic.get(i);
        	Paragraph para = new Paragraph("1."+(i+1)+". "+topic.content+"             "+topic.answer, subFont);
        	addEmptyLine(para, 1);
        	for ( String item : topic.items) {
				para.add(new Paragraph(item, normalFont));
			}
        	addEmptyLine(para, 1);
        	firstPara.add(para);
		}
        
        document.add(firstPara);
        
        //use chapter to record the same type of topics
        for (int i = 1; i < topicList.size(); i++) {
        	 anchor = new Anchor(names.get(i), bigFont);
        	 Chapter chapter = new Chapter(new Paragraph(anchor), i+1);
        	 
             List<Topic4PDF> topics	=	topicList.get(i);
             for (int i1 = 0; i1 < topics.size(); i1++) {
             	Topic4PDF topic = topics.get(i1);
             	Paragraph para = new Paragraph(topic.content+"             "+topic.answer, subFont);
             	addEmptyLine(para, 1);
             	for ( String item : topic.items) {
     				para.add(new Paragraph(item, normalFont));
     			}
             	addEmptyLine(para, 1);
             	chapter.addSection(para);
     		}
             
             document.add(chapter);	//add this type of topic to document
		}      

    }
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" ",normalFont));
        }
    }
    
    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section section) {
    	com.itextpdf.text.List list = new com.itextpdf.text.List(true, false, 10);
        list.add(new ListItem("岂因祸福避趋之",normalFont));
        list.add(new ListItem("谈笑风生又一年",normalFont));
        list.add(new ListItem("人间正道是沧桑",normalFont));
        section.add(list);
    }
    
    public static void main(String[] args) {
    	String title	=	 "专业： 软件工程          科目： 政治        考生：xx   准考证号： 1234 ";
    	
    	List<List<Topic4PDF>> topicList	=	new ArrayList<>();
		List<String> topicNameList	=	new ArrayList<String>();
		topicNameList.add("选择题");
		topicNameList.add("简答题");
		
		List<Topic4PDF>	singleChoiceList	=	new ArrayList<>();
		String[] strings	=	{"1. 岂因祸福避趋之","2. 谈笑风生又一年","3.蜡炬成灰泪始干"};
		List<String> items = Arrays.asList(strings);
		Topic4PDF	topic4pdf	=	new Topic4PDF("苟利国家生死已的下一句诗是什么？", "1", items);
		for(int i = 1; i < 4; i++){
			topic4pdf.answer	=	i+"";
			singleChoiceList.add(topic4pdf);
		}
		topicList.add(singleChoiceList);
		
		List<Topic4PDF> shortAnswerList		=	new ArrayList<>();
		String content	=	"6月19日， “寰行中国®”2016中国文化之旅在成都启程啦！"
				+ "这次寰行之旅将通过7500多公里的亲历体验及国家地理的记录与发现，探寻在中华大地上多姿多彩、原生态的民族文化。你怎么看？";
		items	=	new ArrayList<>();
		items.add(
				"苏古笃响起，就听到了千年前的纳西，悠久、缓慢。纳西古乐的音色仿若古画，在黯淡的色调中勾勒出漫长的时光，最后，每一个音都滑向了消逝。"
				+ "“寰行中国®”中国文化之旅第五程，寻访了世界上最古老的音乐——纳西古乐。");
		Topic4PDF	topic4pdf2	=	new Topic4PDF(content, "", items);
		for (int i = 0; i < strings.length; i++) {
			shortAnswerList.add(topic4pdf2);
		}
		topicList.add(shortAnswerList);
		
		Paper4PDF	paper	=	new Paper4PDF("1234", title, topicList, topicNameList);
        PDFWriter.writePaper(paper, "D:\\");
    }
}
