package com.example.localarm.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.localarm.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ContactModel> {

    Context context;
    List<ContactModel> contacts;
    ListView listView;

    public CustomAdapter(@NonNull Context context, List<ContactModel> contacts) {
        super(context, 0, contacts);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        DbHelper db = new DbHelper(getContext());

        // Get the data item for this position
        ContactModel c = getItem(position);
        contacts = db.getAllContacts();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        LinearLayout linearLayout = convertView.findViewById(R.id.linear);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);

        // Populate the data into the template
        // view using the data object
        Log.i("Hello",c.getName());
        tvName.setText(c.getName());
        tvPhone.setText(c.getPhoneNo());

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Remove Contact")
                        .setMessage("Are you sure want to remove this contact?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // delete the specified contact from the database
                                db.deleteContact(c);
                                contacts=db.getAllContacts();
                                // remove the item from the list
                                contacts.remove(c);
                                // notify the listview that dataset has been changed
                                notifyDataSetChanged();
                                refresh(contacts);
                                Toast.makeText(getContext(), "Contact removed!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                return false;
            }
        });

        return convertView;
    }

    public void refresh(List<ContactModel> list) {
        Log.i("Hello","This is refresh");
        contacts.clear();
        contacts.addAll(list);
        notifyDataSetChanged();
    }
}
