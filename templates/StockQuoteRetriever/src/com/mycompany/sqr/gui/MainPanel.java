package com.mycompany.sqr.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class MainPanel extends JPanel {

	private JButton retrieveButton = new JButton("Retrieve Data");
	private JTextField symbolInput = new JTextField(10);
	private JTable dataTable = new JTable();
	
	public MainPanel() {
		setLayout(new BorderLayout());
		
		JPanel northPane = new JPanel();
		northPane.add(new JLabel("Symbol"));
		northPane.add(symbolInput);
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
		
		// retrieve data
		Calendar from = Calendar.getInstance();
        from.set(Calendar.DAY_OF_MONTH, 1);
        from.set(Calendar.MONTH, 0);
        from.set(Calendar.YEAR, 1950);
        Calendar to = Calendar.getInstance();
        to.set(Calendar.DAY_OF_MONTH, 31);
        to.set(Calendar.MONTH, 11);
        to.set(Calendar.YEAR, 2016);
		List<Quote> quotes = YahooFinanceDataRetriever.getInstance().getData("PFE", from, to);
		
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
