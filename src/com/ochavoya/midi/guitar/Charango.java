package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiNote;


public class Charango extends Guitar{

    // Charango tunning: E'',A',E',C'',G' 

	public Charango(){
		super(new MidiNote[]{new MidiNote(0,67),new MidiNote(0,72),
			new MidiNote(0,64),new MidiNote(0,69),new MidiNote(0,76)},18);
			name="Ukulele Soprano";
            charango=true;
	}

	public static void main(String[] args){
        new Charango();
	}
}
