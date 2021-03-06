package br.com.jonasmendes.desafio_android.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.jonasmendes.desafio_android.utils.MyApplication;
import cz.msebera.android.httpclient.Header;

public class SearchController {
    public static String TAG = SearchController.class.getSimpleName();
    public static String URL = "https://api.github.com/search/";
    public static void getRepository(){
        getRepository("1");
    }

    public static void getRepository(String page){
        final RequestParams params = new RequestParams();
        params.put("q","language:Java");
        params.put("sort","stars");
        params.put("page",page);
        String url = URL+"repositories";
        AsyncHttpClient client = new AsyncHttpClient();
        client.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    Log.d(TAG, "Subiu");
                    Log.d(TAG, response.getJSONArray("items").toString());
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("success", true);
                    bundle.putString("repository", response.getJSONArray("items").toString());
                    Intent intent = new Intent("desafio-android-get-repository");
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    LocalBroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(intent);
                } catch (Exception e) {
                    sendError();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                try{
                    Log.d(TAG, "Não foi");
                    Log.d(TAG, errorResponse.toString());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                sendError();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                try{
                    Log.d(TAG, "Não foi");
                    Log.d(TAG, errorResponse.toString());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                sendError();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                try{
                    Log.d(TAG, "Não foi");
                    Log.d(TAG, responseString.toString());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                sendError();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    protected static void sendError(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("success", false);
        bundle.putString("repository", "");
        Intent intent = new Intent("desafio-android-get-repository");
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        LocalBroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(intent);
    }
}
