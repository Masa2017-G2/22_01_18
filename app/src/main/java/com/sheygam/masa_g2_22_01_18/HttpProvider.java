package com.sheygam.masa_g2_22_01_18;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gregorysheygam on 22/01/2018.
 */

public class HttpProvider {
    private static final HttpProvider ourInstance = new HttpProvider();
    public static final String BASE_URL = "https://telranstudentsproject.appspot.com/_ah/api/contactsApi/v1";

    private Gson gson;
    private OkHttpClient client;
    private MediaType JSON;
    public static HttpProvider getInstance() {
        return ourInstance;
    }

    private HttpProvider() {
        gson = new Gson();
//        client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        JSON = MediaType.parse("application/json; charset=utf-8");
    }

    public AuthToken registration(Auth auth) throws Exception {
        String requestJson = gson.toJson(auth);

        RequestBody body = RequestBody.create(JSON,requestJson);

        Request request = new Request.Builder()
                .url(BASE_URL + "/registration")
                .post(body)
//                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            String responseJson = response.body().string();
            AuthToken authToken = gson.fromJson(requestJson,AuthToken.class);
            return authToken;
        }else if(response.code() == 409){
            throw new Exception("User already exist!");
        }else{
            String error = response.body().string();
            Log.d("MY_TAG", "registration: error: " + error);
            throw new Exception("Server error! Call to support!");
        }
    }

    public void login(Auth auth, Callback callback){
        String requestJson = gson.toJson(auth);

        RequestBody requestBody = RequestBody.create(JSON, requestJson);

        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
