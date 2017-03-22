package com.mycompany.sqr.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException; //22.03.17/TSC neu
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties; //22.03.17/TSC neu
import java.util.Date; //22.03.17/TSC neu

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.mycompany.sqr.dr.YahooFinanceDataRetriever;
import com.mycompany.sqr.model.Quote;
import com.mycompany.sqr.tsc.DateLabelFormatter; //22.03.17/TSC neu

import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;

public class MainPanel extends JPanel {

	private JButton retrieveButton = new JButton("Retrieve Data");
	private JTextField symbolInput = new JTextField(10);
	private UtilDateModel dateModel; //22.03.17/TSC neu
	private JDatePanelImpl datePanel; //22.03.17/TSC neu
	private JDatePickerImpl datePicker; //22.03.17/TSC neu
	private UtilDateModel dateModel2; //22.03.17/TSC neu
	private JDatePanelImpl datePanel2; //22.03.17/TSC neu
	private JDatePickerImpl datePicker2; //22.03.17/TSC neu
	
	private JTable dataTable = new JTable();
	
	public MainPanel() {
		setLayout(new BorderLayout());
		
		JPanel northPane = new JPanel();
		northPane.add(new JLabel("Symbol"));
		northPane.add(symbolInput);
		////////////////////////// /22.03.17/TSC neu
		symbolInput.setText("AAPL");

		Properties p = new Properties(); 
		p.put("text.today", "Today"); 
		p.put("text.month", "Month"); 
		p.put("text.year", "Year"); 

		dateModel = new UtilDateModel();
		datePanel = new JDatePanelImpl(dateModel, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter()); 
		datePicker.setBounds(120, 370, 120, 30); 
		datePicker.getModel().setDate(2017, 2, 15);
		datePicker.getModel().setSelected(true);
		northPane.add(new JLabel("from"));
		northPane.add(datePicker); 
		
		dateModel2 = new UtilDateModel(); 
		datePanel2 = new JDatePanelImpl(dateModel2, p); 
		datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter()); 
		datePicker2.setBounds(120, 370, 120, 30); 
		datePicker2.getModel().setDate(2017, 2, 22);
		datePicker2.getModel().setSelected(true);
		northPane.add(new JLabel("to"));
		northPane.add(datePicker2); 
		//22.03.17/TSC neu //////////////////////////

		northPane.add(retrieveButton);

		this.add(northPane, BorderLayout.NORTH);
		
		this.add(new JScrollPane(dataTable));
		
		retrieveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onRetrieveButtonClicked();
			}
		});
		
		dataTable.setModel(createTableModel(new ArrayList<Quote>()));
	}
	
	private void onRetrieveButtonClicked() {
		System.out.println("Button Clicked");
		///////////////////////// //22.0317/TSC neu
		Date startdate = (Date) datePicker.getModel().getValue(); 
		Date enddate = (Date) datePicker2.getModel().getValue();
		System.out.println(startdate);
		System.out.println(enddate);
		System.out.println(startdate.compareTo(enddate));
	
		if (startdate.compareTo(enddate) > 0) {
			startdate.setDate(enddate.getDate() );
			datePicker.getModel().setDate( datePicker2.getModel().getYear(), datePicker2.getModel().getMonth(), datePicker2.getModel().getDay() );
			datePicker.getModel().setSelected(true);			
		}
		//22.0317/TSC neu /////////////////////////
			// retrieve data
		Calendar from = Calendar.getInstance();
        //from.set(Calendar.DAY_OF_MONTH, 1);
        //from.set(Calendar.MONTH, 0);
        //from.set(Calendar.YEAR, 1950);
        from.setTime(startdate); //22.03.17/TSC
        Calendar to = Calendar.getInstance();
        //to.set(Calendar.DAY_OF_MONTH, 31);
        //to.set(Calendar.MONTH, 11);
        //to.set(Calendar.YEAR, 2016);
        to.setTime(enddate);
		List<Quote> quotes = YahooFinanceDataRetriever.getInstance().getData(symbolInput.getText(), from, to); //22.03.17/TSC angepasst

		// fill in data to table
		dataTable.setModel(createTableModel(quotes));
	}
	
	private AbstractTableModel createTableModel(List<Quote> quotes) {
		String [] header = {"Symbol", "Date", "Open", "Close", "Low", "High", "Volume"};
		
		Object [][] data = new Object[quotes.size()][];
		
		for (int i=0; i<quotes.size(); i++) {
			data[i] = new Object[header.length];
			data[i][0] = quotes.get(i).getSymbol();
			data[i][1] = quotes.get(i).getDate();
			data[i][2] = quotes.get(i).getOpen();
			data[i][3] = quotes.get(i).getClose();
			data[i][4] = quotes.get(i).getLow();
			data[i][5] = quotes.get(i).getHigh();
			data[i][6] = quotes.get(i).getVolume();
		}
		
		DefaultTableModel result = new DefaultTableModel(data, header);
		return result;
	}
}
