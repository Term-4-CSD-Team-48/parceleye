package com.example.parceleyelogin;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.4:8080";
    private static final Calls calls = new Retrofit.Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder();

                    // Add JSESSIONID if available
                    if (sessionId.get() != null)
                        requestBuilder.addHeader("Cookie", "JSESSIONID=" + sessionId.get());

                    return chain.proceed(requestBuilder.build());
                }
            })
            .build()).addConverterFactory(GsonConverterFactory.create()).build().create(Calls.class);
    private static final String TAG = "ApiClient";

    private static final AtomicReference<String> sessionId = new AtomicReference<>(null);
    public static void setSessionId(String arg) {
        sessionId.set(arg);
    }

    public static interface Calls {
        @FormUrlEncoded
        @POST("auth/login")
        Call<Void> login(
                @Field("email") String email,
                @Field("password") String password
        );

        @FormUrlEncoded
        @POST("auth/register")
        Call<Void> registerUser(
                @Field("username") String username,
                @Field("email") String email,
                @Field("password") String password
        );
    }

    public static interface Callbacks {
        void onResponse(int code);
        void onFailure(Throwable t);
    }

    public static void register(String email, String username, String password, Callbacks callbacks) {
        Call<Void> call = calls.registerUser(username, email, password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                callbacks.onResponse(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                callbacks.onFailure(t);
            }
        });
    }
}
