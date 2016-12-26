package com.mycompany.sqr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainPanel extends JPanel {

	private JButton retrieveButton = new JButton("Retrieve Data");
	private JTextField symbolInput = new JTextField(10);
	private JScrollPane scrollPane = new JScrollPane();
	private JTable dataTable = new JTable();
	
	public MainPanel() {
		setLayout(new BorderLayout());
		
		JPanel northPane = new JPanel();
		northPane.add(new JLabel("Symbol"));
		northPane.add(symbolInput);
		northPane.add(retrieveButton);
		this.add(northPane, BorderLayout.NORTH);
		
		scrollPane.add(dataTable);
		this.add(scrollPane);
		
		retrieveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onRetrieveButtonClicked();
			}
		});
	}
	
	void onRetrieveButtonClicked() {
		
	}
	
}
