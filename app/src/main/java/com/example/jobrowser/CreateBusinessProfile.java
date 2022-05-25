package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;

import java.util.Map;

public class CreateBusinessProfile extends AppCompatActivity {


//    business_name
//    logo - imgur.com
//    office/factory/lab/work from home - listview + locations of each place
//    description
//    contact me - listview
//    ***posts list

    private Button create;
    private EditText name;
    private CheckBox office;
    private CheckBox lab;
    private CheckBox factory;
    private CheckBox work_home;
    private EditText description;
    ServerManager request = new ServerManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_business_profile);
        create = findViewById(R.id.create_profile);
        name = findViewById(R.id.name);
        office = findViewById(R.id.office);
        lab = findViewById(R.id.lab);
        factory = findViewById(R.id.factory);
        work_home = findViewById(R.id.work_home);
        description = findViewById(R.id.description);

        Bundle extras = getIntent().getExtras();


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().length() > 0)
                {
                    String subjects = "{\"BusinessName\": " + "\""+name.getText().toString() + "\"";

                    String locations = "";
                    if(office.isChecked())
                    {
                        locations += "\""+ office.getText().toString()+ "\"";
                    }
                    if(lab.isChecked())
                    {
                        if(locations.length()>0) {
                            locations += ", ";
                        }
                        locations += "\""+ lab.getText().toString() + "\"";
                    }
                    if(factory.isChecked())
                    {
                        if(locations.length()>0) {
                            locations += ", ";
                        }
                        locations += "\""+ factory.getText().toString()+ "\"";
                    }
                    if(work_home.isChecked())
                    {
                        if(locations.length()>0) {
                            locations += ", ";
                        }
                        locations += "\""+work_home.getText().toString() + "\"";
                    }

                    subjects += ", \"locations\": ["+ locations + "]";
                    subjects += ", \"description\": \"" + description.getText().toString() + "\"}";
                    String email = extras.getString("email");
                    String password = extras.getString("password");
                    String answer = request.getPosts("{\"NewProfile\": [\""+email+"\", \""+password + "\","+subjects + ", \"1\"]}", "/CreateProfile");
                    request.setIsBusiness(true);
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.sign_up_successfully), Toast.LENGTH_SHORT).show();


                    Map<String, String> properties = Splitter.on(", ")
                            .withKeyValueSeparator(": ")
                            .split(answer);

                    for (Map.Entry<String, String> entry : properties.entrySet()) {
                        if(entry.getKey().equals("_id"))
                        {
                            request.setClientID(answer.substring(2,answer.length()-1));
                        }
                    }
                    Log.d("hh", answer);
                    Intent i = new Intent(CreateBusinessProfile.this, ProfilePage.class);
                    i.putExtra("profile", answer);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.name_required), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}