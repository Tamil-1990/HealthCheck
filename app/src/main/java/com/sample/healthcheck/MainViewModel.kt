package com.sample.healthcheck

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sample.healthcheck.model.HealthCheck
import com.sample.healthcheck.repository.*
import kotlinx.coroutines.Dispatchers

class MainViewModel(var mainRepository: MainRepository, application: Application): AndroidViewModel(application)  {

    companion object{
        val TAG: String =  MainViewModel::class.java.name
    }

    var apiInterface: APIInterface?=null
    var applications: Application? = null


    init {
        apiInterface = APIClient.getClient()
        applications = application
    }

    /*Call api & emit result using coroutines & live data HealthCheck & status*/
    fun getServerHealthCheckDetails() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            if (Util.isNetworkAvailable(applications!!)) {
                val apiResponse = mainRepository.getHealthCheckDetails()
                if(apiResponse.statusCode == 200 && apiResponse.success) {
                    emit(Resource.success(data = apiResponse))
                }else {
                    emit(Resource.error(data = null, message = "No records found"))
                }
            }else {
                emit(Resource.error(data = null, message = "No network, check your network connection"))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    /*RxJava Functions*/
    fun getHealthCheckList() {
        mainRepository.getHealthCheckList(applications!!)
    }
    val response: LiveData<Status> by lazy {
        mainRepository.response
    }
    val healthChekList: LiveData<HealthCheck> by lazy {
        mainRepository.healthChekList
    }/*RxJava Functions*/
}