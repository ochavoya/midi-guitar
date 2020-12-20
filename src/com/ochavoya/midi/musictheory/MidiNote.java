package com.ochavoya.midi.musictheory;

import java.util.Objects;

public class MidiNote implements Cloneable,Comparable<MidiNote> {

	static private String[] preFix ={"C","C#/Db","D","D#/Eb","E","F",
			"F#/Gb","G","G#/Ab","A","A#/Bb","B"};

	private int noteIndex;
	private int midiChannel;


	public MidiNote(int midiChannel,int noteIndex){
		this.midiChannel = midiChannel<0?0:midiChannel>15?15:midiChannel;
		this.noteIndex = noteIndex<0? 0:noteIndex>127?127:noteIndex;
	}

	@Override
	public int compareTo(MidiNote object){
		final MidiNote note = object;
		return noteIndex-note.noteIndex;
	}

	public MidiNote diminishedSeventh() throws MidiIndexException {
		return transpose( 10 );
	}

	public MidiNote diminishedSeventhDown() throws MidiIndexException {
		return transpose( -10 );
	}

	@Override
	public boolean equals(Object object){
		return Objects.equals(this, object);
	}

	public MidiNote fifth() throws MidiIndexException {
		return transpose( 7 );
	}

	public MidiNote fifthDown() throws MidiIndexException {
		return transpose( -7 );
	}

	public MidiNote fourth() throws MidiIndexException {
		return transpose( 5 );
	}

	public MidiNote fourthDown() throws MidiIndexException {
		return transpose( -5 );
	}

	public int getMidiChannel(){
		return midiChannel;
	}

	public int getMidiIndex(){
		return noteIndex;
	}

	public int getNoteIndex(){
		return noteIndex;
	}

	public int getOctave(){
		return noteIndex/12;
	}

	// Ascending intervals

	@Override
	public int hashCode(){
		return noteIndex;
	}

	public MidiNote majorSecond() throws MidiIndexException {
		return transpose( 2 );
	}

	public MidiNote majorSecondDown() throws MidiIndexException {
		return transpose( -2 );
	}

	public MidiNote majorSixth() throws MidiIndexException {
		return transpose( 9 );
	}

	public MidiNote majorSixthDown() throws MidiIndexException {
		return transpose( -9 );
	}

	public MidiNote majorThird() throws MidiIndexException {
		return transpose( 4 );
	}

	public MidiNote majorThirdDown() throws MidiIndexException {
		return transpose( -4 );
	}

	public MidiNote minorSecond() throws MidiIndexException {
		return transpose( 1 );
	}

	public MidiNote minorSecondDown() throws MidiIndexException {
		return transpose( -1 );
	}

	public MidiNote minorSixth() throws MidiIndexException {
		return transpose( 8 );
	}

	public MidiNote minorSixthDown() throws MidiIndexException {
		return transpose( -8 );
	}

	public MidiNote minorThird() throws MidiIndexException {
		return transpose( 3 );
	}

	//Descending intervals

	public MidiNote minorThirdDown() throws MidiIndexException {
		return transpose( -3 );
	}

	public MidiNote octave() throws MidiIndexException {
		return transpose( 12 );
	}

	public MidiNote octaveDown() throws MidiIndexException {
		return transpose( -12 );
	}

	public void setMidiChannel(int midiChannel){
		this.midiChannel = midiChannel < 0 ? 0 : midiChannel > 15 ? 15 : midiChannel;
	}

	public void setNoteIndex(int noteIndex){
		this.noteIndex= noteIndex < 0 ? 0 : noteIndex > 127 ? 127 : noteIndex ;
	}

	public MidiNote seventh() throws MidiIndexException {
		return transpose( 11 );
	}

	public MidiNote seventhDown() throws MidiIndexException {
		return transpose( -11 );
	}

	@Override
	public String toString(){
		final String name=preFix[noteIndex%12]+getOctave();
		return name;
	}

	public MidiNote transpose(int n) throws MidiIndexException {
		MidiNote note=null;
		try{
			note = (MidiNote)(this.clone());
		}catch(final CloneNotSupportedException exception){
			System.out.println(exception.getMessage());
		}
		final int index = noteIndex+n;
		if(index>127 || index<0 ){
			throw new MidiIndexException();
		}
		note.noteIndex=index;
		return note;
	}

	public MidiNote triTone() throws MidiIndexException {
		return transpose( 6 );
	}

	public MidiNote triToneDown() throws MidiIndexException {
		return transpose( -6 );
	}

	public MidiNote unison() throws MidiIndexException {
		return transpose(0);
	}
}
