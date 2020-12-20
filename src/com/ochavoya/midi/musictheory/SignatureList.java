package com.ochavoya.midi.musictheory;

import javax.swing.JComboBox;

public class SignatureList extends JComboBox<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SignatureList(){
		addItem("2/4");
		addItem("3/4");
		addItem("4/4");
		addItem("2/8");
		addItem("3/8");
		addItem("4/8");
		addItem("5/8");
		this.setSelectedIndex(2);
	}
	
	public String getSignature(){
		return new String((String)this.getSelectedItem());
	}
}
