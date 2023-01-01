package com.example.localarm;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localarm.contacts.AlarmModel;
import com.example.localarm.contacts.DbHelper;

import java.util.List;

public class UpdateAlarm extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;

    ListView listView;
    DbHelper db;
    List<AlarmModel> alarmsList;
    private EditText name_edit, desc_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alarm);

        Intent intent = getIntent();
        int alarmId = intent.getIntExtra("alarmId", 0);
        String alarmName = intent.getStringExtra("alarmName");
        String alarmDesc = intent.getStringExtra("alarmDesc");


        name_edit = (EditText)findViewById(R.id.name_editE);
        desc_edit = (EditText)findViewById(R.id.desc_editE);

        name_edit.setText(alarmName);
        desc_edit.setText(alarmDesc);

        Button button = findViewById(R.id.button5E);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,PICK_CONTACT);
            }
        });
        listView = (ListView) findViewById(R.id.ListViewAlarms);
        db = new DbHelper(this);
        alarmsList = db.getAllAlarms();

        Button addbutton = findViewById(R.id.addbuttonE);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarmName = name_edit.getText().toString();
                String alarmDesc = desc_edit.getText().toString();

                if (alarmName.isEmpty() && alarmDesc.isEmpty()) {
                    Toast.makeText(UpdateAlarm.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.updateAlarm(new AlarmModel(alarmId, alarmName, alarmDesc));

                Toast.makeText(UpdateAlarm.this, "Alarm has been updated.", Toast.LENGTH_SHORT).show();
                name_edit.setText("");
                desc_edit.setText("");

                Intent intent = new Intent(UpdateAlarm.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}