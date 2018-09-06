package com.spencer.retrofit.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spencer.retrofit.demo.api.Person;
import com.spencer.retrofit.demo.retrofit.Retrofit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(){
            @Override
            public void run() {
                super.run();
                Retrofit retrofit = new Retrofit.Bundler()
                        .baseUrl("http://v.juhe.cn")
                        .builder();
                Person person = retrofit.create(Person.class);
                Call call = person.get("","");
                Response responseBody = null;
                try {
                    responseBody = call.execute();
                    final String message = responseBody.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "run: " + message);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
