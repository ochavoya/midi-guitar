package com.ochavoya.midi;

import com.ochavoya.midi.guitar.ClassicalGuitar;

public class ClassicalGuitarPlayer extends GuitarPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1756349341323210789L;

	public ClassicalGuitarPlayer() {
		super(new ClassicalGuitar(), "Classical Guitar Player ");
	}

	public static void main(String[] arg) {
		new ClassicalGuitarPlayer();
	}
}
