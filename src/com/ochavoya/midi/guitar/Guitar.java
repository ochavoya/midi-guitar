package com.ochavoya.midi.guitar;

import com.ochavoya.midi.musictheory.MidiIndexException;
import com.ochavoya.midi.musictheory.MidiNote;
import com.ochavoya.midi.musictheory.MidiPlayer;

abstract public class Guitar {

	private static final int GUITAR_CHANNEL = 24;
	private static final int TUNNING_DURATION = 2000;

	private final MidiNote[][] fingerPosition;
	private final int fretNumber;
	private int velocity = 90;
	private final MidiPlayer player = MidiPlayer.getInstance();
	public boolean charango = false;
	protected String name;

	public Guitar(MidiNote[] root, int fretNumber) {

		this.fretNumber = fretNumber;
		++fretNumber;
		fingerPosition = new MidiNote[root.length][fretNumber];
		for (int i = 0; i < root.length; ++i) {
			root[i].setMidiChannel(i);
			player.programChange(i, GUITAR_CHANNEL);

			for (int j = 0; j < fretNumber; ++j) {
				fingerPosition[i][j] = root[i];
				try {
					root[i] = root[i].minorSecond();
				} catch (final MidiIndexException e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}

	public int getFretNumber() {
		return fretNumber;
	}

	public MidiNote getNote(int i, int j) {
		if (i < 0 || i >= fingerPosition.length || j < 0 || j > fretNumber) {
			return null;
		}
		try {
			return fingerPosition[i][j].unison();
		} catch (final MidiIndexException exception) {
		}
		return null;
	}

	public int getStringNumber() {
		return fingerPosition.length;
	}

	public MidiNote[] getTunning() {
		final MidiNote[] tunning = new MidiNote[fingerPosition.length];
		for (int i = 0; i < tunning.length; ++i) {
			tunning[i] = getNote(i, 0);
		}
		return tunning;
	}

	public int getVelocity() {
		return velocity;
	}

	public void playNote(int i, int j, long duration) {

		if (i < 0 || i > fingerPosition.length || j < 0 || j > fretNumber) {
			return;
		}

		player.playNote(fingerPosition[i][j], duration, velocity);
		if (charango && i == 2) {
			try {
				player.playNote(fingerPosition[i][j].octave(), duration, velocity);
			} catch (final MidiIndexException exception) {
			}
		}

	}

	public void playNote(int i, int j, long time, long duration) {

		if (i < 0 || i > fingerPosition.length || j < 0 || j > fretNumber) {
			return;
		}

		player.playNote(fingerPosition[i][j], time, duration, velocity);

	}

	public void playTunning() {

		long time = System.currentTimeMillis();
		velocity = MidiPlayer.MAX_VELOCITY;
		final long duration = TUNNING_DURATION;

		for (final MidiNote[] element : fingerPosition) {
			player.playNote(element[0], time, duration, velocity);
			time += duration;
		}
	}

	public void setVelocity(int velocity) {

		this.velocity = velocity < 0 ? 0 : velocity > MidiPlayer.MAX_VELOCITY ? MidiPlayer.MAX_VELOCITY : velocity;

	}
}
