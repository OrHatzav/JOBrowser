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

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        Button register = view.findViewById(R.id.btn_register);
        RadioButton worker =  view.findViewById(R.id.worker);
        RadioButton business = view.findViewById(R.id.business);
        EditText email = view.findViewById(R.id.email);
        EditText password = view.findViewById(R.id.password);
        EditText repassword = view.findViewById(R.id.repassword);
        TextView notMatch = view.findViewById(R.id.not_match);

        ServerManager request = new ServerManager();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(repassword.getText().toString()))
                {
                    if(password.getText().toString().length()>= 6 && password.getText().toString().length() <= 14)
                    {
                        if(worker.isChecked() || business.isChecked())
                        {
                            String answer = request.getPosts(email.getText().toString(),"/EmailExists");
                            if(answer.equals("true"))
                            {
                                Toast.makeText(getContext(), getContext().getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Intent i;
                                if(business.isChecked())
                                {
                                    i = new Intent(getActivity(), CreateBusinessProfile.class);
                                }
                                else
                                {
                                    i = new Intent(getActivity(), CreateWorkerProfile.class);
                                }
                                i.putExtra("email", email.getText().toString());
                                String hashed = md5Hash(password.getText().toString());
                                i.putExtra("password", hashed);
                                startActivity(i);
                                getActivity().finish();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), getContext().getString(R.string.user_type_not_checked), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), getContext().getString(R.string.password_6_14_letters), Toast.LENGTH_SHORT).show();
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

    public String md5Hash(String password)  {
        return DigestUtils.md5Hex(password).toUpperCase();
    }

}