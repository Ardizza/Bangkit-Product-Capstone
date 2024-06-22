package com.example.ecometer.ui.calculator

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.ecometer.R
import com.example.ecometer.databinding.FragmentCalculatorBinding
import java.text.SimpleDateFormat
import java.util.*

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        calculatorViewModel.result.observe(viewLifecycleOwner, Observer {
            binding.textResult.text = it
        })

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editTextDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }

        binding.editTextEngineCapacity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString().replace(" CC", "").trim()
                    if (input.isNotEmpty()) {
                        binding.editTextEngineCapacity.removeTextChangedListener(this)
                        val formatted = "$input CC"
                        binding.editTextEngineCapacity.setText(formatted)
                        binding.editTextEngineCapacity.setSelection(formatted.length - 3)
                        binding.editTextEngineCapacity.addTextChangedListener(this)
                        calculatorViewModel.setEngineCapacity(input)
                    }
                }
            }
        })

        binding.editTextCylinderCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    if (input.isNotEmpty()) {
                        calculatorViewModel.setCylinderCount(input)
                    }
                }
            }
        })

        binding.editTextDistance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString().replace(" km", "").trim()
                    if (input.isNotEmpty()) {
                        binding.editTextDistance.removeTextChangedListener(this)
                        val formatted = "$input km"
                        binding.editTextDistance.setText(formatted)
                        binding.editTextDistance.setSelection(formatted.length - 3)
                        binding.editTextDistance.addTextChangedListener(this)
                        calculatorViewModel.setDistance(input)
                    }
                }
            }
        })

        binding.buttonCalculate.setOnClickListener {
            calculatorViewModel.calculate()
        }

        binding.buttonReset.setOnClickListener {
            resetInputs()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.vehicle_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerVehicleType.adapter = adapter
        }

        binding.spinnerVehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedVehicleType = parent?.getItemAtPosition(position).toString()
                updateFuelTypeSpinner(selectedVehicleType)
                calculatorViewModel.setVehicleType(selectedVehicleType)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerFuelType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFuelType = parent?.getItemAtPosition(position).toString()
                calculatorViewModel.setFuelType(selectedFuelType)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateFuelTypeSpinner(vehicleType: String) {
        val fuelTypeArray = when (vehicleType) {
            "Car" -> R.array.fuel_types_car
            "Motorcycle" -> R.array.fuel_types_motorcycle
            else -> R.array.fuel_types_car
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            fuelTypeArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFuelType.adapter = adapter
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                val dateString = dateFormat.format(selectedDate.time)
                binding.editTextDate.setText(dateString)
                calculatorViewModel.setDate(dateString)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun resetInputs() {
        binding.editTextDate.text.clear()
        binding.editTextEngineCapacity.text.clear()
        binding.editTextCylinderCount.text.clear()
        binding.editTextDistance.text.clear()
        binding.spinnerVehicleType.setSelection(0)
        binding.spinnerFuelType.setSelection(0)
        binding.textResult.text = ""
        calculatorViewModel.reset()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
