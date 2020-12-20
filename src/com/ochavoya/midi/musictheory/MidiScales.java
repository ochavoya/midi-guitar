package com.ochavoya.midi.musictheory;


public class MidiScales{

	static public MidiNote[] majorScale(MidiNote root){
		MidiNote[] scale = new MidiNote[8];
		try{
			scale[0]=root.unison();
			scale[1]=root.majorSecond();
			scale[2]=root.majorThird();
			scale[3]=root.fourth();
			scale[4]=root.fifth();
			scale[5]=root.majorSixth();
			scale[6]=root.seventh();
			scale[7]=root.octave();
		}catch(MidiIndexException exception){
			return null;
		}
		return scale;
	}

	static public MidiNote[] majorScaleWithThirds(MidiNote root){
		MidiNote[] scale = new MidiNote[16];
		try{
			scale[0]=root.unison();
			scale[2]=root.majorSecond();
			scale[4]=root.majorThird();
			scale[6]=root.fourth();
			scale[8]=root.fifth();
			scale[10]=root.majorSixth();
			scale[12]=root.seventh();
			scale[14]=root.octave();
            scale[1]=scale[4];
			scale[3]=scale[6];
			scale[5]=scale[8];
			scale[7]=scale[10];
			scale[9]=scale[12];
			scale[11]=scale[14];
			scale[13]=scale[14].majorSecond();
			scale[15]=scale[14].majorThird();

		}catch(MidiIndexException exception){
			return null;
		}
		return scale;
	}




	static public MidiNote[] minorScale(MidiNote root){
		MidiNote[] scale = new MidiNote[8];
		try{
			scale[0]=root.unison();
			scale[1]=root.majorSecond();
			scale[2]=root.minorThird();
			scale[3]=root.fourth();
			scale[4]=root.fifth();
			scale[5]=root.minorSixth();
			scale[6]=root.diminishedSeventh();
			scale[7]=root.octave();
		}catch(MidiIndexException exception){
			return null;
		}
		return scale;
	}

	static public MidiNote[] minorScaleWithThirds(MidiNote root){
		MidiNote[] scale = new MidiNote[16];
		try{
			scale[0]=root.unison();
			scale[2]=root.majorSecond();
			scale[4]=root.minorThird();
			scale[6]=root.fourth();
			scale[8]=root.fifth();
			scale[10]=root.minorSixth();
			scale[12]=root.diminishedSeventh();
			scale[14]=root.octave();
			scale[1]=scale[4];
			scale[3]=scale[6];
			scale[5]=scale[8];
			scale[7]=scale[10];
			scale[9]=scale[12];
			scale[11]=scale[14];
			scale[13]=scale[14].majorSecond();
			scale[15]=scale[14].minorThird();

		}catch(MidiIndexException exception){
			return null;
		}
		return scale;
	}
	
}
