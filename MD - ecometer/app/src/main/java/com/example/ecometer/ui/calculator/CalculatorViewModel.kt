package com.example.ecometer.ui.calculator

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecometer.apiService.CalculationResult
import com.example.ecometer.apiService.RetrofitClient
import com.example.ecometer.helper.TFLiteHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val tfliteHelper = TFLiteHelper(application)

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _vehicleType = MutableLiveData<String>()
    val vehicleType: LiveData<String> = _vehicleType

    private val _engineCapacity = MutableLiveData<Double>()
    val engineCapacity: LiveData<Double> = _engineCapacity

    private val _cylinderCount = MutableLiveData<Int>()
    val cylinderCount: LiveData<Int> = _cylinderCount

    private val _fuelType = MutableLiveData<String>()
    val fuelType: LiveData<String> = _fuelType

    private val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double> = _distance

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    // Fungsi untuk mengatur tanggal
    fun setDate(date: String) {
        _date.value = date
    }

    // Fungsi untuk mengatur jenis kendaraan
    fun setVehicleType(vehicleType: String) {
        _vehicleType.value = vehicleType
    }

    // Fungsi untuk mengatur kapasitas mesin
    fun setEngineCapacity(engineCapacity: String) {
        val capacity = engineCapacity.replace(" CC", "").toDoubleOrNull() ?: -1.0
        if (capacity >= 0) {
            _engineCapacity.value = capacity
        } else {
            _result.value = "Invalid engine capacity"
        }
    }

    // Fungsi untuk mengatur jumlah silinder
    fun setCylinderCount(cylinderCount: String) {
        val count = cylinderCount.toIntOrNull() ?: -1
        if (count >= 0) {
            _cylinderCount.value = count
        } else {
            _result.value = "Invalid cylinder count"
        }
    }

    // Fungsi untuk mengatur jenis bahan bakar
    fun setFuelType(fuelType: String) {
        _fuelType.value = fuelType
    }

    // Fungsi untuk mengatur jarak
    fun setDistance(distance: String) {
        val distanceInKm = distance.replace(" km", "").toDoubleOrNull() ?: -1.0
        if (distanceInKm >= 0) {
            _distance.value = distanceInKm
        } else {
            _result.value = "Invalid distance"
        }
    }

    // Fungsi untuk menormalisasi nilai
    private fun normalize(value: Float, min: Float, max: Float): Float {
        return (value - min) / (max - min)
    }

    // Fungsi untuk menghitung dan mengirim hasil ke API
    fun calculate() {
        val vehicleType = _vehicleType.value ?: return
        val engineCapacity = _engineCapacity.value ?: return
        val cylinderCount = _cylinderCount.value ?: return
        val fuelTypeValue = _fuelType.value ?: return
        val distance = _distance.value ?: return

        // Terjemahkan jenis bahan bakar ke nilai numerik
        val fuelType = when (vehicleType) {
            "Car" -> when (fuelTypeValue) {
                "Small (Up to 1500 cc)" -> 7.0f
                "Medium (Up to 3000 cc)" -> 10.0f
                "Large (Over 3000 cc)" -> 15.0f
                else -> 0.0f
            }
            "Motorcycle" -> when (fuelTypeValue) {
                "125 cc" -> 2.0f
                "250 cc" -> 3.5f
                "500 cc" -> 5.0f
                else -> 0.0f
            }
            else -> 0.0f
        }

        // Konversi kapasitas mesin ke liter dengan membagi 1000
        val engineCapacityInLiters = engineCapacity / 1000.0

        // Dapatkan nilai min dan max secara dinamis
        val minValue = min(engineCapacityInLiters.toFloat(), min(cylinderCount.toFloat(), fuelType))
        val maxValue = max(engineCapacityInLiters.toFloat(), max(cylinderCount.toFloat(), fuelType))

        // Normalisasi nilai input
        val normalizedEngineCapacity = normalize(engineCapacityInLiters.toFloat(), minValue, maxValue)
        val normalizedCylinderCount = normalize(cylinderCount.toFloat(), minValue, maxValue)
        val normalizedFuelType = normalize(fuelType, minValue, maxValue)

        // Siapkan data input untuk model TFLite
        val inputs = floatArrayOf(normalizedEngineCapacity, normalizedCylinderCount, normalizedFuelType)

        // Dapatkan prediksi dari model TFLite
        val prediction = tfliteHelper.predict(inputs)

        // Hitung hasil akhir dengan jarak
        val finalResult = prediction * distance.toFloat()

        // Format hasil akhir menjadi 2 angka desimal
        val decimalFormat = DecimalFormat("#.##")
        val formattedResult = decimalFormat.format(finalResult)
        _result.value = "Calculated Result: ${formattedResult} gr"

        // Kirim hasil ke API
        submitResult(
            CalculationResult(
                week = _date.value ?: "",  // Masukkan tanggal ke kolom "week"
                data = formattedResult,   // Masukkan hasil ke kolom "data"
                vehicleType = vehicleType,
                engineCapacity = engineCapacity,
                cylinderCount = cylinderCount,
                fuelType = fuelTypeValue,
                distance = distance
            )
        )
    }

    // Fungsi untuk mengirim hasil ke API
    private fun submitResult(calculationResult: CalculationResult) {
        val call = RetrofitClient.instance.submitResult(calculationResult)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CalculatorViewModel", "Result submitted successfully")
                } else {
                    Log.e("CalculatorViewModel", "Failed to submit result: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CalculatorViewModel", "Error submitting result", t)
            }
        })
    }

    // Fungsi untuk mereset nilai
    fun reset() {
        _date.value = ""
        _vehicleType.value = ""
        _engineCapacity.value = 0.0
        _cylinderCount.value = 0
        _fuelType.value = ""
        _distance.value = 0.0
        _result.value = ""
    }
}
