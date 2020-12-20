package com.ochavoya.midi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ochavoya.midi.guitar.Guitar;
import com.ochavoya.midi.musictheory.MidiChord;
import com.ochavoya.midi.musictheory.MidiIndexException;
import com.ochavoya.midi.musictheory.MidiNote;
import com.ochavoya.midi.musictheory.MidiPlayer;
import com.ochavoya.midi.musictheory.MidiScales;
import com.ochavoya.midi.musictheory.MusicalFunction;
import com.ochavoya.midi.musictheory.SignatureList;
import com.ochavoya.midi.musictheory.TempoList;

class FingerPosition implements Comparable<FingerPosition> {

	int string;
	int fret;

	public FingerPosition(int string, int fret) {

		this.string = string;
		this.fret = fret;

	}

	@Override
	public int compareTo(FingerPosition object) {
		if (fret == object.fret) {
			return string - object.string;
		} else {
			return fret - object.fret;
		}
	}

	@Override
	public boolean equals(Object object) {

		if (object instanceof FingerPosition) {

			final FingerPosition position = (FingerPosition) object;

			return string == position.string && fret == position.fret;

		}

		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return string + fret;
	}
}

abstract public class GuitarPlayer extends JFrame {

	private class fretButtonActionListener implements ActionListener {
		int string;
		int fret;

		fretButtonActionListener(int string, int fret) {
			this.string = string;
			this.fret = fret;
		}

		@Override
		public void actionPerformed(ActionEvent evt) {

			emptyQueue();

			switch (musicalFunction.getSelectedIndex()) {
			case 0:
				synchronized (setQueue) {
					programNote(string, fret, beat);
				}
				break;
			case 1:
				if (addThirds) {
					programScale(MidiScales.majorScaleWithThirds(guitar.getNote(string, fret)), string, fret, beat);
				} else {
					programScale(MidiScales.majorScale(guitar.getNote(string, fret)), string, fret, beat);
				}

				break;
			case 2:
				if (addThirds) {
					programScale(MidiScales.minorScaleWithThirds(guitar.getNote(string, fret)), string, fret, beat);
				} else {
					programScale(MidiScales.minorScale(guitar.getNote(string, fret)), string, fret, beat);
				}
				break;
			case 3:
				programChord(MidiChord.majorTriad(guitar.getNote(string, fret)), string, fret, beat);
				break;
			case 4:
				programChord(MidiChord.minorTriad(guitar.getNote(string, fret)), string, fret, beat);
				break;
			case 5:
				final MidiNote[] scale = MidiChord.majorTriad7(guitar.getNote(string, fret));
				if (scale == null) {
					System.out.println("The scale is null");
				} else {
					programChord(scale, string, fret, beat);
				}
				break;
			case 6:
				programChord(MidiChord.diminishedTriad(guitar.getNote(string, fret)), string, fret, beat);
				break;
			case 7:

			}
		}

		private FingerPosition getCloser(String name) {

			final TreeSet<FingerPosition> positionSet = inverseNoteName.get(name);
			if (positionSet == null) {
				return null;
			}

			int minDistance = 100000;
			int distance = 0;
			FingerPosition closer = null;
			for (final FingerPosition position : positionSet) {

				if (!guitar.charango && position.fret <= 1 && fret <= 3) {
					distance = 0;
				} else {
					distance = position.fret - fret < 0 ? 5 * Math.abs(position.fret - fret)
							: 3 * Math.abs(position.fret - fret);
				}
				if (distance < minDistance) {
					minDistance = distance;
					closer = position;
				}
			}
			return closer;
		}

