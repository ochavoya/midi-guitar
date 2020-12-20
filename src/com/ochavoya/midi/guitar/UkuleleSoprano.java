package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiNote;


public class UkuleleSoprano extends Guitar{

    // Ukulele Soprano tunning: g'c'e'a' 

	public UkuleleSoprano(){
		super(new MidiNote[]{new MidiNote(0,67),new MidiNote(0,60),
				new MidiNote(0,64),new MidiNote(0,69)},18);
			name="Ukulele Soprano";
	}

	public static void main(String[] args){
        new UkuleleSoprano();
	}
}
