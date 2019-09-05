package com.haja.haja.Service

import com.google.gson.GsonBuilder
import com.haja.haja.Service.ApiService.Companion.BASEURL
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator(token:String) {
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                var httpUrl = request.url()

                httpUrl = httpUrl.newBuilder()
                    .addQueryParameter("", "")
                    .build()

                request = request.newBuilder().url(httpUrl)
                    .addHeader("Authorization" , "Bearer $token").build()
                chain.proceed(request)
            }.build()
        private val gson = GsonBuilder()
                .setLenient()
                .create()
        private val builder = Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))

        private val retrofit = builder.build()

        val createService = retrofit.create(ApiService::class.java)
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