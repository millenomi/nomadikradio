package it.polimi.elet.se.nomadikradio;

// ALL RuntimeExceptions are TODO.

public class Radio_Native extends Radio {
	
	private final static boolean Debugging = true;
	
	static {
		System.loadLibrary(Debugging? "FMRadio_Simulated" : "FMRadio_V4L2");
	}
	
	protected static final int OK = 0;
	protected static final int POSIXError = 1;
	protected static final int FrequencyOutOfRange = 2;
	
	private native long open();
	private synchronized native void close(long handle);
	
	private native int setNativeTurnedOn(long handle, boolean on);
	private native int setNativeVolume(long handle, int volume);
	private native int setNativeFrequency(long handle, long khz);
	
	// ---
	
	public Radio_Native() {}
	
	private long self = 0;
	public synchronized void finalize() {
		close(self);
		self = 0;
	}
		
	private Radio_Native thisOpened() {
		if (self == 0) {
			self = open();
			if (self == 0)
				throw new RuntimeException("Could not open the radio device!");
		}
		
		return this;
	}
	
	@Override
	public void setFrequency(long khz) {
		int r = thisOpened().setNativeFrequency(self, khz);
		if (r == FrequencyOutOfRange)
			throw new IllegalArgumentException("The frequency is out of range.");
		if (r != OK)
			throw new RuntimeException();
	}
	
	@Override
	public void setTurnedOn(boolean on) {
		int r = thisOpened().setNativeTurnedOn(self, on);
		if (r != OK)
			throw new RuntimeException();
	}
	
	@Override
	public void setVolume(int volume) {
		int r = thisOpened().setNativeVolume(self, volume);
		if (r != OK)
			throw new RuntimeException();
	}
	
	
}
