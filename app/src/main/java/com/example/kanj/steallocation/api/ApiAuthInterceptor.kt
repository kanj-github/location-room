package com.example.kanj.steallocation.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class ApiAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val authReq = req.newBuilder().header(
                "Authorization",
                Credentials.basic("test/candidate", "c00e-4764")
        ).build()
        return chain.proceed(authReq)
    }
}