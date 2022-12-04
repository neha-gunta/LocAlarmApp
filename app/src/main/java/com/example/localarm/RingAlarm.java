package com.example.localarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class RingAlarm extends AppCompatActivity {

    private final static String default_notification_channel_id = "default";
    private Ringtone ringtoneSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm);




        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager km=(KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            km.requestDismissKeyguard(this,null);
        }
        else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.ringtoneSound = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
        Button button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), MyAlarm.class);
//                stopService(intent);
//                    ringtoneSound.stop();
                Toast.makeText(RingAlarm.this, "Alarm should stop", Toast.LENGTH_SHORT).show();
                ringtoneSound.stop();
                cancelAlarm(true);
            }
        });
    }

private void cancelAlarm(boolean flag)
{
//    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//    Intent i = new Intent(this, MyAlarm.class);
//    PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_MUTABLE);
//    if(am!=null)
//        am.cancel(pi);

    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    Intent intent = new Intent("Myalarm");
    PendingIntent sender = PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
    am.cancel(sender);
    sender.cancel();
    Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_SHORT).show();
}




//    public void off(View view){
//
//        finish();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.i("hello", "onDestroy is called");
//        ringtoneSound.stop();
//    }
}


