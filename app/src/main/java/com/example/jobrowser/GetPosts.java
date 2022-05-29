package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;


public class GetPosts extends AppCompatActivity {
    private LinearLayout container;

    private TreeNode[] array;
    private String[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_posts);
        container = findViewById(R.id.container);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("message");

        try {
            ArrayList<String> arrayList = jsonStringToArray(value);
            for(int i = 0; i< arrayList.size();i++) {
                printInfo(String.valueOf(arrayList.get(i)));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }


    private void printInfo(String answer)
    {
        TreeNode root = TreeNode.root();

        answer = answer.substring(1, answer.length() -1);
        Map<String, String> properties = Splitter.on(",")
                .withKeyValueSeparator(":")
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
                        array[h].setExpanded(true);
                        break;
                    }
                }
            }
        }

        String id = "0";
        for(int index = 0;index < root.getChildren().size(); index++)
        {
            if(root.getChildren().get(index).getValue().toString().equals("\"Business ID\""))
            {
                id = root.getChildren().get(index).getChildren().get(0).getValue().toString();
            }
        }


        LinearLayout layout = new LinearLayout(this);
        container.addView(layout);
        layout.setBackgroundResource(R.color.white);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 5, 0, 5);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        layout.setLayoutParams(params);


        final String fID = id;

        AndroidTreeView tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        layout.addView(tView.getView());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fID.equals("0")) {
                    Intent intent = new Intent(GetPosts.this, ProfilePage.class);
                    intent.putExtra("id", fID.substring(1, fID.length()-1));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }



}