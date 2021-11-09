package digital.iam.ma.datamanager.retrofit;

import java.util.concurrent.TimeUnit;

import digital.iam.ma.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private ApiServices apiServices;
    private String url;


    private RetrofitClient(String url) {
        this.url = url;
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(5, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);
        client.writeTimeout(30, TimeUnit.SECONDS);

        client.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json;version=" + BuildConfig.VERSION_NAME)
                    .addHeader("os", "android");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .client(client.build())
                .build();
        apiServices = retrofit.create(ApiServices.class);
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient(BuildConfig.BASEURL);
        }
        return retrofitClient;
    }

    public ApiServices endpoint() {
        return apiServices;
    }

    public void changeBaseUrl(String url) {
        retrofitClient = new RetrofitClient(url);
    }

    public String getUrl() {
        return url;
    }
}
