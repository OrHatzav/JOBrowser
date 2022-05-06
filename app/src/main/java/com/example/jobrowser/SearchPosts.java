package com.example.jobrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchPosts extends AppCompatActivity {
    private String url = "http://" + "192.168.1.28" + ":" + 5000;
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;
    private LinearLayout container;

    private TreeNode root = TreeNode.root();
    private TreeNode[] array;
    private String[] ids;
    private boolean[] areMarked;
    private Button search;

    private Boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);
        container = findViewById(R.id.container);
        search = findViewById(R.id.search);

        postRequest("1", url+ "/GetSubjects");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPressed = true;
                postRequest(packToSend(),url+ "/GetPosts");
            }
        });

    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;

    }


    private void postRequest(String message, String URL) {

        try {
            RequestBody requestBody = buildRequestBody(message);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .post(requestBody)
                    .url(URL)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchPosts.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String answer = response.peekBody(2048).string();
                                if(!isPressed)
                                {
                                    printInfo(answer);
                                }
                                else
                                {
                                    Intent i = new Intent(SearchPosts.this, GetPosts.class);
                                    i.putExtra("message", answer);
                                    startActivity(i);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                }
            });
        }
        catch (Exception ex) {
            Log.d("crash", "crash", ex);
        }
    }

////    //////////////


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

        AndroidTreeView tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        container.addView(tView.getView());

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

//    private void markParents()
//    {
//        for(int i = 0;i<areMarked.length;i++)
//        {
//            if(areMarked[i] && array[i].isLeaf())
//            {
//                TreeNode parent = array[i].getParent();
//                while(parent.getId() != root.getId())
//                {
////                   Log.d("hh", parent.getValue().toString());
//                    for(int j = 0; j<array.length; j++)
//                    {
//                        if(array[j].getValue().toString().equals(parent.getValue().toString()))
//                        {
//                            areMarked[j] = true;
//                            break;
//                        }
//                    }
//                    parent = parent.getParent();
//                }
//            }
//
//        }
////        removeNotMarked();
//
//    }
//    private void removeNotMarked()
//    {
//        int counter = 0;
//        for(int i = 0;i< areMarked.length;i++)
//        {
//            if(areMarked[i])
//            {
//                counter++;
//            }
//        }
//
//        TreeNode[] arrayToSend = new TreeNode[counter];
//        String[] idsToSends = new String[counter];
//        int index = 0;
//        for(int i = 0; i< array.length;i++)
//        {
//            if(areMarked[i])
//            {
//                arrayToSend[index] = array[i];
//                idsToSends[index] = ids[i];
//                index++;
//            }
//        }
//        array = arrayToSend;
//        ids = idsToSends;
//
//    }
//
//    private String parentsToSend()
//    {
//        int counter = 0;
//        for(int i = 0; i< array.length;i++)
//        {
//            if(ids[i].length() == 1)
//            {
//                counter++;
//            }
//        }
//        TreeNode[] parents = new TreeNode[counter];
//        int index = 0;
//        for(int i = 0; i< array.length;i++)
//        {
//            if(ids[i].length() == 1)
//            {
//                parents[index] = array[i];
//                index++;
//            }
//        }
//        String send = "{";
//        for(int i = 0;i< parents.length;i++)
//        {
//            for(int j = 0; j< array.length;j++)
//            {
//                if(array[j].getValue().toString().equals(parents[i].getValue().toString()))
//                {
//                    send += parents[i].getValue().toString() +":" + packToSend(parents[i]);
//                    if(i == parents.length-1)
//                    {
//                        send+= ",";
//                    }
//                    break;
//                }
//            }
//
//        }
//        send += "}";
//        for(int i = 0; i<send.length()-1;i++)
//        {
//            if(send.charAt(i) == ',' && (send.charAt(i+1)== ']' || send.charAt(i+1) == '}'))
//            {
//                send = send.substring(0, i) + send.substring(i+1);
//            }
//        }
//        return send;
//
//    }
//
//    private String packToSend(TreeNode parent)
//    {
//        if(parent.getChildren().get(0).isLeaf())
//        {
//            if(parent.getChildren().size() == 1) {
//                for (int j = 0; j < array.length; j++) {
//                    if (array[j].getValue().toString().equals(parent.getChildren().get(0).getValue().toString())) {
//                        return parent.getChildren().get(0).getValue().toString();
//                    }
//
//                }
//            }
//            String children = "[";
//            for(int i = 0;i<parent.getChildren().size(); i++)
//            {
//                for(int j = 0; j< array.length;j++)
//                {
//                    if(array[j].getValue().toString().equals(parent.getChildren().get(i).getValue().toString()))
//                    {
//                        children += parent.getChildren().get(i).getValue().toString();
//                        if(i<parent.getChildren().size()-1)
//                        {
//                            children +=",";
//                        }
//                        break;
//                    }
//                }
//
//            }
//            children += "]";
//            return children;
//        }
//        String children = "{";
//        for(int i = 0; i<parent.getChildren().size(); i++)
//        {
//            for(int j = 0; j< array.length;j++)
//            {
//                if(array[j].getValue().toString().equals(parent.getChildren().get(i).getValue().toString()))
//                {
//                    children += parent.getChildren().get(i).getValue().toString() + ":" +
//                            packToSend(parent.getChildren().get(i));
//                    if(i < parent.getChildren().size()-1)
//                    {
//                        children += ",";
//                    }
//                    break;
//                }
//            }
//
//
//
//        }
//        children += "}";
//        return children;
//    }

}