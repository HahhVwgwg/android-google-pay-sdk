package android.google.paysdk.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIMEOUT = 10000L

fun getPaymentDataSource(serverUrl: String): android.google.paysdk.data.dataSource.PaymentDataSource {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val retrofit = Retrofit.Builder()
        .baseUrl(serverUrl)
        .addConverterFactory(GsonConverterFactory.create()).client(
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()
        )
        .build()
    // Create an instance of the service interface.
    return retrofit.create(android.google.paysdk.data.dataSource.PaymentDataSource::class.java)
}
