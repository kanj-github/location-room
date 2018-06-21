package com.example.kanj.steallocation.api

import com.example.kanj.steallocation.api.pojo.ReqUserLoc
import io.reactivex.Completable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/v1/client/test/user/candidate/location")
    fun uploadLocation(@Body loc: ReqUserLoc): Completable

    companion object {
        val INSTANCE: UserService by lazy {
            val okHttp = OkHttpClient.Builder()
                    .addInterceptor(ApiAuthInterceptor())
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(okHttp)
                    .baseUrl("https://locus-api.com")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            retrofit.create(UserService::class.java)
        }
    }
}