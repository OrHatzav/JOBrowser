package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        listView =findViewById(R.id.ListViewID);

        ServerManager request = new ServerManager();
        Bundle extras = getIntent().getExtras();
        String answer = request.getPosts("\"0" + extras.getString("id") + "\"", "/GetProfile");

        Map<String, String> properties = Splitter.on(", ")
                .withKeyValueSeparator(": ")
                .split(answer);
        ArrayList<String> info =  new ArrayList<>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if(!entry.getKey().equals("{'_id")) {
                info.add(entry.getKey().substring(1,entry.getKey().length()-1));
                if(entry.getValue().charAt(entry.getValue().length()-1) == '}')
                {
                    info.add(entry.getValue().substring(1,entry.getValue().length()-2));
                }
                else {
                    info.add(entry.getValue().substring(1, entry.getValue().length() - 1));
                }

            }


        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, info);
        listView.setDivider(null);
        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

    }
}