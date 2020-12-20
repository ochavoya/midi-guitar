package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiNote;


public class UkuleleBaritone extends Guitar{

    // Ukulele Baritone tunning: dgbe'

	public UkuleleBaritone(){
		super(new MidiNote[]{new MidiNote(0,62),new MidiNote(0,67),
			new MidiNote(0,71),new MidiNote(0,76)},18);
		name="Ukulele Baritone";
	}

	public static void main(String[] args){
        new UkuleleBaritone();
	}
}
