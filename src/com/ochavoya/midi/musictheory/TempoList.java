package com.ochavoya.midi.musictheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;


public class TempoList extends JComboBox<String>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxLength=0;
	
	private List<String> tempo=new ArrayList<String>();	
	{
			tempo.addAll(Arrays.asList(new String[]{
					"Largo 40",
					"Largo 42",
					"Largo 44",
					"Largo 46",
					"Largo 48",
					"Largo 50",
					"52",
					"Largetto 54",
					"Largetto 56",
					"Largetto 58",
					"Largetto 60",
					"63",
					"Adagio 69",
					"Adagio 72",
					"Adagio 76",
					"Andante 80",
					"Andante 84",
					"Andante 88",
					"Andante 92",
					"Andante 96",
					"Andante 100",
					"Moderato 104",
					"Moderato 108",
					"Moderato 112",
					"Moderato 116",
					"Moderato 120",
					"Allegro 132",
					"Allegro 138",
					"Allegro 144",
					"Allegro 152",
					"Allegro 160",
					"168",
					"Prestissimo 176",
					"Prestissimo 184",
					"Prestissimo 192",
					"Prestissimo 200"
				}
			)
		);
	}
		
	public TempoList(){		
		for(String x:tempo){
			this.addItem(x);
			maxLength=Math.max(x.length(),maxLength);
			}
		this.setSelectedIndex(tempo.indexOf("Andante 100"));
		tempo=null;
	}

	public int getBeatLength(){
		return (int)60000/getTempo();
	}
	
	
	public String getTempoName(){
		return (String)this.getSelectedItem();
	}
		
	public int getTempo(){
		int tempo=120;
		String[] tokens;
		tokens = getTempoName().split(" ");
	
		for(String token:tokens){
		    try{
		        tempo=Integer.parseInt(token);
		        return tempo;
		    }
                    catch(NumberFormatException nfe){
		    }
		assert false:"You should check your tempo list items";
		}
		return tempo;
	}
	
	public int getMaxLength(){
	    return maxLength;
	    }
}
