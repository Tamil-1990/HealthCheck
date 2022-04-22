package com.sample.healthcheck.repository

import com.sample.healthcheck.model.HealthCheck
import io.reactivex.Observable
import retrofit2.http.GET

interface APIInterface {
    @GET("otp-mgmt/health-check")
    suspend fun getHealthCheckDetails() : HealthCheck

    @GET("otp-mgmt/health-check")
    fun getRxHealthCheckDetails() : Observable<HealthCheck>
}