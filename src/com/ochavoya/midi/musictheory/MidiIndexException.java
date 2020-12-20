package com.ochavoya.midi.musictheory;

public class MidiIndexException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MidiIndexException(){
		super("Invalid midi note index.");
	}
}
