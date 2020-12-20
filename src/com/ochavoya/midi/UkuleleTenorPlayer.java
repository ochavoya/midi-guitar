package com.ochavoya.midi;
import com.ochavoya.midi.guitar.UkuleleTenor;

public class UkuleleTenorPlayer extends GuitarPlayer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UkuleleTenorPlayer(){
		super(new UkuleleTenor(),"Ukulele Tenor ");
	}

	public static void main(String[] arg){

		new UkuleleTenorPlayer();
	}
}