		private FingerPosition getCloserChord(String name) {

			final TreeSet<FingerPosition> positionSet = inverseNoteName.get(name);

			if (positionSet == null) {
				return null;
			}

			int minDistance = 100000;
			int distance = 0;

			FingerPosition closer = null;
			for (final FingerPosition position : positionSet) {
				if (stringUsed[position.string] && !arpegiatedChord) {
					continue;
				}
				if (!guitar.charango && position.fret <= 1 && fret <= 3) {
					distance = 0;
				} else {
					distance = position.fret - fret < 0 ? 5 * Math.abs(position.fret - fret)
							: 3 * Math.abs(position.fret - fret);
				}
				if (distance < minDistance) {
					minDistance = distance;
					closer = position;
				}
			}

			if (closer != null) {
				if ((closer.fret > fret && minDistance > 9) || (closer.fret < fret && minDistance > 15)) {
					return null;
				}
				stringUsed[closer.string] = true;
			}
			return closer;
		}

		private MidiNote[] invertThirdNote(MidiNote[] midiScale) {
			if (midiScale == null) {
				return null;
			}
			final MidiNote temp = midiScale[0];
			try {
				midiScale[0] = midiScale[2].octaveDown();
				if (inverseNoteName.get(midiScale[0].toString()) == null) {
					return null;
				}
				midiScale[2] = midiScale[1];
				midiScale[1] = temp;
			} catch (final MidiIndexException exception) {
				return null;
			}
			return midiScale;
		}

		private void programChord(MidiNote[] midiScale, int string, int fret, int beat) {
			if (midiScale == null) {
				return;
			}
			FingerPosition position = null;
			if (chordOnFifth) {
				midiScale = invertThirdNote(midiScale);
			}
			if (chordOnThird) {
				midiScale = invertThirdNote(invertThirdNote(midiScale));
			}

			if (midiScale == null) {
				return;
			}
			if (!arpegiatedChord) {
				for (int i = 0; i < stringUsed.length; ++i) {
					stringUsed[i] = false;
				}
			}
			synchronized (setQueue) {
				for (final MidiNote note : midiScale) {
					position = getCloserChord(note.toString());
					if (position == null) {
						continue;
					}
					programNote(position.string, position.fret, (arpegiatedChord ? beat++ : beat) % maxBeat);
				}
			}
		}

		private void programNote(int string, int fret, int beat) {

			setQueue.add(new NoteEvent(string, fret, (++beat) % maxBeat));

		}

		private void programScale(MidiNote[] midiScale, int string, int fret, int beat) {
			FingerPosition position = null;
			if (descendingScales) {
				for(int i= midiScale.length-1, j=0; i> j; --i, ++j ) {
					MidiNote temp = midiScale[j];
					midiScale[j] = midiScale[i];
					midiScale[i] = temp;
				}
			}
			synchronized (setQueue) {
				for (final MidiNote note : midiScale) {
					position = getCloser(note.toString());
					if (position == null) {
						continue;
					}

					programNote(position.string, position.fret, (++beat) % maxBeat);
				}
			}
		}
	}
	private class NoteEvent {
		int string;
		int fret;
		int beat;

		NoteEvent(int string, int fret, int beat) {
			this.string = string;
			this.fret = fret;
			this.beat = beat;
		}
	}
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int tempo = 100;
	private long period = 60000 / 100;
	private String signature = "4/4";

	private int beat = 0;
	private int maxBeat = 4;
	private final MidiPlayer player = MidiPlayer.getInstance();
	private final MusicalFunction musicalFunction = new MusicalFunction();
	private final MidiNote tempoNote = new MidiNote(9, 75);
	private final TempoList tempoList = new TempoList();

	private final SignatureList signatureList = new SignatureList();
	private final Guitar guitar;
	private JButton[][] fretButton;
	private boolean playTempo = false;
	boolean playingScales = false;
	boolean addThirds = false;
	boolean descendingScales = false;
	private boolean loop = false;
	private boolean arpegiatedChord = false;
	private boolean chordOnFifth = false;

	private boolean chordOnThird = false;
	private final boolean[] stringUsed;
	private final JCheckBox tempoCheckBox = new JCheckBox("Play Tempo");
	private final JCheckBox loopCheckBox = new JCheckBox("loop");
	private final JCheckBox descendingScalesCheckBox = new JCheckBox("Descending Scales");
	private final JCheckBox addThirdsCheckBox = new JCheckBox("Scales With Thirds");
	private final JCheckBox arpegiatedChordCheckBox = new JCheckBox("Arpegiated Chords");

