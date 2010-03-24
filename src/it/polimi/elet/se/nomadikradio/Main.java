package it.polimi.elet.se.nomadikradio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	/** Called when the activity is first created. */
	protected Intent intentToService;
	protected int volume;
	protected long frequency;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		intentToService = new Intent(Main.this, RadioService.class);
		
		//TODO get range of volume and frequency from Radio.
		
		
		getVolumeFromPreferences();
		getFrequencyFromPreferences();
		
		// Watch for button clicks.
		Button button = (Button)findViewById(R.id.turnOnButton);
		button.setOnClickListener(turnOnListener);
		button = (Button)findViewById(R.id.turnOffButton);
		button.setOnClickListener(turnOffListener);
	}

	private void getFrequencyFromPreferences() {
		// TODO Auto-generated method stub
		frequency = 102500;
	}

	private void getVolumeFromPreferences() {
		// TODO Auto-generated method stub
		volume = 0;
	}

	private OnClickListener turnOnListener = new OnClickListener() {
		public void onClick(View v) {
			startService(new Intent(Main.this,
					RadioService.class));
			changeFrequency(frequency);
			changeVolume(volume);
		}
	};

	private OnClickListener turnOffListener = new OnClickListener() {
		public void onClick(View v) {
			stopService(new Intent(Main.this,
					RadioService.class));
		}
	};
	
	private OnClickListener changeFrequencyListener = new OnClickListener() {
		public void onClick(View v) {
			// TODO pick frequency
			long freq = 0;
			
			changeFrequency(freq);
		}
	};

	private OnClickListener changeVolumeListener = new OnClickListener() {
		public void onClick(View v) {
			// TODO pick volume
			int vol = 0;
			
			
			changeVolume(vol);
		}
	};
	
	private void changeVolume(int vol) {
		//create intent
		Intent i = (new Intent(intentToService))
		.setAction(getResources().getString(R.string.change_volume))
		.putExtra("it.polimi.elet.se.nomadikradio.volume", vol);
		//send intent
		startService(i);
	}

	protected void changeFrequency(long freq) {
		//create intent
		Intent i = (new Intent(intentToService))
		.setAction(getResources().getString(R.string.change_frequency))
		.putExtra("it.polimi.elet.se.nomadikradio.frequency", freq);
		//send intent
		startService(i);
	}
}