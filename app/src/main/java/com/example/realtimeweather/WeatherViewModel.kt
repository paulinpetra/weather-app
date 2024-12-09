package com.example.realtimeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeweather.api.Constant
import com.example.realtimeweather.api.NetworkResponse
import com.example.realtimeweather.api.RetrofitInstance
import com.example.realtimeweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    //getting the instance of weather api
    private val weatherApi = RetrofitInstance.weatherApi

    //getting the result from weather api wrapped in Network response
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()

    //expose the mutable data as live data
     val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading
        //wrapping the async call
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load data")

            }
        }
    }
}