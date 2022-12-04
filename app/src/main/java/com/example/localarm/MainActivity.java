package com.example.localarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.localarm.contacts.AlarmAdapter;
import com.example.localarm.contacts.AlarmModel;
import com.example.localarm.contacts.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    DbHelper db;
    List<AlarmModel> list;
    AlarmAdapter alarmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SOS.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.ListViewAlarms);
        db = new DbHelper(this);
        list = db.getAllAlarms();
        alarmAdapter = new AlarmAdapter(this, list);
        listView.setAdapter(alarmAdapter);

        FloatingActionButton button2 = findViewById(R.id.floating_button_add);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddAlarm.class);
                startActivity(intent);
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
            }
        });


    }
}