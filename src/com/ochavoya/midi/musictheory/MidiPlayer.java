package com.ochavoya.midi.musictheory;

import java.util.PriorityQueue;
import java.util.Queue;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

enum Action {
	noteOn, noteOff;
}

class Event implements Comparable<Event> {
	static Event noteOff(MidiNote note, long timeOut) {

		final Event event = new Event();

		event.note = note;
		event.action = Action.noteOff;
		event.timeOut = timeOut;
		return event;

	}

	static Event noteOn(MidiNote note, long timeOut, int velocity) {

		final long time = System.currentTimeMillis();

		final Event event = new Event();

		event.note = note;
		event.action = Action.noteOn;
		event.timeOut = timeOut < time ? time : timeOut;
		event.velocity = velocity < 0 ? 0 : velocity > MidiPlayer.MAX_VELOCITY ? MidiPlayer.MAX_VELOCITY : velocity;

		return event;
	}

	private MidiNote note;
	private Action action;

	private long timeOut;

	private int velocity;

	private Event() {
	}

	@Override
	public int compareTo(Event event) {

		return (int) (timeOut - event.timeOut);
	}

	public Action getAction() {
		return action;

	}

	public MidiNote getNote() {
		return note;

	}

	public long getTimeOut() {

		return timeOut;
	}

	public int getVelocity() {

		return velocity;
	}

	public boolean timeOver() {

		return timeOut <= System.currentTimeMillis();
	}
}

final public class MidiPlayer {

	public static final int MAX_VELOCITY = 127;
	public static final int MAX_PROGRAM = 127;

	static private MidiPlayer player;

	static String message = "Ok";

	static public MidiPlayer getInstance() {

		if (player == null) {
			return player = new MidiPlayer();
		} else {
			return player;
		}

	}
	private Synthesizer synth;
	private MidiChannel[] channel;

	Queue<Event> queue = new PriorityQueue<Event>();

	Thread mannager = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					while (queue.size() == 0) {
						try {
							queue.wait();
						} catch (final InterruptedException ie) {
							;
						}
					}
				}
				synchronized (queue) {
					final long delay = queue.peek().getTimeOut() - System.currentTimeMillis();
					while (delay > 0) {
						try {
							queue.wait(delay);
						} catch (final InterruptedException ie) {
						}
					}
				}
				synchronized (queue) {
					while (queue.size() > 0 && queue.peek().getTimeOut() - System.currentTimeMillis() <= 0) {
						final Event event = queue.poll();
						final MidiNote note = event.getNote();
						synchronized (channel[note.getMidiChannel()]) {
							switch (event.getAction()) {
							case noteOn:
								channel[note.getMidiChannel()].noteOn(note.getNoteIndex(), event.getVelocity());
								break;
							case noteOff:
								channel[note.getMidiChannel()].noteOff(note.getNoteIndex());
							}
						}
					}
				}
			}
		}
	});

	private MidiPlayer() {
		try {

			synth = MidiSystem.getSynthesizer();
			synth.open();
			channel = synth.getChannels();
			mannager.start();
		} catch (final MidiUnavailableException mu) {
			message = "Midi synthesizer unavailable in this system.";
		}
	}

	public void playChord(MidiNote[] chord, long duration, int velocity) {
		final long start = System.currentTimeMillis();
		int midiChannel;
		for (final MidiNote note : chord) {
			midiChannel = note.getMidiChannel();
			synchronized (channel[midiChannel]) {
				channel[midiChannel].noteOn(note.getNoteIndex(), velocity);
			}
		}
		synchronized (queue) {
			for (final MidiNote note : chord) {
				queue.offer(Event.noteOff(note, start + duration));
			}
		}
	}

	public void playChord(MidiNote[] chord, long start, long duration, int velocity) {
		final long now = System.currentTimeMillis();
		if (now >= start) {
			playChord(chord, duration, velocity);
		} else {
			synchronized (queue) {

				for (final MidiNote note : chord) {
					queue.offer(Event.noteOn(note, start, velocity));
					queue.offer(Event.noteOff(note, start + duration));
				}
			}
		}
	}

	public void playNote(MidiNote note, long duration, int velocity) {
		final long start = System.currentTimeMillis();
		final int midiChannel = note.getMidiChannel();
		synchronized (channel[midiChannel]) {
			channel[midiChannel].noteOn(note.getNoteIndex(), velocity);
		}
		synchronized (queue) {
			queue.offer(Event.noteOff(note, start + duration));
		}
	}

	public void playNote(MidiNote note, long start, long duration, int velocity) {
		final long now = System.currentTimeMillis();
		if (now >= start) {
			playNote(note, duration, velocity);
		} else {
			synchronized (queue) {
				queue.offer(Event.noteOn(note, start, velocity));
				queue.offer(Event.noteOff(note, start + duration));
			}
		}
	}

	public void programChange(int midiChannel, int program) {

		midiChannel = midiChannel < 0 ? 0 : midiChannel >= channel.length ? channel.length - 1 : midiChannel;
		program = program < 0 ? 0 : program > MAX_PROGRAM ? MAX_PROGRAM : program;
		channel[midiChannel].programChange(program);

	}
}
