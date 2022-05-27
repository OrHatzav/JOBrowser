package com.example.jobrowser.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jobrowser.R;
import com.example.jobrowser.ServerManager;
import com.example.jobrowser.UploadPost;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment {

    private LinearLayout contain;

    private TreeNode root = TreeNode.root();
    private TreeNode[] array;
    private String[] ids;
    private boolean[] areMarked;
    private Button create;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
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
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        contain = view.findViewById(R.id.container);
        create = view. findViewById(R.id.create);

        ServerManager request = new ServerManager();
        printInfo(request.getPosts("1", "/GetSubjects"));
//        postRequest("1", url);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markParents();
                String send = parentsToSend();
                Log.d("hh", request.getClientID());
                request.getPosts("[" + send + ", \"" + request.getClientID() + "\"]", "/CreatePost");
                Toast.makeText(getContext(), getContext().getString(R.string.post_uploaded), Toast.LENGTH_SHORT).show();

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
            if (ids[j].length() == 1) {
                root.addChild(array[j]);
            } else {
                for (int h = 0; h < ids.length; h++) {
                    if (ids[h].equals(ids[j].substring(0, ids[j].length() - 1))) {
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
    private void markParents()
    {
        for(int i = 0;i<areMarked.length;i++)
        {
            if(areMarked[i] && array[i].isLeaf())
            {
                TreeNode parent = array[i].getParent();
                while(parent.getId() != root.getId())
                {
//                   Log.d("hh", parent.getValue().toString());
                    for(int j = 0; j<array.length; j++)
                    {
                        if(array[j].getValue().toString().equals(parent.getValue().toString()))
                        {
                            areMarked[j] = true;
                            break;
                        }
                    }
                    parent = parent.getParent();
                }
            }

        }
        removeNotMarked();

    }
    private void removeNotMarked()
    {
        int counter = 0;
        for(int i = 0;i< areMarked.length;i++)
        {
            if(areMarked[i])
            {
                counter++;
            }
        }

        TreeNode[] arrayToSend = new TreeNode[counter];
        String[] idsToSends = new String[counter];
        int index = 0;
        for(int i = 0; i< array.length;i++)
        {
            if(areMarked[i])
            {
                arrayToSend[index] = array[i];
                idsToSends[index] = ids[i];
                index++;
            }
        }
        array = arrayToSend;
        ids = idsToSends;

    }

    private String parentsToSend()
    {
        int counter = 0;
        for(int i = 0; i< array.length;i++)
        {
            if(ids[i].length() == 1)
            {
                counter++;
            }
        }
        TreeNode[] parents = new TreeNode[counter];
        int index = 0;
        for(int i = 0; i< array.length;i++)
        {
            if(ids[i].length() == 1)
            {
                parents[index] = array[i];
                index++;
            }
        }
        String send = "{";
        for(int i = 0;i< parents.length;i++)
        {
            for(int j = 0; j< array.length;j++)
            {
                if(array[j].getValue().toString().equals(parents[i].getValue().toString()))
                {
                    send += parents[i].getValue().toString() +":" + packToSend(parents[i]);
                    if(i == parents.length-1)
                    {
                        send+= ",";
                    }
                    break;
                }
            }

        }
        send += "}";
        for(int i = 0; i<send.length()-1;i++)
        {
            if(send.charAt(i) == ',' && (send.charAt(i+1)== ']' || send.charAt(i+1) == '}'))
            {
                send = send.substring(0, i) + send.substring(i+1);
            }
        }
        return send;

    }

    private String packToSend(TreeNode parent)
    {
        if(parent.getChildren().get(0).isLeaf())
        {
            if(parent.getChildren().size() == 1) {
                for (int j = 0; j < array.length; j++) {
                    if (array[j].getValue().toString().equals(parent.getChildren().get(0).getValue().toString())) {
                        return parent.getChildren().get(0).getValue().toString();
                    }

                }
            }
            String children = "[";
            for(int i = 0;i<parent.getChildren().size(); i++)
            {
                for(int j = 0; j< array.length;j++)
                {
                    if(array[j].getValue().toString().equals(parent.getChildren().get(i).getValue().toString()))
                    {
                        children += parent.getChildren().get(i).getValue().toString();
                        if(i<parent.getChildren().size()-1)
                        {
                            children +=",";
                        }
                        break;
                    }
                }

            }
            children += "]";
            return children;
        }
        String children = "{";
        for(int i = 0; i<parent.getChildren().size(); i++)
        {
            for(int j = 0; j< array.length;j++)
            {
                if(array[j].getValue().toString().equals(parent.getChildren().get(i).getValue().toString()))
                {
                    children += parent.getChildren().get(i).getValue().toString() + ":" +
                            packToSend(parent.getChildren().get(i));
                    if(i < parent.getChildren().size()-1)
                    {
                        children += ",";
                    }
                    break;
                }
            }



        }
        children += "}";
        return children;
    }
}