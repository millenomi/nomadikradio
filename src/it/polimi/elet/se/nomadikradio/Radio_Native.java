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
	protected static final int InvalidArgument = 3;
	
	public static enum RadioResult {
		OK,
		POSIXError,
		FrequencyOutOfRange,
		InvalidArgument
	}
	
	public static class RadioException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private RadioResult result;
		
		public RadioException(RadioResult result) {
			super(result.toString());
			this.result = result;
		}
		
		public RadioResult result() { return result; }
	}
	
	private native long open();
	private synchronized native void close(long handle);
	
	private native int setNativeTurnedOn(long handle, boolean on);
	private native int setNativeVolume(long handle, int volume);
	private native int setNativeFrequency(long handle, long khz);
	
	private native int getNativeFrequencyRange(long handle, long[] minMax);
	
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
			throw new RadioException(RadioResult.values()[r]);
	}
	
	@Override
	public void setTurnedOn(boolean on) {
		int r = thisOpened().setNativeTurnedOn(self, on);
		if (r != OK)
			throw new RadioException(RadioResult.values()[r]);
	}
	
	@Override
	public void setVolume(int volume) {
		int r = thisOpened().setNativeVolume(self, volume);
		if (r != OK)
			throw new RadioException(RadioResult.values()[r]);
	}
	@Override
	public FrequencyRange getFrequencyRange() {
		long[] minMax = new long[2];
		int r = thisOpened().getNativeFrequencyRange(self, minMax);
		if (r == InvalidArgument)
			throw new IllegalArgumentException();
		if (r != OK) 
			throw new RadioException(RadioResult.values()[r]);
		
		return new FrequencyRange(minMax[0], minMax[1]);
	}
	
	
}
