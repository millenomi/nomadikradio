package it.polimi.elet.se.nomadikradio;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	
	protected int volume = DEFAULT_VOLUME;
	protected long frequency = DEFAULT_FREQUENCY;
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
		
		try {
			preferences = getSharedPreferences("radioprefs", MODE_PRIVATE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		preferencesEditor = preferences.edit();
		// loads preferences of volume and frequency (last settings!)
		loadPreferences();
		
		// show preferences in view
		EditText edit = (EditText)findViewById(R.id.frequencyText);
		edit.setText(Long.toString(frequency));
		edit = (EditText)findViewById(R.id.volumeText);
		edit.setText(Integer.toString(volume));
		
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

	private void loadPreferences() {
		if(preferences.contains(LAST_FREQUENCY_SETTINGS))
			frequency = preferences.getLong(LAST_FREQUENCY_SETTINGS, DEFAULT_FREQUENCY);
		if(preferences.contains(LAST_VOLUME_SETTINGS))
			volume = preferences.getInt(LAST_VOLUME_SETTINGS, DEFAULT_VOLUME);
	}
	
	private void savePreferences() {
		preferencesEditor.putLong(LAST_FREQUENCY_SETTINGS, frequency);
		preferencesEditor.putInt(LAST_VOLUME_SETTINGS, volume);
		preferencesEditor.commit();
	}

	private OnClickListener turnOnListener = new OnClickListener() {
		public void onClick(View v) {
			startService((new RadioIntent()).setAction(ACTIVITY_SERVICE));
			// set frequency and volume from preferences
			changeFrequency(frequency);
			changeVolume(volume);
			
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
	private Radio.FrequencyRange fr = new Radio.FrequencyRange(80000, 110000);
	
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
			
			// send intent to service
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
			vol = (int) Radio.MaximumVolume*vol/VOLUME_INTERVAL_NUMBER;
			
			// check if volume is in the range
			if(vol < 0) vol = 0;
			if(vol > Radio.MaximumVolume) vol = Radio.MaximumVolume;
			
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
	
	private String getResourceString (int id) {
		return getResources().getString(id);
	}
}