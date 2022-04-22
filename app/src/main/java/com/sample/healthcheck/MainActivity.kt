package com.sample.healthcheck

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.healthcheck.adapter.HealthAdapter
import com.sample.healthcheck.databinding.ActivityMainBinding
import com.sample.healthcheck.model.Health
import com.sample.healthcheck.model.HealthCheck
import com.sample.healthcheck.repository.*

class MainActivity : AppCompatActivity() {

    companion object{
        val TAG: String = MainActivity::class.java.name
    }
    var binding: ActivityMainBinding? = null
    var mainViewModel: MainViewModel? = null

    var healthCheck: HealthCheck? = null
    var healthAdapter: HealthAdapter? = null
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding?.mainViewModel = mainViewModel
        binding?.mainView = this
        mainViewModel = ViewModelProvider(this, ViewModelFactory(APIClient.getClient(),application))[MainViewModel::class.java]

        binding?.txtNoData?.setOnClickListener {
            checkNetworkConnection()
        }
    }

    /*Whenever on resume triggered check network connected or not */
    override fun onResume() {
        super.onResume()
        checkNetworkConnection()
    }

    /*check network device connected with mobile data or wifi
    * if device connected with network call api function
    * otherwise show alert dialog to enable network*/
    private fun checkNetworkConnection() {
        if (!Util.isNetworkAvailable(application)) {
            showNetworkEnableDialog()
        } else {
            startLayoutVisible()
        }
    }

    /*Set layout visibility then callAPI*/
    private fun startLayoutVisible() {
        binding?.healthRecyclerView?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE

        callAPI() /*Coroutine*/
        //callRxAPI()  /*2nd way  RxJava*/
    }

    /*Call api from mainViewModel class & observe value*/
    private fun callAPI() {
        mainViewModel?.getServerHealthCheckDetails()?.observe(this,{
            it?.let {
                Log.i(TAG, "callAPI test"+ it.status)
                when(it.status){
                    Status.LOADING ->{
                        binding?.progressBar?.visibility = View.VISIBLE
                    }

                    Status.SUCCESS ->{
                        binding?.progressBar?.visibility = View.GONE
                        healthCheck = it.data
                        loadAdapter()
                    }

                    Status.ERROR ->{
                        binding?.progressBar?.visibility = View.GONE
                        binding?.txtNoData?.visibility = View.VISIBLE
                        binding?.txtNoData?.text = it.message
                    }
                }
            }
        })
    }

    /*Once live data observed from view model load adapter*/
    private fun loadAdapter() {
        Log.i(TAG, "Adapter test")
        binding?.txtNoData?.visibility = View.GONE
        binding?.healthRecyclerView?.visibility = View.VISIBLE

        /*initialize LinearLayoutManager,  RecyclerView, Adapter HealthList then load adapter */
        val healDetailsList: List<Health> = healthCheck!!.data.health
        linearLayoutManager = LinearLayoutManager(this)
        binding?.healthRecyclerView?.layoutManager = linearLayoutManager
        healthAdapter = HealthAdapter(this, healDetailsList)
        binding?.healthRecyclerView?.adapter = healthAdapter
    }

    /*Show alert dialog to enable network*/
    private fun showNetworkEnableDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.enable_data))
        builder.setMessage(resources.getString(R.string.enable_data_message))

        /*alert dialog positive button*/
        builder.setPositiveButton(resources.getString(R.string.wi_fi)) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        }

        /* alert dialog negative buttons*/
        builder.setNegativeButton(resources.getString(R.string.mobile_data)) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
            startActivity(intent)
        }

        /* alert dialog neutral buttons*/
        builder.setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }

        /*set dialog non cancelable*/
        builder.setCancelable(false)

        /* create the alert dialog and show it*/
        if(!isFinishing) {
            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
    }


    /*2nd way  RxJava*/
    private fun callRxAPI() {
        mainViewModel?.getHealthCheckList()

        mainViewModel?.response?.observe(this, {
            it?.let {
                Log.i(TAG, "callRxAPI test"+ it.toString())
                when(it){
                    Status.LOADING ->{
                        binding?.progressBar?.visibility = View.VISIBLE
                    }

                    Status.SUCCESS ->{
                        binding?.progressBar?.visibility = View.GONE
                    }

                    Status.ERROR ->{
                        binding?.progressBar?.visibility = View.GONE
                        binding?.txtNoData?.visibility = View.VISIBLE
                        binding?.txtNoData?.text = it.toString()
                    }
                }
            }
        })

        mainViewModel?.healthChekList?.observe(this, {
            if(it.statusCode == 200 && it.success) {
                binding?.progressBar?.visibility = View.GONE
                healthCheck = it
                loadAdapter()
            }else {
                binding?.progressBar?.visibility = View.GONE
                binding?.txtNoData?.visibility = View.VISIBLE
                binding?.txtNoData?.text = it.toString()
            }
        })
    }
}