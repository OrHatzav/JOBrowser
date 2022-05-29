package com.example.jobrowser.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jobrowser.BusinessProfile;
import com.example.jobrowser.MainActivity2;
import com.example.jobrowser.R;
import com.example.jobrowser.WorkerProfile;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ListView listView;

    private MainActivity2 main2 = new MainActivity2();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        listView = view.findViewById(R.id.ListViewID);

        String answer;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras.getString("registeredNow") == null) {
            answer = extras.getString("profile");
        } else {
            answer = extras.getString("profile");
        }

        ArrayList<String> info = new ArrayList<>();

        Map<String, String> properties = Splitter.on(", ")
                .withKeyValueSeparator(": ")
                .split(answer);

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (!entry.getKey().equals("{'_id")) {
                info.add(entry.getKey().substring(1, entry.getKey().length() - 1));
                if (entry.getValue().charAt(entry.getValue().length() - 1) == '}') {
                    info.add(entry.getValue().substring(1, entry.getValue().length() - 2));
                } else {
                    info.add(entry.getValue().substring(1, entry.getValue().length() - 1));
                }

            }
        }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (getContext(), android.R.layout.simple_list_item_1, info);

            listView.setDivider(null);
            // DataBind ListView with items from ArrayAdapter
            listView.setAdapter(arrayAdapter);

            return view;
        }
    }