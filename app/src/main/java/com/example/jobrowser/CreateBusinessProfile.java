package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
    Bundle extras = getIntent().getExtras();
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

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().length() > 0)
                {
                    String subjects = "{ BusinessName: " + name;

                    String locations = "";
                    if(office.isChecked())
                    {
                        locations += office.getText().toString();
                    }
                    if(lab.isChecked())
                    {
                        if(locations.length()>0) {
                            locations += ", ";
                        }
                        locations += lab;
                    }
                    if(factory.isChecked())
                    {
                        if(locations.length()>0) {
                            locations += ", ";
                        }
                        locations += factory;
                    }
                    if(work_home.isChecked())
                    {
                        if(locations.length()>0) {
                            locations += ", ";
                        }
                        locations += work_home;
                    }

                    subjects += ", locations: {["+ locations + "]}";
                    String email = extras.getString("email");
                    String password = extras.getString("password");
                    String id = request.getPosts("["+email+", "+password + ", "+subjects + ", 1]", "/CreateProfile");

                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.sign_up_successfully), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CreateBusinessProfile.this, UploadPost.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.name_required), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}