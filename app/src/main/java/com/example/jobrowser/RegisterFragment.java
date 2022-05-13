package com.example.jobrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RegisterFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button register = (Button) view.findViewById(R.id.btn_register);
        RadioButton worker = (RadioButton) view.findViewById(R.id.worker);
        RadioButton business = (RadioButton) view.findViewById(R.id.business);
        EditText email = (EditText) view.findViewById(R.id.email);
        EditText password = (EditText) view.findViewById(R.id.password);
        EditText repassword = (EditText) view.findViewById(R.id.repassword);
        TextView notMatch = (TextView) view.findViewById(R.id.not_match);

//        register.setVisibility(View.INVISIBLE);
        final Boolean[] isClickable = new Boolean[4];
        Arrays.fill(isClickable, false);
        ServerManager request = new ServerManager();
        SignUp signUp = new SignUp();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(repassword.getText().toString()))
                {
                    if(password.getText().toString().length()>= 6)
                    {
                        if(worker.isChecked() || business.isChecked())
                        {
                            String answer = request.getPosts(email.getText().toString(),"/EmailExists");
                            if(answer == "True")
                            {
                                Toast.makeText(getContext(), getContext().getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                signUp.moveToCreateProfile(email.getText().toString(), password.getText().toString(), worker.isChecked());
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), getContext().getString(R.string.user_type_not_checked), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), getContext().getString(R.string.password_6_letters), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    notMatch.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
    public static void ToastMemoryShort (Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        return;
    }


//
//    private RequestBody buildRequestBody(String msg) {
//        postBodyString = msg;
//        mediaType = MediaType.parse("text/plain");
//        requestBody = RequestBody.create(postBodyString, mediaType);
//        return requestBody;
//    }
//
//
//    private void postRequest(String message, String URL) {
//
//        try {
//            RequestBody requestBody = buildRequestBody(message);
//            OkHttpClient okHttpClient = new OkHttpClient();
//            okhttp3.Request request = new Request
//                    .Builder()
//                    .post(requestBody)
//                    .url(URL)
//                    .build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onFailure(final Call call, final IOException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            Toast.makeText(SignUp.class, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onResponse(Call call, final Response response) throws IOException {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                String answer = response.peekBody(2048).string();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    });
//                }
//            });
//        }
//        catch (Exception ex) {
//            Log.d("crash", "crash", ex);
//        }
//    }
}