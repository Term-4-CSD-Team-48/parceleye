package com.example.parceleyelogin;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ApiClient {
    private static final String BASE_URL = "";
    private static Retrofit retrofit = null;
    /**
     * TODO: Find a way to make ApiClient store sessionId when valid login attempt is made.
     * As of now the MainActivity is responsible for setting the sessionId with setSessionId
     */
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

    public static Calls getCalls() {
        if (retrofit == null) {
            Interceptor sessionInterceptor = new Interceptor() {
                @NonNull
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder();

                    // Add JSESSIONID if available
                    if (sessionId.get() != null)
                        requestBuilder.addHeader("Cookie", "JSESSIONID=" + sessionId.get());

                    return chain.proceed(requestBuilder.build());
                }
            };
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder()
                    .addInterceptor(sessionInterceptor)
                    .build()).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(Calls.class);
    }

}