	private final JCheckBox chordOnFifthCheckBox = new JCheckBox("First Inversion");

	private final JCheckBox chordOnThirdCheckBox = new JCheckBox("Second Inversion");
	private final LinkedList<NoteEvent> setQueue = new LinkedList<NoteEvent>();

	private final LinkedList<NoteEvent> resetQueue = new LinkedList<NoteEvent>();

	private final Thread setQueueWatcher = new Thread(new Runnable() {

		@Override
		public void run() {
			NoteEvent event = null;
			while (true) {
				while (setQueue.size() == 0 || setQueue.getFirst().beat != beat) {
					synchronized (clock) {
						try {
							clock.wait();
						} catch (final InterruptedException exception) {
						}
					}
					synchronized (setQueue) {
						for (final NoteEvent x : setQueue) {
							fretButton[x.string][x.fret].setBackground(x.fret == 0 ? Color.yellow : Color.white);
						}
					}
				}

				final int nextBeat = (setQueue.getLast().beat + 1) % maxBeat;

				while (setQueue.size() != 0 && setQueue.getFirst().beat == beat) {
					synchronized (setQueue) {
						event = setQueue.removeFirst();
					}
					guitar.playNote(event.string, event.fret, period - 8);
					fretButton[event.string][event.fret].setBackground(Color.red);
					if (loop) {
						synchronized (setQueue) {
							event.beat = nextBeat;
							setQueue.add(event);
						}
					} else {
						event.beat = (event.beat + 1) % maxBeat;
						synchronized (resetQueue) {
							resetQueue.add(event);
						}
					}
				}
			}
		}
	});

