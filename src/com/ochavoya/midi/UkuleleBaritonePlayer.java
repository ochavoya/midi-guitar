package com.ochavoya.midi;
import com.ochavoya.midi.guitar.UkuleleBaritone;

public  class UkuleleBaritonePlayer extends GuitarPlayer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UkuleleBaritonePlayer(){
		super(new UkuleleBaritone(),"Ukulele Baritone ");
	}

	public static  void main(String[] arg){

		new UkuleleBaritonePlayer();
	}
}
