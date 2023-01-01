package com.example.localarm.contacts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.localarm.R;
import com.example.localarm.UpdateAlarm;

import java.util.List;

public class AlarmAdapter extends ArrayAdapter<AlarmModel> {

    Context context;
    List<AlarmModel> alarms;
    ListView listView;

    public AlarmAdapter(@NonNull Context context, List<AlarmModel> alarms) {
        super(context, 0, alarms);
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        DbHelper db = new DbHelper(getContext());

        // Get the data item for this position
        AlarmModel a = getItem(position);
        alarms = db.getAllAlarms();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        LinearLayout linearLayout = convertView.findViewById(R.id.linear2);

        // Lookup view for data population
        TextView tvAlarmName = (TextView) convertView.findViewById(R.id.tvAlarmName);
        TextView tvAlarmDesc = (TextView) convertView.findViewById(R.id.tvAlarmDesc);
        Button updateBut = (Button) convertView.findViewById(R.id.updateButton);


        // Populate the data into the template
        // view using the data object
        Log.i("Hello",a.getName());
        tvAlarmName.setText(a.getName());
        tvAlarmDesc.setText(a.getDesc());

        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // a contains the position of alarm
                // call intent to the update page
                Intent intent = new Intent(context, UpdateAlarm.class);
                int alarmId = 552;
                alarmId = a.getId();
                Log.i("update", "update has been called on alarm w id"+alarmId);
                String alarmName = a.getName();
                String alarmDesc = a.getDesc();
                intent.putExtra("alarmId", alarmId);
                intent.putExtra("alarmName", alarmName);
                intent.putExtra("alarmDesc", alarmDesc);
                context.startActivity(intent);
            }
        });

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

//                new MaterialAlertDialogBuilder(getContext())
//                        .setTitle("Remove Contact")
//                        .setMessage("Are you sure want to remove this contact?")
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // delete the specified contact from the database
//                                db.deleteContact(c);
//                                contacts=db.getAllContacts();
//                                // remove the item from the list
//                                contacts.remove(c);
//                                // notify the listview that dataset has been changed
//                                notifyDataSetChanged();
//                                refresh(contacts);
//                                Toast.makeText(getContext(), "Contact removed!", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
//                        .show();
                return false;
            }
        });


        return convertView;
    }

    public void refresh(List<AlarmModel> list) {
        Log.i("Hello","This is refresh");
        alarms.clear();
        alarms.addAll(list);
        notifyDataSetChanged();
    }
}
