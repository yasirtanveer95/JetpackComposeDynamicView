package com.example.dymanicviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var actualCountryList = arrayListOf(
        "Pakistan",
        "India",
        "Nepal",
        "Australia",
        "UAE",
        "NewZeeland"
    )
    var countryList = MutableStateFlow(actualCountryList)
    var viewPropertiesMap = HashMap<Int, ViewProperties>()
    private var viewPropertiesFlow = MutableSharedFlow<HashMap<Int, ViewProperties>>()
    var viewPropertiesDetails = viewPropertiesFlow.asSharedFlow()

    fun searchCountry(searchText: String) {
        if (searchText.isBlank()) {
            countryList.value = actualCountryList
        } else {
            countryList.value = actualCountryList.filter {
                it.startsWith(searchText, ignoreCase = true)
            } as ArrayList<String>
        }
    }

    fun addError(position: Int, isError: Boolean) {
        viewPropertiesMap.let {
            if (it.containsKey(position))
                it[position] = it.getValue(position).copy(isError = isError)
            else
                it[position] = ViewProperties(isError = isError)
        }
        viewModelScope.launch {
            viewPropertiesFlow.emit(viewPropertiesMap)
        }
    }

    fun addRequiredField(position: Int) {
        viewPropertiesMap.let {
            it[position] = it.getValue(position).copy(isRequired = true)
        }
    }

    fun addValues(position: Int, textValue: String) {
        viewPropertiesMap.let {
            it[position] = it.getValue(position).copy(answerText = textValue)
        }
    }

    fun isValid(): Boolean {
        viewPropertiesMap = HashMap(viewPropertiesMap).apply {
            keys.forEach { key ->
                val itemValue = getValue(key)
                put(
                    key,
                    getValue(key).copy(isError = itemValue.isRequired && itemValue.answerText.isBlank())
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            viewPropertiesFlow.emit(viewPropertiesMap)
        }
        return viewPropertiesMap.any { it.component2().isError }.not()
    }

}