package it.polimi.elet.se.nomadikradio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Watch for button clicks.
		Button button = (Button)findViewById(R.id.turnOnButton);
		button.setOnClickListener(turnOnListener);
		button = (Button)findViewById(R.id.turnOffButton);
		button.setOnClickListener(turnOffListener);
	}

	private OnClickListener turnOnListener = new OnClickListener() {
		public void onClick(View v) {
			startService(new Intent(Main.this,
					RadioService.class));
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
			// TODO Auto-generated method stub
			//pick frequency
			//create intent
			//send intent
		}
	};

	private OnClickListener changeVolumeListener = new OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//pick frequency
			//create intent
			//send intent
		}
	};
}