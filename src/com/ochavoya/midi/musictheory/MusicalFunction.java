package com.ochavoya.midi.musictheory;

import javax.swing.JComboBox;

public class MusicalFunction extends JComboBox<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MusicalFunction(){
		addItem("Play Note");
		addItem("Play Major Scale");
		addItem("Play Minor Scale");
		addItem("Play Major Triad");
		addItem("Play Minor Triad");
		addItem("Play Major Seventh");
		addItem("Play Diminished Triad");
		this.setSelectedIndex(0);
	}
	
	public String getMusicalFunction(){
		return new String((String)this.getSelectedItem());
	}

}
