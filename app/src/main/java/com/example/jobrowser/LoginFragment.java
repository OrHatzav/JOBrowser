package com.example.jobrowser;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_login.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button login = view.findViewById(R.id.btn_login);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerManager request = new ServerManager();
                String answer = request.getPosts("{\"email\": \"" + email.getText().toString() + "\", \"password\": \""+ password.getText().toString() + "\"}", "/SignIn");
                if(answer.equals("false"))
                {
                    Toast.makeText(getContext(), getContext().getString(R.string.email_or_password), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String, String> properties = Splitter.on(", ")
                            .withKeyValueSeparator(": ")
                            .split(answer);

                    boolean isFound = false;
                    for (Map.Entry<String, String> entry : properties.entrySet()) {
                        if (entry.getKey().equals("BusinessName"))
                        {
                            isFound = true;
                        }

                        if(entry.getKey().equals("_id"))
                        {
                            request.setClientID(answer.substring(2,answer.length()-1));
                        }
                    }
                    request.setIsBusiness(isFound);

                    Intent i = new Intent(getActivity(), ProfilePage.class);
                    i.putExtra("profile", answer);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });
        return view;


    }
}