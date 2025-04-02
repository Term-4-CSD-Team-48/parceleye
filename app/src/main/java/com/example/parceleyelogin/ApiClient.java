package com.example.parceleyelogin;

import android.util.Log;

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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ApiClient {

    private static final String BASE_URL = "http://18.142.237.177:8080";
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

                    Log.v(TAG, "JSESSIONID: " + sessionId.get());

                    return chain.proceed(requestBuilder.build());
                }
            })
            .build()).addConverterFactory(GsonConverterFactory.create()).build().create(Calls.class);
    private static final String TAG = "ApiClient";

    private static final AtomicReference<String> sessionId = new AtomicReference<>(null);
    private static void setSessionId(String arg) {
        sessionId.set(arg);
    }

    public static class PromptRequest {
        final public float x;
        final public float y;

        public PromptRequest(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public interface Calls {
        @POST("ai/observe")
        Call<Void> observe(
        );

        @POST("ai/prompt")
        Call<Void> prompt(
                @Body PromptRequest promptRequest
        );

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

    public interface CallbackParts {
        default void onResponse(int code) {};
        default void onFailure(Throwable t) {}
    }

    public static class DefaultCallback implements Callback<Void> {
        private final CallbackParts callbackParts;

        public DefaultCallback(CallbackParts callbackParts) {
            this.callbackParts = callbackParts;
        }

        @Override
        public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
            Log.i(TAG, "Call to " + call.request().url().url().toString() + " returned " + response.code());
            callbackParts.onResponse(response.code());
        }

        @Override
        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            Log.e(TAG, "Network error: " + t.getMessage());
            callbackParts.onFailure(t);
        }
    }

    public static void register(String email, String username, String password, CallbackParts callbackParts) {
        Call<Void> call = calls.registerUser(username, email, password);
        call.enqueue(new DefaultCallback(callbackParts));
    }

    public static void login(String email, String password, CallbackParts callbackParts) {
        Call<Void> call = calls.login(email, password);
        call.enqueue(new DefaultCallback(callbackParts) {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                super.onResponse(call, response);
                // Extract the 'Set-Cookie' header
                String setCookieHeader = response.headers().get("Set-Cookie");

                if (setCookieHeader != null) {
                    Log.d(TAG, "Set-Cookie Header: " + setCookieHeader);

                    // Extract JSESSIONID from the header
                    String jsessionId = null;
                    for (String cookie : response.headers().values("Set-Cookie")) {
                        if (cookie.startsWith("JSESSIONID")) {
                            jsessionId = cookie.split(";")[0].split("=")[1].trim(); // Extracts 'JSESSIONID=xxxx'
                            break;
                        }
                    }

                    Log.d(TAG, "Extracted JSESSIONID: " + jsessionId);
                    if (jsessionId == null) {
                        callbackParts.onResponse(500);
                    } else {
                        setSessionId(jsessionId);
                        callbackParts.onResponse(200);
                    }
                }
            }
        });
    }

    public static void observe(CallbackParts callbackParts) {
        Call<Void> call = calls.observe();
        call.enqueue(new DefaultCallback(callbackParts));
    }

    public static void prompt(float x, float y, CallbackParts callbackParts) {
        PromptRequest promptRequest = new PromptRequest(x, y);
        Call<Void> call = calls.prompt(promptRequest);
        call.enqueue(new DefaultCallback(callbackParts));
    }
}
