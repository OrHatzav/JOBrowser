package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;

import java.util.Map;

public class CreateWorkerProfile extends AppCompatActivity {

    private Button create;
    private EditText firstName;
    private EditText lastName;
    ServerManager request = new ServerManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_worker_profile);
        create = findViewById(R.id.create_profile);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);

        Bundle extras = getIntent().getExtras();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstName.getText().toString().length() > 0 && lastName.getText().toString().length() > 0) {
                    String subjects = "{\"FirstName\": " + "\""+firstName.getText().toString() + "\", \"LastName\": " + "\""+lastName.getText().toString() + "\"}";
                    String email = extras.getString("email");
                    String password = extras.getString("password");
                    String answer = request.getPosts("{\"NewProfile\": [\""+email+"\", \""+password + "\","+subjects + ", \"2\"]}", "/CreateProfile");
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

                    Intent i = new Intent(CreateWorkerProfile.this, ProfilePage.class);
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