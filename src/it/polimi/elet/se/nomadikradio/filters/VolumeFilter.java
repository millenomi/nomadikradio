package it.polimi.elet.se.nomadikradio.filters;

public class VolumeFilter extends RadioFilter {
	private int maximumVolume;
	
	public VolumeFilter(int intervalNumber, int maximumVolume) {
		super(intervalNumber);
		this.maximumVolume = maximumVolume;
	}

	public int toRadioVolume(int userVolume) {
		return (int) maximumVolume*userVolume/intervalNumber;
	}
	
	public int toUserVolume(int radioVolume) {
		return (int) intervalNumber*radioVolume/maximumVolume;
	}

	public int fitVolumeRange(int vol) {
		if(vol < 0) vol = 0;
		if(vol > intervalNumber) vol = intervalNumber;
		return vol;
	}
}
