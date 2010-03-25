package it.polimi.elet.se.nomadikradio;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.widget.EditText;

public abstract class AbstractRadioActivity extends Activity {
	protected static final int DEFAULT_VOLUME = 0;
	protected static final long DEFAULT_FREQUENCY = 102500;

	protected static final String LAST_VOLUME_SETTINGS = "LAST_VOLUME_SETTINGS";
	protected static final String LAST_FREQUENCY_SETTINGS = "LAST_FREQUENCY_SETTINGS";
	protected static final String LAYOUT_SETTINGS = "LAYOUT_SETTINGS";

	protected SharedPreferences preferences;
	protected SharedPreferences.Editor preferencesEditor;
	protected VolumeFilter filter;
	//--------- ci˜ che va caricato dalle preferenze ----------
	private int volume;
	private long frequency;
	private int layout;
	//---------------------------------------------------------
	protected boolean radioOn = false;	
	protected Radio.FrequencyRange fr = null;
	protected OnClickListener turnOnListener, turnOffListener,
							changeFrequencyListener, changeVolumeListener;

	
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
		return radioOn;
	}

	public void setRadioOn(boolean radioOn) {
		this.radioOn = radioOn;
	}
	
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
		putPreferences();
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
		putPreferences();
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
			setVolume(preferences.getInt(LAST_VOLUME_SETTINGS, DEFAULT_VOLUME));
	}
	
	/**
	 * Modifica le preferenze localmente. NON esegue il commit delle modifiche,
	 * chiamare esplicitamente {@link commitPreferences()}.
	 */
	protected void putPreferences() {
		preferencesEditor.putLong(LAST_FREQUENCY_SETTINGS, getFrequency());
		preferencesEditor.putInt(LAST_VOLUME_SETTINGS, getVolume());
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
	protected abstract void updateCommandsVisibility();
	/* **********END VIEW*************/
	
	/* *******RADIO CONNECTION**********/
	protected void sincronizeToRadioState() {
		if(Radio.getRadio().isTurnedOn()) {
			setVolume(filter.toUserVolume(Radio.getRadio().getVolume()));
			setFrequency(Radio.getRadio().getFrequency());
		}
	}
	
	protected long fitFrequencyRange(long freq) {
		if(fr==null)
			fr = Radio.getRadio().getFrequencyRange();
		if(freq < fr.getMinimum()) freq = fr.getMinimum();
		if(freq > fr.getMaximum()) freq = fr.getMaximum();
		return freq;
	}

	protected void changeVolume(int vol) {
		//create intent
		Intent i = (new RadioIntent())
		.setAction(getResourceString(R.string.change_volume))
		.putExtra(getResourceString(R.string.volume_intent_string), vol);
		//send intent
		startService(i);
	}

	protected void changeFrequency(long freq) {
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

	public class VolumeFilter {
		private int volumeIntervalNumber;
		
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

		public int fitVolumeRange(int vol) {
			if(vol < 0) vol = 0;
			if(vol > volumeIntervalNumber) vol = volumeIntervalNumber;
			return vol;
		}
	}
	
	public class RadioIntent extends Intent {
		public RadioIntent() {
			super(AbstractRadioActivity.this, RadioService.class);
		}
	}
	/* **********END UTILITY************/
}