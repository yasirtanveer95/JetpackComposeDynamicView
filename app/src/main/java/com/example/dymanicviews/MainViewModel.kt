package com.example.dymanicviews

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

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
    var viewPropertiesList = MutableStateFlow<HashMap<Int, ViewProperties>>(hashMapOf())

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
        viewPropertiesList.value = viewPropertiesList.value.also {
            if (it.containsKey(position))
                it[position] = it.getValue(position).copy(isError = isError)
            else
                it[position] = ViewProperties(isError = isError)
        }
    }

    fun addRequiredField(position: Int) {
        viewPropertiesList.value = viewPropertiesList.value.also {
            it[position] = it.getValue(position).copy(isRequired = true)
        }
    }

    fun addValues(position: Int, textValue: String) {
        viewPropertiesList.value = viewPropertiesList.value.also {
            it[position] = it.getValue(position).copy(answerText = textValue)
        }
    }

    fun isValid(): Boolean {
        viewPropertiesList.value = HashMap(viewPropertiesList.value).apply {
            keys.forEach { key ->
                val itemValue = getValue(key)
                put(
                    key,
                    getValue(key).copy(isError = itemValue.isRequired && itemValue.answerText.isBlank())
                )
            }
        }
        return viewPropertiesList.value.any { it.component2().isError }.not()
    }

}