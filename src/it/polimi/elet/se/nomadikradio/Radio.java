package it.polimi.elet.se.nomadikradio;

public abstract class Radio {
	private static Class<? extends Radio> radioClass = Radio_Native.class;
	public static void setRadioClass(Class<? extends Radio> rc) {
		radioClass = rc;
	}
	
	private static Radio instance;
	public static Radio getRadio() {
		if (instance == null) {
			try {
				instance = radioClass.newInstance();
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
		
		return instance;
	}
	
	// ----------------
	
	public abstract void setTurnedOn(boolean on);
	public abstract boolean isTurnedOn();
	
	public static final int Muted = 0;
	public static final int MaximumVolume = 65535;
	
	public abstract void setVolume(int volume);
	public abstract int getVolume();
	
	public abstract void setFrequency(long khz);
	public abstract long getFrequency();
	
	public static class FrequencyRange {
		private long minimum, maximum;

		public long getMinimum() {
			return minimum;
		}

		public long getMaximum() {
			return maximum;
		}

		public FrequencyRange(long minimum, long maximum) {
			super();
			this.minimum = minimum;
			this.maximum = maximum;
		}
	}
	
	public abstract FrequencyRange getFrequencyRange();
}
