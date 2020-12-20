package com.ochavoya.midi;

import com.ochavoya.midi.guitar.Bass;

public class BassPlayer extends GuitarPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BassPlayer() {

		super(new Bass(), "Bass Player ");

	}

	public static void main(String[] arg) {

		new BassPlayer();

	}

}
