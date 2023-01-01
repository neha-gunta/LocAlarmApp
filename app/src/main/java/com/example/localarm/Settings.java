package com.example.localarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private SeekBar seekBar;
    private AudioManager audioManager;
    static public float volume=0;
    static public float maxVol=0;
    static public float prevVol=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView tv=(TextView)findViewById(R.id.textView7);
        tv.setText("0");
//        Float givenbatt = Float.parseFloat(result);
//        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context ctxt, Intent intent) {
//                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//                float batteryPct = level * 100 / (float) scale;
//                if(givenbatt<=batteryPct){
//                setAlarm(true);
//                }
//                //textView.setText(String.valueOf(batteryPct) + "%");
//            }
//        };
//        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Button about=findViewById(R.id.button7);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inti=new Intent(Settings.this,About.class);
                startActivity(inti);
            }
        });
        Button vol=findViewById(R.id.button10);
        vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager aud= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                float cv=volume;
                float mv=maxVol;

                int sevvol=(int)cv;
                setPrevVol(cv);
                aud.setStreamVolume(AudioManager.STREAM_MUSIC,sevvol,0);
                seekBar = findViewById(R.id.seekBar);

                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                volume= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                maxVol=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                Log.i("vol",String.valueOf(maxVol));
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        volume=progress*maxVol/100;//0 to 2.56
                        tv.setText(String.valueOf( (int)((volume*100)/2.56)+"%"));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
        Button button = findViewById(R.id.button8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,EmergencyContacts.class);
                startActivity(intent);
            }
        });


    }
    public static float getPrevVol(){
        return prevVol;
    }
    public static void setPrevVol(float v){
        prevVol=(float)v;
        return;
    }




    private void setAlarm(boolean flag) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_MUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, 2000, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
}