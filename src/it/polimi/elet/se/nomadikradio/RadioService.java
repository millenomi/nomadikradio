package it.polimi.elet.se.nomadikradio;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class RadioService extends Service {
    private static final long defFreq = 0;
	private static final int defVol = 0;
	private NotificationManager mNM;
    private TelephonyManager tm;

    public class LocalBinder extends Binder {
        RadioService getService() {
            return RadioService.this;
        }
    }
    
    @Override
    public void onCreate() {
        Radio.getRadio().setTurnedOn(true);

    	mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//if intent is an ACTIVITY_SERVICE Action is the intent send to start radio.
		if(intent.getAction().equals(ACTIVITY_SERVICE)) return;
		
		StringBuffer s = new StringBuffer();
		
		if(intent.getAction().equals(getResources().getString(R.string.change_frequency))) {
			long f = intent.getLongExtra(getResourceString(R.string.frequency_intent_string), defFreq);
			changeFrequency(f);
			s.append(getResourceString(R.string.frequency_changed));
			s.append(f);			
		} else if(intent.getAction().equals(getResources().getString(R.string.change_volume))) {
			int v = intent.getIntExtra(getResourceString(R.string.volume_intent_string), defVol);
			changeVolume(v);
			s.append(getResourceString(R.string.volume_changed));
			s.append(v);
		}
		Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show();
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				super.onCallStateChanged(state, incomingNumber);
				String s1 = null;
				if(state == TelephonyManager.CALL_STATE_RINGING ||
						state == TelephonyManager.CALL_STATE_OFFHOOK) {
					Radio.getRadio().setTurnedOn(false);
					s1 = getResourceString(R.string.local_service_stopped);
				} else if(state == TelephonyManager.CALL_STATE_IDLE) {
					Radio.getRadio().setTurnedOn(true);
					s1 = getResourceString(R.string.local_service_started);
				}
				Toast.makeText(RadioService.this, s1, Toast.LENGTH_SHORT).show();
			}
		}, MODE_PRIVATE);
	}

	@Override
    public void onDestroy() {
		Radio.getRadio().setTurnedOn(false);
		
    	// Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.stat_sample, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RelativeMain.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.radioLabel),
                       text, contentIntent);
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.local_service_started, notification);
    }
    
    private void changeFrequency(long freq) {
    	try {
			Radio.getRadio().setFrequency(freq);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void changeVolume(int volume) {
    	Radio.getRadio().setVolume(volume);
    }
    
	private String getResourceString (int id) {
		return getResources().getString(id);
	}
}
