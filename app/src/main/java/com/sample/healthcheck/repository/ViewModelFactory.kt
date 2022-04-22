package com.sample.healthcheck.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.healthcheck.MainViewModel

/*Create ViewModelFactory for custom ViewModelProvider  */
class ViewModelFactory (private val apiHelper: APIInterface,var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper),application = application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}