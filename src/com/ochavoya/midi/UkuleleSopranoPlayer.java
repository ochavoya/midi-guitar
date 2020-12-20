package com.ochavoya.midi;
import com.ochavoya.midi.guitar.UkuleleSoprano;

public class UkuleleSopranoPlayer extends GuitarPlayer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UkuleleSopranoPlayer(){
		super(new UkuleleSoprano(),"Ukulele Soprano ");
	}

	public static  void main(String[] arg){

		new UkuleleSopranoPlayer();
	}
}
