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


		//COPIATO E INCOLLATO DAI SAMPLES!

		// Watch for button clicks.
		Button button = (Button)findViewById(R.id.turnOnButton);
		button.setOnClickListener(mStartListener);
		button = (Button)findViewById(R.id.turnOffButton);
		button.setOnClickListener(mStopListener);
	}

	private OnClickListener mStartListener = new OnClickListener() {
		public void onClick(View v)
		{
			// Make sure the service is started.  It will continue running
			// until someone calls stopService().  The Intent we use to find
			// the service explicitly specifies our service component, because
			// we want it running in our own process and don't want other
			// applications to replace it.
			startService(new Intent(Main.this,
					PACService.class));
		}
	};

	private OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v)
		{
			// Cancel a previous call to startService().  Note that the
			// service will not actually stop at this point if there are
			// still bound clients.
			stopService(new Intent(Main.this,
					PACService.class));
		}
	};

}