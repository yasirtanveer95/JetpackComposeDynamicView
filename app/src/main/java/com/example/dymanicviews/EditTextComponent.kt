package com.example.dymanicviews

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType


class CustomTextFieldProperties(initialValue: String = "", hasError: Boolean = false) {
    companion object {
        var saver: Saver<CustomTextFieldProperties, *> =
            listSaver(save = { listOf(it.textValue, it.isError) }, restore = {
                CustomTextFieldProperties(initialValue = it[0] as String, it[1] as Boolean)
            })
    }

    var textValue by mutableStateOf(initialValue)
    var isError by mutableStateOf(hasError)
    var maxLengthAllowed by mutableStateOf(0)
    var isEnabled by mutableStateOf(true)
    var keyboardType by mutableStateOf(0)

    fun getTextFieldValue() = textValue

    fun setTextFieldError(error: Boolean) {
        isError = error
    }

    fun setValue(newTextValue: String) {
        textValue = newTextValue
    }

    /* Use coroutineScope to Update Value this will reflect intimidatingly value*/
    fun setTextFieldEnable(isEnable: Boolean) {
        isEnabled = isEnable
    }

    fun setMaxLength(maxLength: Int) {
        maxLengthAllowed = maxLength
    }

    /* Use coroutineScope to Update Value this will reflect intimidatingly value*/
    fun setTextFieldKeyboardType(type: Int) {
        keyboardType = type
    }

    fun isValid(): Boolean {
        return if (textValue.isNotBlank()) {
            setTextFieldError(false)
            true
        } else {
            Log.e("Field", "Invalid Field Data")
            setTextFieldError(true)
            false
        }
    }
}

@Composable
fun CustomTextFieldComponent(state: CustomTextFieldProperties, hint: String = "") {

    OutlinedTextField(
        value = state.getTextFieldValue(),
        onValueChange = {
            if (state.maxLengthAllowed == 0 || it.length <= state.maxLengthAllowed) {
                state.setValue(it)
                state.setTextFieldError(state.isValid().not())
            }
        },
        modifier = Modifier.fillMaxWidth(),
        isError = state.isError,
        enabled = state.isEnabled,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            keyboardType = when (state.keyboardType) {
                1 -> KeyboardType.Number
                else -> KeyboardType.Text
            }
        ),
        label = { Text(text = hint) },
        singleLine = true
    )
}

@Composable
fun rememberCustomTextFieldPropertiesState(initText: String = ""): CustomTextFieldProperties {
    return rememberSaveable(saver = CustomTextFieldProperties.saver) {
        CustomTextFieldProperties(initText)
    }
}
