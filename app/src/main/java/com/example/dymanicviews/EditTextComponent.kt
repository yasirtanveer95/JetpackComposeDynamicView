package com.example.dymanicviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.MutableLiveData


class EditTextComponent(
    var hint: String,
    var textColor: Color = Color.Black,
    var backgroundColor: Color = Color.White,
    var initialValue: String = ""
) {
    private val state = MutableLiveData(initialValue)
    private val isError = MutableLiveData(false)

    @Composable
    fun Render() {
        MyTextField(hint = hint,
            textColor = textColor,
            backgroundColor = backgroundColor,
            value = state.observeAsState().value ?: "",
            isError = isError.observeAsState().value ?: false,
            onValueChange = { newValue ->
                setError(false)
                state.value = newValue
            })
    }

    fun getTextFieldValue() = state.value

    fun setError(error: Boolean) {
        isError.value = error
    }

    fun setValue(newValue: String) {
        state.value = newValue
    }

    fun isValid(): Boolean {
        return state.value.isNullOrBlank().not()
    }

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
    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        isError = isError,
        textStyle = TextStyle(color = textColor),
        label = { Text(text = hint) })
}
