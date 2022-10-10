package waslim.githubuserapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import waslim.githubuserapp.BuildConfig

object ApiConfig {

    private const val URL = BuildConfig.BASE_URL

    private val loggingInterceptor = if(BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val clint = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val instance : ApiServices by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clint)
            .build()
        retrofit.create(ApiServices::class.java)
    }
}