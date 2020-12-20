package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiNote;


public class ClassicalGuitar extends Guitar{    

	public ClassicalGuitar(){
		super(new MidiNote[]{new MidiNote(0,40),new MidiNote(0,45),
			new MidiNote(0,50),new MidiNote(0,55),new MidiNote(0,59),
			new MidiNote(0,64)},19);
		name="Classical Guitar";
	}

	public static void main(String[] args){
		 new ClassicalGuitar();
	}
}
