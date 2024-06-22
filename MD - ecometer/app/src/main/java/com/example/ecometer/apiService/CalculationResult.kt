package com.example.ecometer.apiService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class CalculationResult(
    val week: String,
    val data: String,
    val vehicleType: String,
    val engineCapacity: Double,
    val cylinderCount: Int,
    val fuelType: String,
    val distance: Double
)

interface ApiService {
    @POST("https://karbon-api-4aw42iqdea-et.a.run.app/add")
    fun submitResult(@Body calculationResult: CalculationResult): Call<Void>
}
