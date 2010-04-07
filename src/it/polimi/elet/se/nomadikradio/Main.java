package it.polimi.elet.se.nomadikradio;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends AbstractRadioActivity {
	private static final int VOLUME_INTERVAL_NUMBER = 16;
	private static final int DEFAULT_LAYOUT = R.layout.main;
	private OnClickListener turnOffListener;
	private OnClickListener turnOnListener;
	private OnClickListener changeFrequencyListener;
	private OnClickListener changeVolumeListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		volumeFilter = new VolumeFilter(VOLUME_INTERVAL_NUMBER);
		setLayout(DEFAULT_LAYOUT);
		
		// loads preferences of volume and frequency (last settings!) and layout.
		loadPreferences();

		// sets the view and initialize listeners and visibility.
		setContentView(getLayout());
		
		changeVolumeListener = new OnClickListener() {
			public void onClick(View v) {
				// pick volume from view
				int vol = Integer.parseInt(getTextFromViewById(R.id.volumeText));
				// change the value of volume to fit range
				vol = volumeFilter.fitVolumeRange(vol);
				setVolume(vol);
				
				changeVolume(volumeFilter.toRadioVolume(vol));
			}
		};
		changeFrequencyListener = new OnClickListener() {
			public void onClick(View v) {
				// pick frequency from view
				long freq = Long.parseLong(getTextFromViewById(R.id.frequencyText));
				// check if frequency is in the range
				freq = fitFrequencyRange(freq);
				setFrequency(freq);
				
				changeFrequency(freq);
			}
		};
		turnOnListener = new OnClickListener() {
			public void onClick(View v) {
				startService((new RadioIntent()).setAction(ACTIVITY_SERVICE));
				// set frequency and volume from preferences
				changeFrequency(getFrequency());
				changeVolume(getVolume());
				setRadioOn(true);
				
				updateCommandsVisibility();
			}
		};
		turnOffListener = new OnClickListener() {
			public void onClick(View v) {
				stopService(new RadioIntent());
				setRadioOn(false);
				
				updateCommandsVisibility();
			}
		};
		
		initView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		commitPreferences();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPreferences();
		updateView();
	}

	@Override
	protected void onStop() {
		super.onStop();
		commitPreferences();
	}

	@Override
	protected void loadPreferences() {
		super.loadPreferences();
		sincronizeToRadioState();

		if(preferences.contains(LAYOUT_SETTINGS)) {
			int newLayout = preferences.getInt(LAYOUT_SETTINGS, DEFAULT_LAYOUT);
			if(getLayout()!=newLayout) throw new IllegalStateException();
		}
	}

	@Override
	protected void updateView() {
		// show preferences in view
		EditText edit = (EditText)findViewById(R.id.frequencyText);
		edit.setText(Long.toString(getFrequency()));
		edit = (EditText)findViewById(R.id.volumeText);
		edit.setText(Integer.toString(getVolume()));
	}

	@Override
	protected void updateCommandsVisibility() {
		boolean v = isRadioOn(); // solo un segnaposto per ricordare come farlo.
		// TODO set visibility of the view of volume and frequency settings
		
	}

	private void initView() {
		updateCommandsVisibility();
		
		// TODO set the label correctly
		// Watch for button clicks.
		Button button = (Button)findViewById(R.id.turnOnButton);
		button.setOnClickListener(turnOnListener);
		button = (Button)findViewById(R.id.turnOffButton);
		button.setOnClickListener(turnOffListener);
		button = (Button)findViewById(R.id.frequencyButton);
		button.setOnClickListener(changeFrequencyListener);
		button = (Button)findViewById(R.id.volumeButton);
		button.setOnClickListener(changeVolumeListener);
	}
}
