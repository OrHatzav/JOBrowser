package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;
import com.google.gson.Gson;

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

                    Gson gson = new Gson();
                    WorkerProfile profile = new WorkerProfile(firstName.getText().toString(), lastName.getText().toString());
                    String json = gson.toJson(profile);

                    String email = extras.getString("email");
                    String password = extras.getString("password");
                    String answer = request.getPosts("{\"NewProfile\": [\""+email+"\", \""+password + "\","+json + ", \"2\"]}", "/CreateProfile");
//                    request.setIsBusiness(false);
//                    request.setClientID(answer.substring(2,answer.length()-1));
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.sign_up_successfully), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CreateWorkerProfile.this, LoginFragment.class);
                    startActivity(i);
                    finish();


//                    Intent i = new Intent(CreateWorkerProfile.this, MainActivity2.class);
//                    i.putExtra("profile", json);
//                    i.putExtra("registeredNow", "worker");
//                    startActivity(i);
//                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.name_required), Toast.LENGTH_SHORT).show();
                }
            }
        });
}
}