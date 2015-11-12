package kr.ac.kaist.ds.groupd.file;

import java.io.*;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import jxl.format.*;
import jxl.write.Number;


/****** 엑셀 파일 저장을 위한 클래스 ******/

public class SaveFile 
{
	WritableWorkbook myWorkbook;

	public static final int MaxSheet = 16;	// 최대 시트수

	/**** 생성자 ****/
	public SaveFile(String FileName)
	{
		try 
		{
			// 파일생성
			myWorkbook = Workbook.createWorkbook(new File(FileName)); 	// 파일이름을 정하여 생성한다.
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**** 시트생성 및 데이터 Write ****/
	public void WriteData(String SheetName, String Title, int SheetIndex, String column[] , String Row[] , float Data[][], int DataXnum, int DataYnum )
	{
		
		try {
			/*** 시트생성 ***/
			WritableSheet mySheet = myWorkbook.createSheet(SheetName, SheetIndex);  	
			WritableCellFormat numberFormat = new WritableCellFormat(); // 번호 셀
			WritableCellFormat nameFormat = new WritableCellFormat(); // 이름 셀 포멧
			//WritableCellFormat dataFormat = new WritableCellFormat(); // 데이터 셀
			

			/*** 설정 ***/
			numberFormat.setAlignment(Alignment.CENTRE); // 셀 수평 정렬
			numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 정렬
			numberFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // 보더
			numberFormat.setBackground(Colour.GREY_25_PERCENT); //배경

			nameFormat.setAlignment(Alignment.LEFT); // 셀 수평 정렬
			nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 셀 수직 정렬
			nameFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // 보더
			nameFormat.setBackground(Colour.LIGHT_GREEN); //배경
			
			mySheet.setColumnView(0, 20); // 컬럼의 넓이 설정.


			/*** 데이터값 삽입 ***/
			mySheet.addCell(new Label(0, 0, Title, nameFormat)); // 시트의 addCell 메소드를 사용하여 삽입
			
			for (int i = 0; i < DataXnum; i++) 
			{
				mySheet.addCell(new Label(i+1, 1, column[i], numberFormat)); // 시트의 addCell 메소드를 사용하여 삽입
			
				for (int j = 0; j<DataYnum; j++) 
				{           
					mySheet.addCell(new Number(i+1, j+2,Data[i][j])); // 셀에 삽입  인자 : col, row, data, style		
				}
			}

			for (int j = 0; j<DataYnum; j++) 
			{
				mySheet.addCell(new Label(0, j+2, Row[j], numberFormat)); // 셀에 삽입  인자 : col, row, data, style		
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**** 파일 닫기 ****/
	public void FileClose()
	{
		try 
		{
			myWorkbook.write(); // 준비된 정보를 엑셀 포멧에 맞게 작성
			myWorkbook.close(); // 처리 후 메모리에서 해제 처리	
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

