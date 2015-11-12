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
	
	String Running= "������"; 
	String Waiting = "�����";  
	String FileWriting = "���� ������"; 
	
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
		                                                    	 new String("������1 : MD5����Ƚ���� ���� F���� Ʃ������ byte"),
		                                                    	 new String("������2 : ������byte"),
		                                                    	 new String("������3-1 : R�� N ���� ó������Ʈ"),
		                                                    	 new String("������3-2 : S�� N ���� ó������Ʈ"),
		                                                    	 new String("������4 : ��Ź��������� ����byte"),
		                                                    	 new String("������5 : �޸𸮿뷮�� ���� ����bytes"),
		                                                    	 new String("������6 : Area size �� ���� ����bytes"),
		                                                    	 new String("������7 : ���μ��÷��� ���� ù��°�ƿ�ǲ ��½ð�"),
		                                                    	 new String("������8 : Area size�� ���� ù��°�ƿ�ǲ ��½ð�"),
		                                                    	 new String("������9 : RƩ�ü������� ����byte"),
		                                                    	 new String("������10 : S Ʃ�ü��� ���� ���� byte"),
		                                                    	 new String("������11 : Join attribute range"),
		                                                    	 new String("������ 12 : Node�� ���濡 ���� ����byte"),
		                                                    	 new String("������ 13 : ��Ʈ��ũ����� ���� ����byte")
		                                                     },
		                                                     {new String("�����ġ����"),new String("�����ġ����")},  
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
		//===â ������ ����===//
		this.setSize(610, 600);
		this.setMinimumSize(this.getSize());

		this.setContentPane(getJContentPane());
		this.setTitle("SensorNetworkJoin");
		
		// ��Ʈ ���̱� //
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
	        // ===�׷��� ������Ʈ=== //	   

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
		else if(m_Progress==100)	// �˰��� ����Ϸ�
		{	        
			SystemMessage.setText(Waiting);			
			m_Progress = 0;
			OKButton.setEnabled(true);
			choice[0].setEnabled(true);
			choice[1].setEnabled(true);
		}
		else
			SystemMessage.setText("��" + 10 + "ȸ �ݺ���" + 2 +  "ȸ "+ 20 + "%" + Running);
  
	}

	
	private JPanel getJContentPane() 
	{
		if (jContentPane == null) {
			//===panel����===//
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

		  checkbox = new JCheckBox("SinkNode ���� ����");
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
			OKButton.setText("����");
			OKButton.setBounds(new Rectangle(OKButtonPos));
			OKButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) 
				{
					if(m_Progress == 0)
					{
						// !������ ����
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
					
					if(m_ChoiceNum == 0)	//MD5����
					{
					}

					else	//MD5�����
					{
					}
						
					ReflashOutput(0, 100);
				
				}
			});			
		}
		return choice[ChoiceCreate];
	}
	

	// ===========================================================//
	// ==						��Ʈ�κ�							==//
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
    
    // �����ͼ��� �׷����� �߰���Ų��.
    private static XYSeriesCollection createDataset( String[] DataName, float data[][], int[] X)
    {
    	XYSeriesCollection xyseriescollection = new XYSeriesCollection(); 

    	for(int i=0; i<DataName.length; i++) // �׷����Ǽ�
    	{
    		System.out.println("i = " + i );
    		XYSeries xyseries = new XYSeries(DataName[i], true, true); // �����ͼ�
	        for(int j = 0;  j<data[i].length; j++) 
	        {
	        	if(X == null) {
	        		xyseries.add((0.0001*(j+1)), data[i][j]); 	
	        		
	        	}
	        	else {
		        	xyseries.add(X[j], data[i][j]); 	
	        		
	        	}
	        }

	        xyseriescollection.addSeries(xyseries); // �׷����� �����ͼ��� �߰��Ѵ�.
	    }
    	
        return xyseriescollection; 
    }

 
    private static JFreeChart createChart(XYDataset xydataset, String Xname, String Yname, String ChartName, String ChartExplain) // ��Ʈ ����
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
        
        //�׷��� ���� Ÿ��Ʋ �ֱ�
        TextTitle texttitle = new TextTitle(ChartName, new Font("SansSerif", 1, 14)); 
        TextTitle texttitle1 = new TextTitle( ChartExplain,  new Font("SansSerif", 0, 11)); 
        
        jfreechart.addSubtitle(texttitle); 
        jfreechart.addSubtitle(texttitle1); 
        
        // �׷��� ���� ����.
        XYPlot xyplot = jfreechart.getXYPlot(); 
        NumberAxis numberaxis = (NumberAxis)xyplot.getDomainAxis(); 
        numberaxis.setUpperMargin(0.12D); 
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
        NumberAxis numberaxis1 = (NumberAxis)xyplot.getRangeAxis(); 
        numberaxis1.setAutoRangeIncludesZero(false); 

        // �׷����ȿ� ���ڸ� ���ºκ�
     /*   XYTextAnnotation xytextannotation = null;         
     	Font font = new Font("SansSerif", 0, 9);         
      	xytextannotation = new XYTextAnnotation("3rd", 36.5D, 11.76D); 
        xytextannotation.setFont(font); 
        xytextannotation.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT); 
        xyplot.addAnnotation(xytextannotation); */
        return jfreechart; 
    }  

} 
