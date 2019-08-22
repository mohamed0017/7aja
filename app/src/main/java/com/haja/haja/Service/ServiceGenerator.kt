package com.haja.haja.Service

import com.google.gson.GsonBuilder
import com.haja.haja.Service.ApiService.Companion.BASEURL
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {

    companion object {
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                var httpUrl = request.url()

                httpUrl = httpUrl.newBuilder()
                    .addQueryParameter("", "")
                    .build()

                request = request.newBuilder().url(httpUrl)
                    .addHeader("Authorization" , "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQ5YzNlN2FjMjQ5MzMzY2JhMjNiYjkwOGRhNzM3YTJiOTQ3NGQzZTkzNWExNDNhYzkyNWMzOTAxYzk3ZWM1M2VjNjE1NmI0MDkyNDJiOTk3In0.eyJhdWQiOiIxIiwianRpIjoiNDljM2U3YWMyNDkzMzNjYmEyM2JiOTA4ZGE3MzdhMmI5NDc0ZDNlOTM1YTE0M2FjOTI1YzM5MDFjOTdlYzUzZWM2MTU2YjQwOTI0MmI5OTciLCJpYXQiOjE1NjQ2MzQ5MjMsIm5iZiI6MTU2NDYzNDkyMywiZXhwIjoxNTk2MjU3MzIzLCJzdWIiOiIxNzQiLCJzY29wZXMiOltdfQ.rNRVXbojmVgHKAgT_qMF89IJesNkwJz7AieZPVwSnQa88cvtVPYKLOHfYfIQ2fmeuu0pgj9TlwfRfE5L4aUrz2mn5EjrOjYm_BJd_XvWY4hctwG8Q5iNVYK2KBqz6ToVA_uLrOEA72Pah87J5B7enLBvdhHO3DusFqx81YjWHg5BOPVUUAXbylzaOzWdGgX1EIefS8j4RK8gEnswLo1sy0CObQbrij9DtPR4522jZwpgYtCk6i2BsUTL8nXCitiHKSGaIlLhMYEbcU7wRgiW8pEDMMoGVSm3CZPBCn-6gvcb92qFPElH5W78qrHVgUWlnDsXunFSry_kA37ACQNFmlChT52gXmEHdGSYgSKkJvZ7INXwUHGbvDwJsYStBPO2ecyMLqpOqUN-g6TNdY1xatyRXziUFFQzXpR6qV8pvaJVfma3VXGpwQaYCuwurngQmrNtWazANPVWydLlGUIgGGgrBKonjmkkcFRfLpCnVxK2JF8y7OvWWJVMZnlLIB1NkeKGaH9G1IcxGLtzCOWv4M3CxePVyMdSlI6htrly6Iaxqlo8ZJk4yuGkdXvxgXdYdHi9xx2udAbAT7OuCbV8NZsEonrO1lxdzYmY-_8sZCsboLJNwu1AGZNSfDv5tvxye-wzj_Xjk9Uqs-3lLmMHbZ85ZK1Nw7NwSUpgltqmC_c").build()
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