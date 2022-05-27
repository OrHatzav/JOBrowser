package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

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
                    String checkBox = "";
                    if(office.isChecked())
                    {
                       checkBox += "1";
                    }
                    if(lab.isChecked())
                    {
                        checkBox += "2";
                    }
                    if(factory.isChecked())
                    {
                        checkBox += "3";
                    }
                    if(work_home.isChecked())
                    {
                        checkBox += "4";
                    }
//TODO: subjects cannot be sent properly to server
                    Gson gson = new Gson();
                    BusinessProfile profile = new BusinessProfile(name.getText().toString(), office.isChecked(), lab.isChecked(),
                            factory.isChecked(), work_home.isChecked(), description.getText().toString());
                    String json = gson.toJson(profile);
//                    Profile json = gson.fromJson("", (Type) Profile.class);

                    String email = extras.getString("email");
                    String password = extras.getString("password");
                    String answer = request.getPosts("{\"NewProfile\": [\""+email+"\", \""+password + "\","+json + ", \"1\"]}", "/CreateProfile");
                    request.setIsBusiness(true);
                    request.setClientID(answer.substring(2,answer.length()-1));
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.sign_up_successfully), Toast.LENGTH_SHORT).show();
//TODO: pass profile to profile fragment
                    Intent i = new Intent(CreateBusinessProfile.this, MainActivity.class);
//                    i.putExtra("profile", answer);
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