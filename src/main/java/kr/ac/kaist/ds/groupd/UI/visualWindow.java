package kr.ac.kaist.ds.groupd.UI;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Rectangle;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.event.ItemEvent;
public class visualWindow extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	
	private JButton OKButton = null;
	private JCheckBox checkbox = null;
	
	private Choice[] choice;
	int ChoiceCreate;
	private JLabel SystemMessage = null;
	
	String Running= "수행중"; 
	String Waiting = "대기중";  
	String FileWriting = "파일 저장중"; 
	
	XYSeriesCollection xyseriescollection;
	JFreeChart jfreechart;
	ChartPanel chartpanel;
	String Selected;
	
	boolean RandomChoice;
	float[][] m_GraphData;

	int m_Progress;
	
	int m_ChoiceNum = 0;
	
	private static Rectangle OKButtonPos = new Rectangle(510, 510, 73, 28);
	private static Rectangle[] ChoicePos = new Rectangle[]
	                                                     {
															new Rectangle(10, 510, 300, 21),
															new Rectangle(400, 510, 100, 21),
	                                                     };
	private static String[][] ChoiceString = new String[][]{
		                                                      {
		                                                    	 new String("성능평가1 : MD5적용횟수에 따른 F지역 튜플전송 byte"),
		                                                    	 new String("성능평가2 : 총전송byte"),
		                                                    	 new String("성능평가3-1 : R과 N 사이 처리바이트"),
		                                                    	 new String("성능평가3-2 : S과 N 사이 처리바이트"),
		                                                    	 new String("성능평가4 : 통신범위에따른 전송byte"),
		                                                    	 new String("성능평가5 : 메모리용량에 따른 전송bytes"),
		                                                    	 new String("성능평가6 : Area size 에 따른 전송bytes"),
		                                                    	 new String("성능평가7 : 조인선택률에 따른 첫번째아웃풋 출력시간"),
		                                                    	 new String("성능평가8 : Area size에 따른 첫번째아웃풋 출력시간"),
		                                                    	 new String("성능평가9 : R튜플수에따른 전송byte"),
		                                                    	 new String("성능평가10 : S 튜플수에 따른 전송 byte"),
		                                                    	 new String("성능평가11 : Join attribute range"),
		                                                    	 new String("성능평가 12 : Node수 변경에 따른 전송byte"),
		                                                    	 new String("성능평가 13 : 네트워크사이즈에 따른 전송byte")
		                                                     },
		                                                     {new String("노드위치고정"),new String("노드위치랜덤")},  
		                                                   };
		
	private static Rectangle MessagePos = new Rectangle(430, 550, 200, 21);
	private static Rectangle ChckboxPos = new Rectangle(10, 540, 200, 21);
	
	public visualWindow() 
	{
		super();

		initialize();		
	}

	private void initialize() 
	{
		choice = new Choice[2];
		for(int i = 0; i<2; i++)
		{
			choice[i] = null;
		}
		RandomChoice = false;
		Selected = ChoiceString[0][0];
		//===창 사이즈 조절===//
		this.setSize(610, 600);
		this.setMinimumSize(this.getSize());

		this.setContentPane(getJContentPane());
		this.setTitle("SensorNetworkJoin");
		
		// 차트 붙이기 //
		CreateNewChart();  
        //setLayout(new BorderLayout());
        setLayout(null);
       // chartpanel.bounds()
        chartpanel.setSize(600,500);
        add(chartpanel);
		ReflashOutput(0, m_Progress);
	}

	public void ReflashOutput(int _RepeatCount, int Progress) 
	{
		;
		m_Progress = Progress;
		
		if(m_Progress >= 98)
		{
	        // ===그래프 업데이트=== //	   

			this.remove(chartpanel);
			CreateNewChart();  
	        //setLayout(new BorderLayout());
	        setLayout(null);
	        
	        //chartpanel.bounds()
	        chartpanel.setSize(600,500);
	        add(chartpanel);
	        chartpanel.updateUI();
	        // ====================== //
		}
		if(m_Progress==0)
			SystemMessage.setText(Waiting);
		else if(m_Progress==99)
			SystemMessage.setText(FileWriting);
		else if(m_Progress==100)	// 알고리즘 수행완료
		{	        
			SystemMessage.setText(Waiting);			
			m_Progress = 0;
			OKButton.setEnabled(true);
			choice[0].setEnabled(true);
			choice[1].setEnabled(true);
		}
		else
			SystemMessage.setText("총" + 10 + "회 반복중" + 2 +  "회 "+ 20 + "%" + Running);
  
	}

	
	private JPanel getJContentPane() 
	{
		if (jContentPane == null) {
			//===panel생성===//
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			
			//===interface object add===//
			
			SystemMessage = new JLabel();
			SystemMessage.setText("");
			SystemMessage.setBounds(MessagePos);
			SystemMessage.setForeground(new Color(255,0,0));
			
			jContentPane.add(SystemMessage, null);
			for(ChoiceCreate =0; ChoiceCreate<2; ChoiceCreate++)
			{
				jContentPane.add(getChoice(), null);
			}
			jContentPane.add(getOKButton(), null);

		  checkbox = new JCheckBox("SinkNode 전송 제외");
		  checkbox.addItemListener(new java.awt.event.ItemListener() 
			{
				public void itemStateChanged(java.awt.event.ItemEvent e) 
				{
					  int type = e.getStateChange();
				}
			});		
		  checkbox.setBounds(ChckboxPos);
		  jContentPane.add(checkbox);
		}
		return jContentPane;
	}

	/**
	 * This method initializes OKButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOKButton() {
		if (OKButton == null) {
			OKButton = new JButton();
			OKButton.setText("수행");
			OKButton.setBounds(new Rectangle(OKButtonPos));
			OKButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					if(m_Progress == 0)
					{
						// !쓰레드 생성
//						Thread t = new Thread(m_AlgorithmRun);
//						t.start();
//						m_Progress = 1;
//						OKButton.setEnabled(false);
//						choice[0].setEnabled(false);
//						choice[1].setEnabled(false);
//						ReflashOutput(0,m_Progress);
					}
				}
				
			});
		}
		return OKButton;
	}


	/**
	 * This method initializes choice	
	 * 	
	 * @return java.awt.Choice	
	 */
	private Choice getChoice() 
	{
		if (choice[ChoiceCreate] == null) 
		{
			choice[ChoiceCreate] = new Choice();
			choice[ChoiceCreate].setBounds(ChoicePos[ChoiceCreate]);
			for(int i=0; i<ChoiceString[ChoiceCreate].length; i++)
			{
				if(i<12 || RandomChoice)
				choice[ChoiceCreate].add(ChoiceString[ChoiceCreate][i]);
			}
			
			choice[ChoiceCreate].addItemListener(new java.awt.event.ItemListener() 
			{
				public void itemStateChanged(java.awt.event.ItemEvent e) 
				{
					if(e.getItem().toString() == ChoiceString[1][0]  && RandomChoice ==true )
					{
						choice[0].removeAll();

						for(int i=0; i<12; i++)
						{
							choice[0].add(ChoiceString[0][i]);
						}
						RandomChoice = false;
					}
					else if(e.getItem().toString() == ChoiceString[1][1] && RandomChoice ==false)
					{
						choice[0].remove(ChoiceString[0][0]);
						choice[0].add(ChoiceString[0][12]);
						choice[0].add(ChoiceString[0][13]);
						RandomChoice = true;
					}
					Selected = choice[0].getSelectedItem();
					
					m_ChoiceNum = choice[0].getSelectedIndex();

					if(choice[1].getSelectedIndex() == 1)
					{
					}
					else if(choice[1].getSelectedIndex() == 0)
					{			
					}
					
					if(m_ChoiceNum == 0)	//MD5수행
					{
					}

					else	//MD5수행외
					{
					}
						
					ReflashOutput(0, 100);
				
				}
			});			
		}
		return choice[ChoiceCreate];
	}
	

	// ===========================================================//
	// ==						차트부분							==//
	// ===========================================================//

    public ChartPanel CreateNewChart() 
    {    
    	xyseriescollection = null;
    	jfreechart = null;
    	chartpanel = null;
    	
    	String[] DataName = new String[3];
		DataName[0] = "1";
    	DataName[1] = "2";
    	DataName[2] = "3";

    	int[] DataLow = new int[]{ 1,2,3,4,5 };
    	
		switch(m_ChoiceNum)
		{
		
    		default:
    			m_GraphData = new float[3][5];
		}
    	
        
        xyseriescollection = createDataset(DataName, m_GraphData, DataLow );  
  	    jfreechart = createChart(xyseriescollection, "x", "Y", Selected, " "); 
        chartpanel = new ChartPanel(jfreechart); 
        //chartpanel.setPreferredSize(new Dimension(600, 450));       
        
        return chartpanel;
    }
    
    // 데이터셋을 그래프에 추가시킨다.
    private static XYSeriesCollection createDataset( String[] DataName, float data[][], int[] X)
    {
    	XYSeriesCollection xyseriescollection = new XYSeriesCollection(); 

    	for(int i=0; i<DataName.length; i++) // 그래프의수
    	{
    		System.out.println("i = " + i );
    		XYSeries xyseries = new XYSeries(DataName[i], true, true); // 데이터수
	        for(int j = 0;  j<data[i].length; j++) 
	        {
	        	if(X == null) {
	        		xyseries.add((0.0001*(j+1)), data[i][j]); 	
	        		
	        	}
	        	else {
		        	xyseries.add(X[j], data[i][j]); 	
	        		
	        	}
	        }

	        xyseriescollection.addSeries(xyseries); // 그래프에 데이터셋을 추가한다.
	    }
    	
        return xyseriescollection; 
    }

 
    private static JFreeChart createChart(XYDataset xydataset, String Xname, String Yname, String ChartName, String ChartExplain) // 차트 생성
    { 
    	
        JFreeChart jfreechart = ChartFactory.createXYLineChart( 
                null, 
                Xname, 
                Yname, 
                xydataset, 
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false); 
        
        //그래프 위에 타이틀 넣기
        TextTitle texttitle = new TextTitle(ChartName, new Font("SansSerif", 1, 14)); 
        TextTitle texttitle1 = new TextTitle( ChartExplain,  new Font("SansSerif", 0, 11)); 
        
        jfreechart.addSubtitle(texttitle); 
        jfreechart.addSubtitle(texttitle1); 
        
        // 그래프 세부 설정.
        XYPlot xyplot = jfreechart.getXYPlot(); 
        NumberAxis numberaxis = (NumberAxis)xyplot.getDomainAxis(); 
        numberaxis.setUpperMargin(0.12D); 
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
        NumberAxis numberaxis1 = (NumberAxis)xyplot.getRangeAxis(); 
        numberaxis1.setAutoRangeIncludesZero(false); 

        // 그래프안에 글자를 쓰는부분
     /*   XYTextAnnotation xytextannotation = null;         
     	Font font = new Font("SansSerif", 0, 9);         
      	xytextannotation = new XYTextAnnotation("3rd", 36.5D, 11.76D); 
        xytextannotation.setFont(font); 
        xytextannotation.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT); 
        xyplot.addAnnotation(xytextannotation); */
        return jfreechart; 
    }  

} 
