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


/****** ���� ���� ������ ���� Ŭ���� ******/

public class SaveFile 
{
	WritableWorkbook myWorkbook;

	public static final int MaxSheet = 16;	// �ִ� ��Ʈ��

	/**** ������ ****/
	public SaveFile(String FileName)
	{
		try 
		{
			// ���ϻ���
			myWorkbook = Workbook.createWorkbook(new File(FileName)); 	// �����̸��� ���Ͽ� �����Ѵ�.
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**** ��Ʈ���� �� ������ Write ****/
	public void WriteData(String SheetName, String Title, int SheetIndex, String column[] , String Row[] , float Data[][], int DataXnum, int DataYnum )
	{
		
		try {
			/*** ��Ʈ���� ***/
			WritableSheet mySheet = myWorkbook.createSheet(SheetName, SheetIndex);  	
			WritableCellFormat numberFormat = new WritableCellFormat(); // ��ȣ ��
			WritableCellFormat nameFormat = new WritableCellFormat(); // �̸� �� ����
			//WritableCellFormat dataFormat = new WritableCellFormat(); // ������ ��
			

			/*** ���� ***/
			numberFormat.setAlignment(Alignment.CENTRE); // �� ���� ����
			numberFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // �� ���� ����
			numberFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // ����
			numberFormat.setBackground(Colour.GREY_25_PERCENT); //���

			nameFormat.setAlignment(Alignment.LEFT); // �� ���� ����
			nameFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // �� ���� ����
			nameFormat.setBorder(Border.ALL, BorderLineStyle.THICK); // ����
			nameFormat.setBackground(Colour.LIGHT_GREEN); //���
			
			mySheet.setColumnView(0, 20); // �÷��� ���� ����.


			/*** �����Ͱ� ���� ***/
			mySheet.addCell(new Label(0, 0, Title, nameFormat)); // ��Ʈ�� addCell �޼ҵ带 ����Ͽ� ����
			
			for (int i = 0; i < DataXnum; i++) 
			{
				mySheet.addCell(new Label(i+1, 1, column[i], numberFormat)); // ��Ʈ�� addCell �޼ҵ带 ����Ͽ� ����
			
				for (int j = 0; j<DataYnum; j++) 
				{           
					mySheet.addCell(new Number(i+1, j+2,Data[i][j])); // ���� ����  ���� : col, row, data, style		
				}
			}

			for (int j = 0; j<DataYnum; j++) 
			{
				mySheet.addCell(new Label(0, j+2, Row[j], numberFormat)); // ���� ����  ���� : col, row, data, style		
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**** ���� �ݱ� ****/
	public void FileClose()
	{
		try 
		{
			myWorkbook.write(); // �غ�� ������ ���� ���信 �°� �ۼ�
			myWorkbook.close(); // ó�� �� �޸𸮿��� ���� ó��	
		} 
		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

