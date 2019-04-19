package com.mojodigi.khulasaNewsLite.AddsUtility;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkhttpMethods {

    static String apiResponse;
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
   static OkHttpClient client = new OkHttpClient();
    public static String CallApi(String Url) {
        //tested code
        // from official site http://square.github.io/okhttp/
        //OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(Url);
        Request request = builder.build();
        try {

            Response respone = client.newCall(request).execute();
            if(respone.isSuccessful())
            return respone.body().string();
            else return "Error in connection";
        } catch (Exception e) {
            return e.getMessage();
        }


    }

    public static String CallApi(String Url, String[]key, String[]values) throws Exception {

        //tested code
        // from official site http://square.github.io/okhttp/

       // OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("store_id", "1058")
                .add("cust_id", "55").build();

        Request request = new Request.Builder().url(Url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful())
       return response.body().string();
        else return "Error in connection";

        //assertTrue(response.isSuccessful());
    }

   public static String CallApi(Context mContext, String url, String json) throws IOException
    {
         //tested code
        // from official site http://square.github.io/okhttp/
        //OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        SharedPreferenceUtil appPref=new  SharedPreferenceUtil(mContext);
        int code=response.code();
        if(response.isSuccessful())
        {
            appPref.setValue(AddConstants.API_RESPONSE_CODE,code );
            System.out.print("Data -->" + response.toString());
        }
        else
        {
            appPref.setValue(AddConstants.API_RESPONSE_CODE,code);
            return "Error in connection";
        }
        return response.body().string();

    }

    public void PostFile() throws Exception
    {
          OkHttpClient client = new OkHttpClient();
          final MediaType MEDIA_TYPE_PLAINTEXT = MediaType.parse("text/plain; charset=utf-8");
          File file = new File("src/test/resources/Lorem Ipsum.txt");
          Request request = new Request.Builder().url("http://httpbin.org/post")
                .post(RequestBody.create(MEDIA_TYPE_PLAINTEXT, file))
                .build();
          Response response = client.newCall(request).execute();
         // assertTrue(response.isSuccessful());

    }
    public void downloadFileAsync(final String downloadUrl) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }
                FileOutputStream fos = new FileOutputStream("d:/tmp.txt");
                fos.write(response.body().bytes());
                fos.close();
            }
        });
    }
    public void downloadFileSync(String downloadUrl) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }
        FileOutputStream fos = new FileOutputStream("d:/tmp.txt");
        fos.write(response.body().bytes());
        fos.close();
    }
    public void uploadImage(File image, String imageName) throws IOException {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", imageName, RequestBody.create(MEDIA_TYPE_PNG, image))
                .build();

        Request request = new Request.Builder().url("http://localhost:8080/v1/upload")
                .post(requestBody).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

    }


}
