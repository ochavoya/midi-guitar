package com.ochavoya.midi.musictheory;


public class MidiChord{

	public static MidiNote[] minorTriad(MidiNote root){
		MidiNote[] chord = new MidiNote[3];
		try{
			chord[0]=root.unison();
			chord[1]=root.minorThird();
			chord[2]=root.fifth();
			}
		catch(MidiIndexException exception){
			return null;
		}
		return chord;
	}
	
	public static MidiNote[] majorTriad(MidiNote root){
		MidiNote[] chord = new MidiNote[3];
		try{
			chord[0]=root.unison();
			chord[1]=root.majorThird();
			chord[2]=root.fifth();
			}
		catch(MidiIndexException exception){
			return null;
		}
		return chord;
	}
	
	public static MidiNote[] diminishedTriad(MidiNote root){
		MidiNote[] chord = new MidiNote[3];
		try{
			chord[0]=root.unison();
			chord[1]=root.minorThird();
			chord[2]=root.triTone();
			}
		catch(MidiIndexException exception){
			return null;
		}
		return chord;
	}
	
	public static MidiNote[] minorTriad7(MidiNote root){

		MidiNote[] chord = new MidiNote[4];
		try{
			chord[0]=root.unison();
			chord[1]=root.minorThird();
			chord[2]=root.fifth();
			chord[3]=root.diminishedSeventh();
			}
		catch(MidiIndexException exception){
			return null;
		}
		return chord;
	}

	public static MidiNote[] majorTriad7(MidiNote root){
		MidiNote[] chord = new MidiNote[4];
		try{
			chord[0]=root.unison();
			chord[1]=root.majorThird();
			chord[2]=root.fifth();
			chord[3]=root.diminishedSeventh();	
			}
		catch(MidiIndexException exception){
			return null;
		}
		return chord;
	}
}
