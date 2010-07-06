package it.polimi.elet.se.nomadikradio;

import it.polimi.elet.se.nomadikradio.filters.FrequencyFilter;
import it.polimi.elet.se.nomadikradio.filters.VolumeFilter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

public abstract class AbstractRadioActivity extends Activity {
	public static final String LOG_TAG = "FMRadio [UI]";
	
	protected static final int DEFAULT_VOLUME = 0;
	protected static final long DEFAULT_FREQUENCY = 102500;

	protected static final String LAST_VOLUME_SETTINGS = "LAST_VOLUME_SETTINGS";
	protected static final String LAST_FREQUENCY_SETTINGS = "LAST_FREQUENCY_SETTINGS";
	protected static final String LAYOUT_SETTINGS = "LAYOUT_SETTINGS";

	protected SharedPreferences preferences;
	protected SharedPreferences.Editor preferencesEditor;
	protected VolumeFilter volumeFilter;
	protected FrequencyFilter frequencyFilter;
	//--------- ci˜ che va caricato dalle preferenze ----------
	private int volume;
	private long frequency;
	private int layout;
	//---------------------------------------------------------
	protected boolean radioOn = false;	
	protected Radio.FrequencyRange fr = null;
	
	public AbstractRadioActivity() {
		super();
	}

	/* **********GETTERS & SETTERS***********/
	public int getLayout() {
		return layout;
	}

	protected void setLayout(int layout) {
		this.layout = layout;
	}

	public boolean isRadioOn() {
		return Radio.getRadio().isTurnedOn();
	}

	public void setRadioOn(boolean radioOn) {
		Radio.getRadio().setTurnedOn(radioOn);
		updateView();
	}
	
	public int getUserVolume() {
		return volume;
	}
	
	public int getRadioVolume() {
		return volumeFilter.toRadioVolume(volume);
	}

	public void setUserVolume(int volume) {
		this.volume = volume;
		putPreferences();
		updateView();
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
		putPreferences();
		updateView();
	}
	/* *******END GETTERS & SETTERS**********/

	/* ************PREFERENCES************/
	/**
	 * Carica dalle preferenze ci˜ che serve per l'applicazione.
	 * Non aggiorna la UI, chiamare esplicitamente {@link updateView()}.
	 */
	protected void loadPreferences() {
		// carica il file delle preferenze e, se non esiste, lo crea.
		if(preferences == null)
			preferences = getPreferences(MODE_PRIVATE);
		if(preferencesEditor == null)
			preferencesEditor = preferences.edit();
		
		// carica dalle preferenze ci˜ che serve.
		if(preferences.contains(LAST_FREQUENCY_SETTINGS))
			setFrequency(preferences.getLong(LAST_FREQUENCY_SETTINGS, DEFAULT_FREQUENCY));
		if(preferences.contains(LAST_VOLUME_SETTINGS))
			setUserVolume(preferences.getInt(LAST_VOLUME_SETTINGS, DEFAULT_VOLUME));
	}
	
	/**
	 * Modifica le preferenze localmente. NON esegue il commit delle modifiche,
	 * chiamare esplicitamente {@link commitPreferences()}.
	 */
	protected void putPreferences() {
		if(preferences==null) return;
		preferencesEditor.putLong(LAST_FREQUENCY_SETTINGS, getFrequency());
		preferencesEditor.putInt(LAST_VOLUME_SETTINGS, getUserVolume());
	}

	/**
	 * Esegue il commit delle preferenze.
	 */
	protected boolean commitPreferences() {
		return preferencesEditor.commit();
	}
	/* ********END PREFERENCES************/

	/* *************VIEW**************/
	protected abstract void updateView();
	/* **********END VIEW*************/
	
	/* *******RADIO CONNECTION**********/
	protected void turnRadioOn(boolean on) {
		if (on) {
			startService((new RadioIntent()).setAction(ACTIVITY_SERVICE));
			// set frequency and volume from preferences
			setRadioOn(true); // it can't be moved after if-else statement (using Radio.getRadio().isTurnedOn()) because its absence causes a recursive call
			changeFrequency(getFrequency());
			changeVolume(getRadioVolume());
		} else {
			stopService(new RadioIntent());
			setRadioOn(false);
		}
		updateView();
	}
	
	protected void updateRadioState() {
		if (Radio.getRadio().isTurnedOn()) {
			setUserVolume(volumeFilter.toUserVolume(Radio.getRadio().getVolume()));
			setFrequency(Radio.getRadio().getFrequency());
		}
	}
	
	protected long fitFrequencyRange(long freq) {
		if (fr == null)
			fr = Radio.getRadio().getFrequencyRange();
		
		return fr.clamp(freq);
	}

	protected void changeVolume(int radioVolume) {
		if(!isRadioOn()) turnRadioOn(true);
		//create intent
		Intent i = (new RadioIntent())
			.setAction(getResourceString(R.string.change_volume))
			.putExtra(getResourceString(R.string.volume_intent_string), radioVolume);
		//send intent
		startService(i);
	}

	protected void changeFrequency(long freq) {
		if(!isRadioOn()) turnRadioOn(true);
		//create intent
		Intent i = (new RadioIntent())
			.setAction(getResourceString(R.string.change_frequency))
			.putExtra(getResourceString(R.string.frequency_intent_string), freq);
		//send intent
		startService(i);
	}
	/* *****END RADIO CONNECTION********/

	/* **********UTILITY****************/
	protected String getResourceString(int id) {
		return getResources().getString(id);
	}
	
	protected String getTextFromViewById(int id) {
		EditText text = (EditText)findViewById(id);
		return text.getText().toString();
	}
	
	public class RadioIntent extends Intent {
		public RadioIntent() {
			super(AbstractRadioActivity.this, RadioService.class);
		}
	}
	/* **********END UTILITY************/
	
	private Radio.RadioEvents radioEvents = new Radio.RadioEvents() {
		
		private void update() {
			updateRadioState();
			updateView();
		}

		
		public void radioDidChangeTurnedOnState(Radio r) {
			Log.d(LOG_TAG, "Radio on/off state changed, updating.");
			update();
		}
	};
	
	private long radioMonitoringUses = 0;
	
	protected void startMonitoringRadio() {
		if (radioMonitoringUses == 0) {
			updateView();
			Radio.getRadio().addRadioEventsObserver(radioEvents);
			Log.d(LOG_TAG, "Did start monitoring radio.");
		}
		
		radioMonitoringUses++;
		Log.d(LOG_TAG, "Did increment radio monitoring count.");
	}
	
	protected void stopMonitoringRadio() {
		radioMonitoringUses--;
		Log.d(LOG_TAG, "Did decrement radio monitoring count.");
		
		if (radioMonitoringUses == 0) {
			Radio.getRadio().removeRadioEventsObserver(radioEvents);
			Log.d(LOG_TAG, "Did stop monitoring radio.");
		}
	}
}