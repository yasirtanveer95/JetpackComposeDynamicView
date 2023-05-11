package com.example.dymanicviews

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle


class EditTextComponent(
    var hint: String,
    var textColor: Color = Color.Black,
    var backgroundColor: Color = Color.White,
    var initialValue: String = ""
) {
    private val fieldValue = mutableStateOf(initialValue)
    private val isError = mutableStateOf(false)

    fun getTextFieldValue() = fieldValue.value

    fun setError(error: Boolean) {
        isError.value = error
    }

    fun setValue(newValue: String) {
        fieldValue.value = newValue
    }

    fun isValid(): Boolean {
        return if (fieldValue.value.isNotBlank()) {
            setError(false)
            true
        } else {
            Log.e("Field", "Invalid Field Data")
            setError(true)
            false
        }
    }

    @Composable
    fun Render() {
        MyTextField(hint = hint,
            textColor = textColor,
            backgroundColor = backgroundColor,
            value = fieldValue.value,
            isError = isError.value,
            onValueChange = { newValue ->
                setError(false)
                fieldValue.value = newValue
            })
    }

    @Composable
    fun MyTextField(
        hint: String,
        textColor: Color = Color.Black,
        backgroundColor: Color = Color.White,
        value: String,
        isError: Boolean,
        onValueChange: (String) -> Unit
    ) {
        val context = LocalContext.current
        var textValue by rememberSaveable { mutableStateOf(value) }
        val currentOrientation by remember { mutableStateOf(context.resources.configuration.orientation) }

        LaunchedEffect(currentOrientation) {
            onValueChange(textValue)
        }

        OutlinedTextField(value = textValue,
            onValueChange = {
                textValue = it
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor),
            isError = isError,
            textStyle = TextStyle(color = textColor),
            label = { Text(text = hint) })
    }

}
