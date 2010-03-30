package it.polimi.elet.se.nomadikradio;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RelativeMain extends AbstractRadioActivity {
	private static final int VOLUME_INTERVAL_NUMBER = 16;
	private static final int DEFAULT_LAYOUT = R.layout.relative;
	private OnClickListener plusFrequencyListener;
	private TextWatcher frequencyChangedListener;
	private OnClickListener turnOnOffListener;
	private OnClickListener minusFrequencyListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		filter = new VolumeFilter(VOLUME_INTERVAL_NUMBER);
		setLayout(DEFAULT_LAYOUT);
		
		// loads preferences of volume and frequency (last settings!) and layout.
		loadPreferences();

		// sets the view and initialize listeners and visibility.
		setContentView(getLayout());
		
//		changeVolumeListener = new OnClickListener() {
//			public void onClick(View v) {
//				// pick volume from view
//				int vol = Integer.parseInt(getTextFromViewById(R.id.volumeText));
//				// change the value of volume to fit range
//				vol = filter.fitVolumeRange(vol);
//				setVolume(vol);
//				
//				changeVolume(filter.toRadioVolume(vol));
//			}
//		};
		plusFrequencyListener = new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		};
		minusFrequencyListener = new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		};
		frequencyChangedListener = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
//		changeFrequencyListener = new OnClickListener() {
//			public void onClick(View v) {
//				// pick frequency from view
//				long freq = Long.parseLong(getTextFromViewById(R.id.frequencyText));
//				// check if frequency is in the range
//				freq = fitFrequencyRange(freq);
//				setFrequency(freq);
//				
//				changeFrequency(freq);
//			}
//		};
		turnOnOffListener = new OnClickListener() {
			public void onClick(View v) {
				Button b = (Button)findViewById(R.id.OnOffButton);
				if(!radioOn) {
					startService((new RadioIntent()).setAction(ACTIVITY_SERVICE));
					// set frequency and volume from preferences
					changeFrequency(getFrequency());
					changeVolume(getVolume());
					setRadioOn(true);
					
					b.setText(R.string.on);
					updateCommandsVisibility();
				} else {
					stopService(new RadioIntent());
					setRadioOn(false);
					
					b.setText(R.string.off);
					updateCommandsVisibility();
				}
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
		EditText edit = (EditText)findViewById(R.id.FrequencyText);
		edit.setText(Float.toString(getFrequency()/1000));
		
		//TODO set volume slider properly
//		edit = (EditText)findViewById(R.id.volumeText);
//		edit.setText(Integer.toString(getVolume()));
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
		Button button = (Button)findViewById(R.id.OnOffButton);
		button.setOnClickListener(turnOnOffListener);
		button = (Button)findViewById(R.id.PlusFrequency);
		button.setOnClickListener(plusFrequencyListener);
		button = (Button)findViewById(R.id.MinusFrequency);
		button.setOnClickListener(minusFrequencyListener);
		EditText etext = (EditText)findViewById(R.id.FrequencyText);
		button.addTextChangedListener(frequencyChangedListener);
	}
}
