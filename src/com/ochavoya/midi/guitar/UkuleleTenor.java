package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiNote;


public class UkuleleTenor extends Guitar{

    // Ukulele tenor tunning: gc'e'a' 

	public UkuleleTenor(){
		super(new MidiNote[]{new MidiNote(0,67),new MidiNote(0,72),
			new MidiNote(0,76),new MidiNote(0,81)},18);
		name="Ukulele Tenor";
	}

	public static void main(String[] args){
        new UkuleleTenor();
	}
}