	private final Thread resetQueueWatcher = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				synchronized (resetQueue) {
					while (resetQueue.size() != 0 && resetQueue.getFirst().beat == beat) {
						final NoteEvent event = resetQueue.removeFirst();
						if (event.fret == 0) {
							fretButton[event.string][event.fret].setBackground(Color.yellow);
						} else {
							fretButton[event.string][event.fret].setBackground(Color.white);
						}
					}
				}
				synchronized (clock) {
					try {
						clock.wait();
					} catch (final InterruptedException exception) {
						;
					}
				}
			}
		}
	});

	{

		descendingScalesCheckBox.setEnabled(false);
		addThirdsCheckBox.setEnabled(false);
		arpegiatedChordCheckBox.setEnabled(false);
		chordOnFifthCheckBox.setEnabled(false);
		chordOnThirdCheckBox.setEnabled(false);

		tempoList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tempo = tempoList.getTempo();
				setPeriod();
			}
		});

		signatureList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				signature = signatureList.getSignature();
				setPeriod();
			}
		});

		tempoCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				playTempo = tempoCheckBox.isSelected();
			}
		});

		loopCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				loop = loopCheckBox.isSelected();
			}
		});

		descendingScalesCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				descendingScales = descendingScalesCheckBox.isSelected();
				loop = false;
				loopCheckBox.setSelected(loop);
			}
		});

		addThirdsCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				addThirds = addThirdsCheckBox.isSelected();
				loop = false;
				loopCheckBox.setSelected(false);
			}
		});

		arpegiatedChordCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				arpegiatedChord = arpegiatedChordCheckBox.isSelected();
				loop = false;
				loopCheckBox.setSelected(false);
			}
		});

		chordOnFifthCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				chordOnFifth = chordOnFifthCheckBox.isSelected();
				if (chordOnFifth) {
					chordOnThird = false;
					chordOnThirdCheckBox.setSelected(false);
				} else {
				}
				loop = false;
				loopCheckBox.setSelected(false);
			}
		});

		chordOnThirdCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				chordOnThird = chordOnFifthCheckBox.isSelected();
				if (chordOnThird) {
					chordOnFifth = false;
					chordOnFifthCheckBox.setSelected(false);
				} else {
				}
				loop = false;
				loopCheckBox.setSelected(false);
			}
		});

		musicalFunction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				switch (musicalFunction.getSelectedIndex()) {
				case 0:
					descendingScalesCheckBox.setEnabled(false);
					addThirdsCheckBox.setEnabled(false);
					arpegiatedChordCheckBox.setEnabled(false);
					chordOnFifthCheckBox.setEnabled(false);
					chordOnThirdCheckBox.setEnabled(false);
					loop = false;
					loopCheckBox.setSelected(false);
					playingScales = false;
					break;
				case 1:
				case 2:
					playingScales = true;
					descendingScalesCheckBox.setEnabled(true);
					addThirdsCheckBox.setEnabled(true);
					arpegiatedChordCheckBox.setEnabled(false);
					chordOnFifthCheckBox.setEnabled(false);
					chordOnThirdCheckBox.setEnabled(false);
					loop = false;
					loopCheckBox.setSelected(false);
					break;
				default:
					playingScales = false;
					descendingScalesCheckBox.setEnabled(false);
					addThirdsCheckBox.setEnabled(false);
					arpegiatedChordCheckBox.setEnabled(true);
					chordOnFifthCheckBox.setEnabled(true);
					chordOnThirdCheckBox.setEnabled(true);
					loop = false;
					loopCheckBox.setSelected(false);
					break;
				}
			}
		});

	}

	private String[][] noteName;

	private Map<String, TreeSet<FingerPosition>> inverseNoteName;

	private final Thread clock = new Thread(new Runnable() {

		@Override
		public void run() {
			long time;
			while (true) {
				synchronized (clock) {
					if (playTempo) {
						switch (beat) {
						case 0:
							player.playNote(tempoNote, period, 100);
							break;
						default:
							player.playNote(tempoNote, period, beat % 2 == 0 ? 80 : 60);
						}
					}
				}
				synchronized (clock) {
					time = System.currentTimeMillis();
					beat = (beat + 1) % maxBeat;
					try {
						time = period - (System.currentTimeMillis() - time);
						if (time > 0) {
							clock.wait(time);
						}
						clock.notifyAll();
					} catch (final InterruptedException exception) {
						;
					}
				}
			}
		}
	});
	GuitarPlayer(Guitar guitar, String name) {

		super(name + (char) 0xa9 + " 2010 Oscar Chavoya Aceves");
		this.guitar = guitar;
		mapGuitar();
		stringUsed = new boolean[guitar.getStringNumber()];
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clock.start();
		resetQueueWatcher.setPriority(Thread.MIN_PRIORITY);
		setQueueWatcher.setPriority(Thread.MAX_PRIORITY);
		setQueueWatcher.start();
		resetQueueWatcher.start();
		setLayout(new BorderLayout());
		final JPanel controlPane = new JPanel();
		final GridLayout controlLayout = new GridLayout(1, 8);
		controlLayout.setHgap(2);
		controlPane.setLayout(controlLayout);
		final JPanel tempoPanel = new JPanel();
		tempoPanel.setLayout(new GridLayout(2, 1));
		tempoPanel.add(new JLabel("Select Tempo"));
		tempoPanel.add(tempoList);
		controlPane.add(tempoPanel);
		final JPanel signaturePanel = new JPanel();
		signaturePanel.setLayout(new GridLayout(2, 1));
		signaturePanel.add(new JLabel("Select Signature"));
		signaturePanel.add(signatureList);
		controlPane.add(signaturePanel);
		controlPane.add(tempoCheckBox);
		final JPanel musicalFunctionPanel = new JPanel();
		musicalFunctionPanel.setLayout(new GridLayout(3, 1));
		musicalFunctionPanel.add(new JLabel("Select a Function"));
		musicalFunctionPanel.add(new JLabel("Click on a Note"));
		musicalFunctionPanel.add(musicalFunction);
		controlPane.add(musicalFunctionPanel);
		controlPane.add(loopCheckBox);
		final JPanel scalePanel = new JPanel();
		scalePanel.setLayout(new GridLayout(2, 1));
		scalePanel.add(descendingScalesCheckBox);
		scalePanel.add(addThirdsCheckBox);
		controlPane.add(scalePanel);
		final JPanel chordPane = new JPanel();
		chordPane.setLayout(new GridLayout(4, 1));
		chordPane.add(new JLabel("Select Chord Modes"));
		chordPane.add(arpegiatedChordCheckBox);
		chordPane.add(chordOnFifthCheckBox);
		chordPane.add(chordOnThirdCheckBox);
		controlPane.add(chordPane);
		final JPanel fretPane = new JPanel();
		fretPane.setLayout(new GridLayout(guitar.getStringNumber(), guitar.getFretNumber()));
		for (int i = 0; i < guitar.getStringNumber(); ++i) {
			for (int j = 0; j < guitar.getFretNumber(); ++j) {
				fretPane.add(fretButton[i][j]);
			}
		}
		add(controlPane, BorderLayout.NORTH);
		add(fretPane, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	private void emptyQueue() {
		NoteEvent event;
		synchronized (setQueue) {
			while (setQueue.size() > 0) {
				event = setQueue.removeFirst();
				if (event.fret == 0) {
					fretButton[event.string][event.fret].setBackground(Color.yellow);
				} else {
					fretButton[event.string][event.fret].setBackground(Color.white);
				}
			}
		}

		synchronized (resetQueue) {
			while (resetQueue.size() > 0) {
				event = resetQueue.removeFirst();
				if (event.fret == 0) {
					fretButton[event.string][event.fret].setBackground(Color.yellow);
				} else {
					fretButton[event.string][event.fret].setBackground(Color.white);
				}
			}
		}
	}

	private void mapGuitar() {

		noteName = new String[guitar.getStringNumber()][guitar.getFretNumber()];
		fretButton = new JButton[guitar.getStringNumber()][guitar.getFretNumber()];
		Font font = null;

		for (int i = 0; i < noteName.length; ++i) {

			for (int j = 0; j < noteName[i].length; ++j) {
				noteName[i][j] = guitar.getNote(i, j).toString();
				fretButton[i][j] = new JButton(noteName[i][j]);
				if (i == 0 && j == 0) {
					font = fretButton[i][j].getFont();
				}
				fretButton[i][j].setFont(new Font(font.getFontName(), font.getStyle(), 10));
				fretButton[i][j].addActionListener(new fretButtonActionListener(i, j));
				if (j > 0) {
					fretButton[i][j].setBackground(Color.white);
				} else {
					fretButton[i][j].setBackground(Color.yellow);
				}
			}
		}

		inverseNoteName = new HashMap<String, TreeSet<FingerPosition>>();

		TreeSet<FingerPosition> fingerPositionSet;

		for (int i = 0; i < guitar.getStringNumber(); ++i) {
			for (int j = 0; j < guitar.getFretNumber(); ++j) {
				fingerPositionSet = inverseNoteName.get(noteName[i][j]);
				if (fingerPositionSet == null) {
					fingerPositionSet = new TreeSet<FingerPosition>();
					fingerPositionSet.add(new FingerPosition(i, j));
					inverseNoteName.put(noteName[i][j], fingerPositionSet);
				} else {
					fingerPositionSet.add(new FingerPosition(i, j));
				}

			}
		}
	}

	private void setPeriod() {

		period = 60000 / tempo;

		if (signature.equals("2/4")) {
			maxBeat = 2;
		}

		if (signature.equals("3/4")) {
			maxBeat = 3;
		}

		if (signature.equals("4/4")) {
			maxBeat = 4;
		}

		if (signature.equals("2/8")) {
			maxBeat = 2;
			period /= 2;

		}

		if (signature.equals("3/8")) {
			maxBeat = 3;
			period /= 2;
		}

		if (signature.equals("4/8")) {
			maxBeat = 4;
			period /= 2;
		}

		if (signature.equals("5/8")) {
			maxBeat = 5;
			period /= 2;
		}
	}
}
