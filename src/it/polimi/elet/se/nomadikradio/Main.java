package it.polimi.elet.se.nomadikradio;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity {
	public static final int DEFAULT_VOLUME = 0;
	public static final long DEFAULT_FREQUENCY = 102500;

	private static final String LAST_VOLUME_SETTINGS = "LAST_VOLUME_SETTINGS";
	private static final String LAST_FREQUENCY_SETTINGS = "LAST_FREQUENCY_SETTINGS";
	private static final int VOLUME_INTERVAL_NUMBER = 10;
	private SharedPreferences preferences;
	private SharedPreferences.Editor preferencesEditor;
	private final VolumeFilter filter = new VolumeFilter(VOLUME_INTERVAL_NUMBER);

	private int volume = DEFAULT_VOLUME;
	private long frequency = DEFAULT_FREQUENCY;
	protected boolean isOn = false;
	
	protected class RadioIntent extends Intent {
		public RadioIntent() {
			super(Main.this, RadioService.class);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// loads preferences of volume and frequency (last settings!)
		loadPreferences();
				
		//TODO set not visible the view of volume and frequency settings
		
		// Watch for button clicks.
		Button button = (Button)findViewById(R.id.turnOnButton);
		button.setOnClickListener(turnOnListener);
		button = (Button)findViewById(R.id.turnOffButton);
		button.setOnClickListener(turnOffListener);
		button = (Button)findViewById(R.id.frequencyButton);
		button.setOnClickListener(changeFrequencyListener);
		button = (Button)findViewById(R.id.volumeButton);
		button.setOnClickListener(changeVolumeListener);
		
		savePreferences();
	}

	@Override
	protected void onPause() {
		super.onPause();
		savePreferences();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(Radio.getRadio().isTurnedOn()) {
			setVolume(filter.toUserVolume(Radio.getRadio().getVolume()));
			setFrequency(Radio.getRadio().getFrequency());
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		savePreferences();
	}

	private void loadPreferences() {
		if(preferences == null)
			preferences = getPreferences(MODE_PRIVATE);
		if(preferencesEditor == null)
			preferencesEditor = preferences.edit();
		
		if(preferences.contains(LAST_FREQUENCY_SETTINGS))
			setFrequency(preferences.getLong(LAST_FREQUENCY_SETTINGS, DEFAULT_FREQUENCY));
		if(preferences.contains(LAST_VOLUME_SETTINGS))
			setVolume(preferences.getInt(LAST_VOLUME_SETTINGS, DEFAULT_VOLUME));
		// show preferences in view
		EditText edit = (EditText)findViewById(R.id.frequencyText);
		edit.setText(Long.toString(getFrequency()));
		edit = (EditText)findViewById(R.id.volumeText);
		edit.setText(Integer.toString(getVolume()));
	}
	
	private void savePreferences() {
		preferencesEditor.putLong(LAST_FREQUENCY_SETTINGS, getFrequency());
		preferencesEditor.putInt(LAST_VOLUME_SETTINGS, getVolume());
		preferencesEditor.commit();
	}

	private OnClickListener turnOnListener = new OnClickListener() {
		public void onClick(View v) {
			startService((new RadioIntent()).setAction(ACTIVITY_SERVICE));
			// set frequency and volume from preferences
			changeFrequency(getFrequency());
			changeVolume(getVolume());
			
			//TODO set visible the view of volume and frequency settings
			
			isOn = true;
		}
	};

	private OnClickListener turnOffListener = new OnClickListener() {
		public void onClick(View v) {
			stopService(new RadioIntent());
			
			//TODO set not visible the view of volume and frequency settings
			
			isOn = false;
		}
	};
	
	// set to null to charge these from driver
	private Radio.FrequencyRange fr = null;
	
	private OnClickListener changeFrequencyListener = new OnClickListener() {
		public void onClick(View v) {
			// pick frequency from view
			EditText text = (EditText)findViewById(R.id.frequencyText);
			String t = text.getText().toString();
			long freq = Long.parseLong(t);
			
			// check if frequency is in the range
			if(fr==null)
				fr = Radio.getRadio().getFrequencyRange();
			if(freq < fr.getMinimum()) freq = fr.getMinimum();
			if(freq > fr.getMaximum()) freq = fr.getMaximum();
			
			setFrequency(freq);
			
			changeFrequency(freq);
		}
	};

	private OnClickListener changeVolumeListener = new OnClickListener() {
		public void onClick(View v) {
			// pick volume from view
			EditText text = (EditText)findViewById(R.id.volumeText);
			String t = text.getText().toString();
			int vol = Integer.parseInt(t);
			
			// change the value of volume to fit range
			vol = filter.toRadioVolume(vol);
			
			// check if volume is in the range
			if(vol < 0) vol = 0;
			if(vol > Radio.MaximumVolume) vol = Radio.MaximumVolume;
			
			setVolume(filter.toUserVolume(vol));
			
			changeVolume(vol);
		}
	};
	
	private void changeVolume(int vol) {
		//create intent
		Intent i = (new RadioIntent())
		.setAction(getResourceString(R.string.change_volume))
		.putExtra(getResourceString(R.string.volume_intent_string), vol);
		//send intent
		startService(i);
	}

	private void changeFrequency(long freq) {
		//create intent
		Intent i = (new RadioIntent())
		.setAction(getResourceString(R.string.change_frequency))
		.putExtra(getResourceString(R.string.frequency_intent_string), freq);
		//send intent
		startService(i);
	}
	
	private String getResourceString(int id) {
		return getResources().getString(id);
	}
	
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
		savePreferences();
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
		savePreferences();
	}

	private class VolumeFilter {
		private int volumeIntervalNumber = 10;
		
		public VolumeFilter(int volumeIntervalNumber) {
			super();
			this.volumeIntervalNumber = volumeIntervalNumber;
		}

		public int toRadioVolume(int userVolume) {
			return (int) Radio.MaximumVolume*userVolume/volumeIntervalNumber;
		}
		
		public int toUserVolume(int radioVolume) {
			return (int) volumeIntervalNumber*radioVolume/Radio.MaximumVolume;
		}
	}
}