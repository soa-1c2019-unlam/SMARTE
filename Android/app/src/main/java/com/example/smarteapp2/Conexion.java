package com.example.smarteapp2;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Conexion {

    public static String getDatos (String urlUsuario){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlUsuario)
                .build();

        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch(IOException error){
            return error.toString();
        }
    }

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
