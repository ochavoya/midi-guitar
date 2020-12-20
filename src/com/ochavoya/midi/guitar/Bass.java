package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiNote;


public class Bass extends Guitar{
	
	/*
	 * The bass is supposed to have four strings. It starts with MidiNote
	 */

	public Bass(){
		super(new MidiNote[]{new MidiNote(0,28),new MidiNote(0,33), new MidiNote(0,38),new MidiNote(0,43)},19);
		name="Bass";
	}

	public static void main(String[] args){
		new Bass();
	}
}
