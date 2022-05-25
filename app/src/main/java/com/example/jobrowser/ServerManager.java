package com.example.jobrowser;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import org.cactoos.func.Async;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerManager extends AppCompatActivity {

    public ServerManager() {

    }
    private String clientID;
    private boolean isBusiness;
    public void setClientID(String id)
    {
        this.clientID = id;
    }

    public String getClientID()
    {
        return clientID;
    }
/////////////
    public void setIsBusiness(boolean isBusiness)
    {
        this.isBusiness = isBusiness;
    }

    public boolean getIsBusiness()
    {
        return isBusiness;
    }

    private final String SERVER_URL = "http://192.168.75.17:5000";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    public String getPosts(String message, String kindOfMsg)
    {
        try {
            Async<Object, String> asyncFunction = new Async<>(param -> postRequest(message, kindOfMsg));
            String ret = asyncFunction.apply(null).get();
            return  ret;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

//    public String getFromServer(String message, String kindOfMsg)
//    {
//        try {
//            RequestBody requestBody = buildRequestBody(message);
//            OkHttpClient okHttpClient = new OkHttpClient();
//            okhttp3.Request request = new okhttp3.Request
//                    .Builder()
//                    .post(requestBody)
//                    .url(SERVER_URL + kindOfMsg)
//                    .build();
//
//            Response response = okHttpClient.newCall(request).execute();
//            if (response.isSuccessful()) {
//                return response.peekBody(2048).string();
//            }
//            return null;
//        }
//        catch (Exception ex)
//        {
//            return null;
//        }
//    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;

    }

    private String postRequest(String message, String kindOfMsg) {
        try {
            RequestBody requestBody = buildRequestBody(message);
            OkHttpClient okHttpClient = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request
                    .Builder()
                    .post(requestBody)
                    .url(SERVER_URL + kindOfMsg)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.peekBody(2048).string();
            }

        } catch (Exception ex) {
            Log.d("crash", "crash", ex);
        }
        return null;
    }
}
