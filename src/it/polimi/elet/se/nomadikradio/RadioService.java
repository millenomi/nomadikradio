package it.polimi.elet.se.nomadikradio;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.widget.RemoteViews.ActionException;

public class RadioService extends Service {
    private NotificationManager mNM;
    

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
                new Intent(this, Main.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.radioLabel),
                       text, contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.local_service_started, notification);
    }
    
    private void changeFrequency(long freq) {
    	Radio.getRadio().setFrequency(freq);
    }
    
    private void changeVolume(int volume) {
    	Radio.getRadio().setVolume(volume);
    }
}
