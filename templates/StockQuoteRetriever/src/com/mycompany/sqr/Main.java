package com.mycompany.sqr;

import javax.swing.UIManager;

import com.mycompany.sqr.gui.Gui;

public class Main {

	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		new Gui();
	}
}
