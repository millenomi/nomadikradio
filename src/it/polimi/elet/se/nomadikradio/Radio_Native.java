package it.polimi.elet.se.nomadikradio;

// ALL RuntimeExceptions are TODO.

public class Radio_Native extends Radio {
	
	private final static boolean Debugging = true;
	
	static {
		System.loadLibrary(Debugging? "FMRadio_Simulated" : "FMRadio_V4L2");
	}
	
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
	
	private native int getNativeTurnedOn(long handle, boolean[] on);
	private native int getNativeVolume(long handle, int[] volume);
	private native int getNativeFrequency(long handle, long[] khz);
	
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
	
	private void handleReturnValue(int r) {
		if (r == RadioResult.InvalidArgument.ordinal())
			throw new IllegalArgumentException();
		if (r == RadioResult.FrequencyOutOfRange.ordinal())
			throw new IllegalArgumentException("Frequency is out of range.");
		if (r != RadioResult.OK.ordinal()) 
			throw new RadioException(RadioResult.values()[r]);
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
		handleReturnValue(thisOpened().setNativeFrequency(self, khz));
	}
	
	@Override
	public void setTurnedOn(boolean on) {
		handleReturnValue(thisOpened().setNativeTurnedOn(self, on));
		
	}
	
	@Override
	public void setVolume(int volume) {
		handleReturnValue(thisOpened().setNativeVolume(self, volume));
	}
	
	@Override
	public FrequencyRange getFrequencyRange() {
		long[] minMax = new long[2];
		handleReturnValue(thisOpened().getNativeFrequencyRange(self, minMax));
		
		return new FrequencyRange(minMax[0], minMax[1]);
	}
	
	@Override
	public long getFrequency() {
		long[] r = new long[1];
		handleReturnValue(thisOpened().getNativeFrequency(self, r));
		return r[0];
	}
	@Override
	public int getVolume() {
		int[] r = new int[1];
		handleReturnValue(thisOpened().getNativeVolume(self, r));
		return r[0];
	}
	@Override
	public boolean isTurnedOn() {
		boolean[] r = new boolean[1];
		handleReturnValue(thisOpened().getNativeTurnedOn(self, r));
		return r[0];
	}
}
