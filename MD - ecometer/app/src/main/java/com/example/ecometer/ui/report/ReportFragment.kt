package com.example.ecometer.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecometer.databinding.FragmentReportBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lineChart = binding.lineChart

        setupLineChart()

        // Dummy data for demonstration
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 20f))
        entries.add(Entry(2f, 50f))
        entries.add(Entry(3f, 30f))
        entries.add(Entry(4f, 80f))

        val dataSet = LineDataSet(entries, "Label")
        dataSet.color = resources.getColor(android.R.color.holo_blue_light)
        dataSet.valueTextColor = resources.getColor(android.R.color.black)

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()
        return root
    }

    private fun setupLineChart() {
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description = Description().apply {
            text = "Grafik Hasil Kalkulasi"
        }
        lineChart.legend.isEnabled = false
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}