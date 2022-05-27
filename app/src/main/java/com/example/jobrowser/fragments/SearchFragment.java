package com.example.jobrowser.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.jobrowser.GetPosts;
import com.example.jobrowser.R;
import com.example.jobrowser.SearchPosts;
import com.example.jobrowser.ServerManager;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private LinearLayout contain;

    private TreeNode root = TreeNode.root();
    private TreeNode[] array;
    private String[] ids;
    private boolean[] areMarked;
    private Button search;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        contain = view.findViewById(R.id.container);
        search = view.findViewById(R.id.search);

        ServerManager request = new ServerManager();
        printInfo(request.getPosts("1","/GetSubjects"));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                postRequest(packToSend(),url+ "/GetPosts");
                if(array.length > 0) {
                    Intent i = new Intent(getActivity(), GetPosts.class);
                    i.putExtra("message", request.getPosts(packToSend(), "/GetPosts"));
                    startActivity(i);
                }

            }
        });

        return view;
    }


    private void printInfo(String answer)
    {
        answer = answer.substring(1, answer.length() -1);
        Map<String, String> properties = Splitter.on(", ")
                .withKeyValueSeparator(": ")
                .split(answer);

        array = new TreeNode[properties.size()];
        ids = new String[properties.size()];

        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            array[i] = new TreeNode(entry.getKey());
            ids[i] = entry.getValue().substring(1, entry.getValue().length() - 1);
            i++;
        }

        for (int j = 0; j < ids.length;j++) {
            if(ids[j].length() == 1)
            {
                root.addChild(array[j]);
            }
            else
            {
                for(int h = 0;h <ids.length;h++)
                {
                    if(ids[h].equals(ids[j].substring(0,ids[j].length() -1)))
                    {
                        array[h].addChild(array[j]);
                        break;
                    }
                }
            }
        }

        AndroidTreeView tView = new AndroidTreeView(getContext(), root);
        tView.setDefaultAnimation(true);
        contain.addView(tView.getView());

        listeners();
    }

    private void listeners()
    {
        areMarked = new boolean[array.length];
        for(int i = 0; i< array.length;i++)
        {
            areMarked[i] = false;
        }

        for(int i = 0; i<array.length; i++)
        {
            if (array[i].isLeaf())
            {
                int finalI = i;
                array[i].setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        areMarked[finalI] = !areMarked[finalI];
                    }
                });
            }

        }
    }

    private String packToSend()
    {
        String paths = "{";
        int index = 0;
        for(int i = 0;i< areMarked.length;i++)
        {
            if(areMarked[i])
            {
                paths += "\"" + index + "\"" + ": \"";
                index++;
                for(int j = 1; j<ids[i].length();j++)
                {
                    int h = 0;
                    for(; h< ids.length;h++)
                    {
                        if(ids[h].equals(ids[i].substring(0, j)))
                        {
                            break;
                        }
                    }
                    paths += array[h].getValue().toString().substring(1,array[h].getValue().toString().length()-1) + "/";
                }
                if(array[i].getParent().getChildren().size() == 1)
                {
                    paths = paths.substring(0, paths.length()-1);
                    paths += ":";
                }
                paths += array[i].getValue().toString().substring(1,array[i].getValue().toString().length()-1) + "\", ";

            }
        }
        paths = paths.substring(0, paths.length()-2);
        paths += "}";
        Log.d("hh", paths);
        return paths;
    }

}