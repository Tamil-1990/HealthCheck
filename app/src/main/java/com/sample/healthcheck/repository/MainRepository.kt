package com.sample.healthcheck.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.healthcheck.model.HealthCheck
import io.reactivex.schedulers.Schedulers

class MainRepository(private val apiHelper: APIInterface) {

    suspend fun getHealthCheckDetails() = apiHelper.getHealthCheckDetails()

    /*2nd way  RxJava*/
    private val _response = MutableLiveData<Status>()
    val response: LiveData<Status> get() = _response
    private val _healthChekList = MutableLiveData<HealthCheck>()
    val healthChekList: LiveData<HealthCheck> get() = _healthChekList
    fun getHealthCheckList(applications: Application) {
        _response.postValue(Status.LOADING)
        if (!Util.isNetworkAvailable(applications)) {
            _response.postValue(Status.ERROR)
        } else {
            try {
                apiHelper.getRxHealthCheckDetails().subscribeOn(Schedulers.io()).subscribe({
                    if (it.statusCode == 200 && it.success) {
                        _healthChekList.postValue(it)
                        _response.postValue(Status.SUCCESS)
                    } else {
                        _response.postValue(Status.ERROR)
                    }
                }, {
                    _response.postValue(Status.ERROR)
                })
            } catch (e: Exception) {
                _response.postValue(Status.ERROR)
            }
        }
    }/*2nd way  RxJava*/
}