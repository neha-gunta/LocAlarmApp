//package com.example.localarm;
//
//import android.os.Bundle;
//import android.widget.ListView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.localarm.contacts.AlarmAdapter;
//import com.example.localarm.contacts.AlarmModel;
//import com.example.localarm.contacts.DbHelper;
//
//import java.util.List;
//
//public class Alarms extends AppCompatActivity {
//
//
//    ListView listView;
//    DbHelper db;
//    List<AlarmModel> list;
//    AlarmAdapter alarmAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_alarms);
//
//
//        listView = (ListView) findViewById(R.id.ListViewAlarms);
//        db = new DbHelper(this);
//        list = db.getAllAlarms();
//        alarmAdapter = new AlarmAdapter(this, list);
//        listView.setAdapter(alarmAdapter);
//    }
//}