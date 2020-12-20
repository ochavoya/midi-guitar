package com.ochavoya.midi;

import com.ochavoya.midi.guitar.Charango;

public class CharangoPlayer extends GuitarPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CharangoPlayer() {
		super(new Charango(), "Charango Player ");
	}

	public static void main(String[] arg) {

		new CharangoPlayer();
	}
}
