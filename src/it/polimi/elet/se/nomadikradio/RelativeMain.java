package it.polimi.elet.se.nomadikradio;

import it.polimi.elet.se.nomadikradio.filters.FrequencyFilter;
import it.polimi.elet.se.nomadikradio.filters.VolumeFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class RelativeMain extends AbstractRadioActivity {
//	private static final int VOLUME_INTERVAL_NUMBER = 16;
	private static final int FREQUENCY_INTERVAL_NUMBER = 1000;
	private static final int DEFAULT_LAYOUT = R.layout.relative;
	private OnClickListener plusFrequencyListener;
	private TextView.OnEditorActionListener frequencyChangedListener;
	private SeekBar.OnSeekBarChangeListener volumeChangedListener;
	private OnClickListener turnOnOffListener;
	private OnClickListener minusFrequencyListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//sets volume, frequency and layout to default values for the fisrt execution 
		setUserVolume(DEFAULT_VOLUME);
		setFrequency(DEFAULT_FREQUENCY);
		setLayout(DEFAULT_LAYOUT);

		// sets the view and initialize listeners and visibility.
		setContentView(getLayout());
		//create the filters for the view - cannot be above the setContentView command
		volumeFilter = new VolumeFilter(((SeekBar)findViewById(R.id.VolumeSeekBar)).getMax(), Radio.MaximumVolume);
		frequencyFilter = new FrequencyFilter(FREQUENCY_INTERVAL_NUMBER,Radio.getRadio().getFrequencyRange());

		// loads preferences of volume and frequency (last settings!) and layout.
		loadPreferences();
	
		frequencyChangedListener = new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==EditorInfo.IME_NULL) {
					EditText text = (EditText)findViewById(R.id.FrequencyText);
					float freq = Float.valueOf(text.getText().toString());
					changeFrequency(frequencyFilter.toRadioFrequency(freq));
					return true;
				}
				return false;
			}
		};
		volumeChangedListener = new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				//do nothing
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				//do nothing
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser) {
					setUserVolume(progress);
					changeVolume(getRadioVolume());
					updateView();
				}
			}
		};
		plusFrequencyListener = new OnClickListener() {
			public void onClick(View v) {
				changeFrequency(getFrequency()+100);
			}
		};
		minusFrequencyListener = new OnClickListener() {
			public void onClick(View v) {
				changeFrequency(getFrequency()-100);
			}
		};
		turnOnOffListener = new OnClickListener() {
			public void onClick(View v) {
				turnRadioOn(!isRadioOn());
			}
		};
		
		initView();
		updateRadioState();
		updateView();
		startMonitoringRadio();
	}

	@Override
	protected void onPause() {
		super.onPause();
		commitPreferences();
		stopMonitoringRadio();
	}

	@Override
	protected void onResume() {
		super.onResume();
		volumeFilter = new VolumeFilter(((SeekBar)findViewById(R.id.VolumeSeekBar)).getMax(), Radio.MaximumVolume);
		loadPreferences();
		updateRadioState();
		updateView();
		startMonitoringRadio();
	}

	@Override
	protected void onStop() {
		super.onStop();
		commitPreferences();
		stopMonitoringRadio();
	}

	@Override
	protected void loadPreferences() {
		super.loadPreferences();
		updateRadioState();

		if(preferences.contains(LAYOUT_SETTINGS)) {
			int newLayout = preferences.getInt(LAYOUT_SETTINGS, DEFAULT_LAYOUT);
			if(getLayout()!=newLayout) throw new IllegalStateException();
		}
	}

	@Override
	protected void updateView() {
		Button b = (Button)findViewById(R.id.OnOffButton);
		if (b != null) {
			if (isRadioOn())
				b.setText(R.string.turnoff);
			else
				b.setText(R.string.turnon);
		}

		// show preferences in view
		EditText edit = (EditText)findViewById(R.id.FrequencyText);
		if (edit != null)
			edit.setText(Float.toString(frequencyFilter.toUserFrequency(getFrequency())));
		
		// set volume slider properly
		SeekBar seek = (SeekBar)findViewById(R.id.VolumeSeekBar);
		if (seek != null)
			seek.setProgress(getUserVolume());
	}

	private void initView() {
		// Watch for button clicks.
		Button button = (Button)findViewById(R.id.OnOffButton);
		button.setOnClickListener(turnOnOffListener);
		button = (Button)findViewById(R.id.PlusFrequency);
		button.setOnClickListener(plusFrequencyListener);
		button = (Button)findViewById(R.id.MinusFrequency);
		button.setOnClickListener(minusFrequencyListener);
		EditText etext = (EditText)findViewById(R.id.FrequencyText);
		etext.setOnEditorActionListener(frequencyChangedListener);
		SeekBar seek = (SeekBar)findViewById(R.id.VolumeSeekBar);
		seek.setOnSeekBarChangeListener(volumeChangedListener);
	}
	
	@Override
	protected void changeFrequency(long freq) {
		setFrequency(frequencyFilter.fitFrequencyRange(freq));
		super.changeFrequency(getFrequency());
		updateView();
	}
}
