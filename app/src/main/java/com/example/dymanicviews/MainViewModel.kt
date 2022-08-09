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
    var requiredFieldsList = MutableStateFlow<ArrayList<Int>>(arrayListOf())
    var errorList = MutableStateFlow<HashMap<Int, Boolean>>(hashMapOf())
    var answerList = MutableStateFlow<HashMap<Int, String>>(hashMapOf())

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
        errorList.value = errorList.value.also {
            it[position] = isError
        }
    }

    fun addRequiredField(position: Int) {
        requiredFieldsList.value = requiredFieldsList.value.also {
            if (it.contains(position).not()) {
                it.add(position)
            }
        }
    }

    fun addValues(position: Int, textValue: String) {
        answerList.value = answerList.value.also {
            it[position] = textValue
        }
    }

    fun isValid(): Boolean {
        errorList.value = HashMap(errorList.value).apply {
            requiredFieldsList.value.forEach {
                if (answerList.value.containsKey(it).not()) {
                    put(it, true)
                }
            }
        }
        return errorList.value.any { it.component2() }.not()
    }

}