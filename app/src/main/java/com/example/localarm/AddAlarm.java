package com.example.localarm;

        import androidx.activity.result.ActivityResult;
        import androidx.activity.result.ActivityResultCallback;
        import androidx.activity.result.ActivityResultLauncher;
        import androidx.activity.result.contract.ActivityResultContracts;
        import androidx.appcompat.app.AppCompatActivity;

        import android.app.Activity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.Bundle;
        import android.preference.EditTextPreference;
        import android.provider.ContactsContract;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.example.localarm.contacts.AlarmAdapter;
        import com.example.localarm.contacts.AlarmModel;
        import com.example.localarm.contacts.ContactModel;
        import com.example.localarm.contacts.CustomAdapter;
        import com.example.localarm.contacts.DbHelper;

        import org.jetbrains.annotations.Nullable;

        import java.util.List;

public class AddAlarm extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;

    ListView listView;
    DbHelper db;
    List<AlarmModel> list;
    private EditText name_edit, desc_edit;

    Intent intent = getIntent();

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    int result = activityResult.getResultCode();
                    Intent data = activityResult.getData();
                    if (result == Activity.RESULT_OK) {
                        Uri contactData = data.getData();
                        Cursor c = managedQuery(contactData, null, null, null, null);
                        if (c.moveToFirst()) {

                            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                            String hasPhone = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            String phone = null;
                            try {
                                if (hasPhone.equalsIgnoreCase("1")) {
                                    Log.i("hello", "hey udi udi udi1");
                                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                    Log.i("hello", "hey udi udi udi");
                                    phones.moveToFirst();
                                    phone = phones.getString(phones.getColumnIndexOrThrow("data1"));
                                }
                                String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                                Log.i("hello", name + " " + phone + " naa bondha bro");
                                db.addcontact(new ContactModel(0, name, phone, 1));

                            } catch (Exception ex) {
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        name_edit = (EditText)findViewById(R.id.name_edit);
        desc_edit = (EditText)findViewById(R.id.desc_edit);

        Button button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        Button button2 = findViewById(R.id.button6);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAlarm.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.ListViewAlarms);
        db = new DbHelper(this);
        list = db.getAllAlarms();

        Button addbutton = findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarmName = name_edit.getText().toString();
                String alarmDesc = desc_edit.getText().toString();

                if (alarmName.isEmpty() && alarmDesc.isEmpty()) {
                    Toast.makeText(AddAlarm.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.addAlarm(new AlarmModel(alarmName, alarmDesc));

                Toast.makeText(AddAlarm.this, "Alarm has been added.", Toast.LENGTH_SHORT).show();
                name_edit.setText("");
                desc_edit.setText("");

                Intent intent = new Intent(AddAlarm.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}