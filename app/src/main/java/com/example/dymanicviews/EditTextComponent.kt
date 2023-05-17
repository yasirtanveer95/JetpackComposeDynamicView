package com.example.dymanicviews

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


class CustomTextFieldProperties(initialValue: String = "", hasError: Boolean = false) {
    companion object {
        var saver: Saver<CustomTextFieldProperties, *> =
            listSaver(save = { listOf(it.textValue, it.isError) }, restore = {
                CustomTextFieldProperties(initialValue = it[0] as String, it[1] as Boolean)
            })
    }

    private val requiredValidations = arrayListOf<TextFieldValidation>()
    var lastFailedValidation: TextFieldValidation? = null
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

    fun addValidation(validation: TextFieldValidation) {
        requiredValidations.add(validation)
    }

    fun isValid(): Boolean {
        var isValidData = true
        // Loop through the validations and check if the text field value meets the validation criteria
        for (validation in requiredValidations) {
            if (validation.isValid(textValue).not()) {
                lastFailedValidation = validation
                isValidData = false
                break
            }
        }
        setTextFieldError(isValidData.not())
        return isValidData
    }
}

@Composable
fun CustomTextFieldComponent(state: CustomTextFieldProperties, hint: String = "") {

    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
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
        // If the error message is not empty, show it
        if (state.isError) {
            Text(
                text = state.lastFailedValidation?.errorMessage ?: "Failed",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(5.dp),
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun rememberCustomTextFieldPropertiesState(initText: String = ""): CustomTextFieldProperties {
    return rememberSaveable(saver = CustomTextFieldProperties.saver) {
        CustomTextFieldProperties(initText)
    }
}

data class TextFieldValidation(val validationType: Int, val errorMessage: String) {
    fun isValid(value: String): Boolean {
        return when (validationType) {
            ValidationType.Required.value -> value.isNotBlank()
            ValidationType.Email.value -> Patterns.EMAIL_ADDRESS.matcher(value).matches()
            else -> true
        }
    }
}

enum class ValidationType(var value: Int) {
    Required(1), Email(2)
}
