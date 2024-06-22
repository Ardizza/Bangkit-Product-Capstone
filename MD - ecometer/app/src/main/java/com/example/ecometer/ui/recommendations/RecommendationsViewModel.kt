package com.example.ecometer.ui.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendationsViewModel : ViewModel() {

    private val _tips = listOf(
        "Kurangi penggunaan kendaraan bermotor dan beralihlah ke transportasi umum atau bersepeda.",
        "Menghemat energi di rumah dengan mematikan perangkat elektronik yang tidak digunakan.",
        "Gunakan produk yang ramah lingkungan dan kurangi penggunaan plastik sekali pakai.",
        "Tanam pohon dan pelihara taman untuk membantu menyerap CO2 dari udara.",
        "Dukung kebijakan dan program yang bertujuan untuk mengurangi emisi karbon."
    )

    private val _tipNumbers = (1.._tips.size).map { "$it." }

    private val _tipTexts = MutableLiveData<List<String>>().apply { value = _tips }
    val tipTexts: LiveData<List<String>> = _tipTexts

    private val _tipNums = MutableLiveData<List<String>>().apply { value = _tipNumbers }
    val tipNums: LiveData<List<String>> = _tipNums
}
