package com.haja.hja.Service

import com.google.gson.GsonBuilder
import com.haja.hja.Service.ApiService.Companion.BASEURL
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator(token:String) {
        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                var request = chain.request()
                var httpUrl = request.url

                httpUrl = httpUrl.newBuilder()
                    .build()

                request = if (token != "")
                    request.newBuilder().url(httpUrl)
                        .addHeader("Authorization" , "Bearer $token").build()
                else
                    request.newBuilder().url(httpUrl).build()

                chain.proceed(request)
            }.build()

    private val okHttpClientForSms = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()

            var string = request.url.toString()

            val newRequest =  Request.Builder()
                .url(string)
                .build()
            chain.proceed(newRequest)
        }.build()

        private val gson = GsonBuilder()
                .setLenient()
                .create()
        private val builder = Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))

    private val builderForSms = Retrofit.Builder()
        .baseUrl("http://www.kwtsms.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))

        private val retrofit = builder.build()

        val createService = retrofit.create(ApiService::class.java)
        val createServiceForSms = builderForSms.build().create(ApiService::class.java)
}

//enqueue  extension
fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

//Callback  extension
class CallBackKt<T> : Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }
}