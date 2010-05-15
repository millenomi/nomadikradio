package it.polimi.elet.se.nomadikradio.filters;

import it.polimi.elet.se.nomadikradio.Radio;

public class FrequencyFilter extends RadioFilter {
	private Radio.FrequencyRange fr;
	
	public FrequencyFilter(int intervalNumber, Radio.FrequencyRange frequencyRange) {
		super(intervalNumber);
		fr = frequencyRange;
	}

	public long toRadioFrequency(float userFreq) {
		return (long) (userFreq*intervalNumber);
	}
	
	public float toUserFrequency(long radioFreq) {
		return (float) radioFreq/((float)intervalNumber);
	}

	public long fitFrequencyRange(long freq) {
		if(freq < fr.getMinimum()) freq = fr.getMinimum();
		if(freq > fr.getMaximum()) freq = fr.getMaximum();
		return freq;
	}
}
