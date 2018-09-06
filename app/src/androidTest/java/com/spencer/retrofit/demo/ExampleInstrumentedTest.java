package com.spencer.retrofit.demo;

import android.support.test.runner.AndroidJUnit4;

import com.spencer.retrofit.demo.api.Person;
import com.spencer.retrofit.demo.retrofit.Retrofit;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";
    @Test
    public void useAppContext() throws IOException {
        Retrofit retrofit = new Retrofit.Bundler()
                .baseUrl("http://v.juhe.cn")
                .builder();
        System.out.println("-------------------------------------------");
        Person person = retrofit.create(Person.class);
        Call call = person.get("","");
        Response responseBody = call.execute();
        System.out.println(responseBody.body().string());
    }
}
