package com.example.dymanicviews

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.dymanicviews.ui.theme.DymanicViewsTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DymanicViewsTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
//                    .paint(
//                        BitmapPainter(
//                            BitmapFactory
//                                .decodeResource(resources, R.drawable.test)
//                                .asImageBitmap()
//                        ),
//                        contentScale = ContentScale.FillBounds
//                    )
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Divider()

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.setDefaultNightMode(
                                    AppCompatDelegate.MODE_NIGHT_NO
                                )
                                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(5.dp),
                        ) {
                            Text(text = "Change Mode")
                        }
                        Button(
                            onClick = {
                                AppCompatDelegate.setApplicationLocales(
                                    if (AppCompatDelegate.getApplicationLocales()
                                            .toLanguageTags() == "ur-PK"
                                    ) LocaleListCompat.getEmptyLocaleList() else LocaleListCompat.forLanguageTags(
                                        "ur-PK"
                                    )
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(5.dp),
                        ) {
                            Text(text = "Change Lng")
                        }
                    }

                    Divider()

                    repeat(3) {
                        val rememberCustomTextFieldPropertiesState =
                            rememberCustomTextFieldPropertiesState(initText = "Yasir")
                        CustomTextFieldComponent(
                            state = rememberCustomTextFieldPropertiesState,
                            hint = "Enter$it your text here"
                        )
                        if (it == 1) {
                            rememberCustomTextFieldPropertiesState.addValidation(
                                TextFieldValidation(ValidationType.Required.value, "Field Required")
                            )
                            rememberCustomTextFieldPropertiesState.setMaxLength(8)
                            val coroutineScope = rememberCoroutineScope()
                            coroutineScope.launch {
                                rememberCustomTextFieldPropertiesState.setTextFieldKeyboardType(1)
                            }
                        }
                        if (it == 2) {
                            rememberCustomTextFieldPropertiesState.addValidation(
                                TextFieldValidation(ValidationType.Required.value, "Field Required")
                            )
                            rememberCustomTextFieldPropertiesState.addValidation(
                                TextFieldValidation(ValidationType.Email.value, "Enter Valid Email")
                            )
                        }

                        Button(
                            onClick = {
                                if (rememberCustomTextFieldPropertiesState.isValid()) Toast.makeText(
                                    this@MainActivity,
                                    rememberCustomTextFieldPropertiesState.getTextFieldValue(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 30.dp),
                        ) {
                            Text(text = "Submit")
                        }
                    }

                    repeat(11) {
                        mainViewModel.addError(it, false)
                        when (it) {
                            0 -> BuildTextView(index = it)
                            1 -> {
                                BuildEditText(index = it)
                                mainViewModel.addRequiredField(it)
                            }

                            2 -> {
                                BuildDatePicket(index = it)
                                mainViewModel.addRequiredField(it)
                            }

                            3 -> BuildButtonView(index = it)
                            4 -> {
                                BuildTimePicket(index = it)
                                mainViewModel.addRequiredField(it)
                            }

                            5 -> {
                                BuildDropDown(index = it)
                                mainViewModel.addRequiredField(it)
                            }

                            6 -> {
                                BuildSearchableDropDown(index = it)
                                mainViewModel.addRequiredField(it)
                            }

                            7 -> {
                                mainViewModel.addRequiredField(it)
                                BuildSlider(index = it)
                            }

                            8 -> {
                                mainViewModel.addRequiredField(it)
                                BuildRangeSlider(index = it)
                            }

                            9 -> {
                                mainViewModel.addRequiredField(it)
                                BuildCheckBox(index = it)
                            }

                            10 -> {
                                mainViewModel.addRequiredField(it)
                                BuildRadioGroup(index = it)
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (mainViewModel.isValid()) Toast.makeText(
                                this@MainActivity, "Its Valid Data", Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 30.dp),
                    ) {
                        Text(text = "Click Me")
                    }
                }
            }
        }
    }

    @Composable
    private fun BuildTextView(index: Int) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), text = "its a $index position"
        )
    }

    @Composable
    private fun BuildButtonView(index: Int) {
        Button(
            onClick = { },
            modifier = Modifier.padding(10.dp),
        ) {
            Text(text = "its a $index position")
        }
    }

    @Composable
    private fun BuildEditText(index: Int) {
        var textValue by remember { mutableStateOf("") }
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError

        OutlinedTextField(value = textValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            label = {
                Text(text = "Position $index")
            },
            isError = showError,
            onValueChange = {
                mainViewModel.addValues(index, it)
                textValue = it
                if (it.isBlank()) {
                    mainViewModel.addError(index, true)
                } else {
                    mainViewModel.addError(index, false)
                }
            })
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BuildDatePicket(index: Int) {
        var dateValue by remember { mutableStateOf("Select date") }
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError

        Box {
            OutlinedTextField(
                value = dateValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                readOnly = true,
                label = {
                    Text(text = "Position $index")
                },
                isError = showError,
                onValueChange = { },
                textStyle = TextStyle(textAlign = TextAlign.Center),
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .padding(10.dp)
                    .clickable(onClick = {
                        Calendar
                            .getInstance()
                            .apply {
                                DatePickerDialog(
                                    this@MainActivity,
                                    { _, year, monthOfYear, dayOfMonth ->
                                        dateValue = "$dayOfMonth/$monthOfYear/$year"
                                        mainViewModel.addValues(index, dateValue)
                                        mainViewModel.addError(index, false)
                                    },
                                    get(Calendar.YEAR),
                                    get(Calendar.MONTH),
                                    get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                    })
            )
        }

//        TextButton(
//            onClick = {
//                Calendar
//                    .getInstance()
//                    .apply {
//                        DatePickerDialog(
//                            this@MainActivity, { _, year, monthOfYear, dayOfMonth ->
//                                dateValue = "$dayOfMonth $monthOfYear, $year"
//                                mainViewModel.addValues(index, dateValue)
//                                mainViewModel.addError(index, false)
//                            },
//                            get(Calendar.YEAR),
//                            get(Calendar.MONTH),
//                            get(Calendar.DAY_OF_MONTH)
//                        ).show()
//                    }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(5.dp)
//        ) {
//            Text(text = dateValue)
//        }
//
//        if (showError)
//            Toast.makeText(this@MainActivity, "Pls Select Data", Toast.LENGTH_SHORT).show()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BuildTimePicket(index: Int) {
        var timeValue by remember { mutableStateOf("Select Time") }
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError

        Box {
            OutlinedTextField(
                value = timeValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                readOnly = true,
                label = {
                    Text(text = "Position $index")
                },
                isError = showError,
                onValueChange = { },
                textStyle = TextStyle(textAlign = TextAlign.Center),
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .padding(10.dp)
                    .clickable(onClick = {
                        Calendar
                            .getInstance()
                            .apply {
                                TimePickerDialog(
                                    this@MainActivity, { _, hourOfDay, minute ->
                                        timeValue = "$hourOfDay : $minute"
                                        mainViewModel.addValues(index, timeValue)
                                        mainViewModel.addError(index, false)
                                    }, get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), true
                                ).show()
                            }
                    })
            )
        }

//        TextButton(
//            onClick = {
//                Calendar
//                    .getInstance()
//                    .apply {
//                        TimePickerDialog(
//                            this@MainActivity,
//                            { _, hourOfDay, minute ->
//                                timeValue = "$hourOfDay : $minute"
//                                mainViewModel.addValues(index, timeValue)
//                                mainViewModel.addError(index, false)
//                            },
//                            get(Calendar.HOUR_OF_DAY),
//                            get(Calendar.MINUTE),
//                            true
//                        ).show()
//                    }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(5.dp)
//        ) {
//            Text(text = timeValue)
//        }
//
//        if (showError)
//            Toast.makeText(this@MainActivity, "Pls Select Time", Toast.LENGTH_SHORT).show()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BuildDropDown(index: Int) {
        val options = listOf("Transfer", "Bill Payment", "Recharges", "Outing", "Other")
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = expanded.not() },
            modifier = Modifier.padding(paddingValues = PaddingValues(horizontal = 5.dp))
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedOptionText,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { },
                isError = showError,
                label = { Text("Purpose") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )
            DropdownMenu(modifier = Modifier.exposedDropdownSize(),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(onClick = {
                        selectedOptionText = selectionOption
                        mainViewModel.addValues(index, selectionOption)
                        mainViewModel.addError(index, false)
                        expanded = false
                    }) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BuildSearchableDropDown(index: Int) {
        val options = mainViewModel.countryList.collectAsState().value
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf("Select Country") }
        var searchText by remember { mutableStateOf("") }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = expanded.not() },
            modifier = Modifier.padding(paddingValues = PaddingValues(horizontal = 5.dp))
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedOptionText,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { },
                isError = showError,
                label = { Text("Country") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )
            DropdownMenu(modifier = Modifier.exposedDropdownSize(),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                OutlinedTextField(value = searchText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    label = {
                        Text(text = "Type Text")
                    },
                    placeholder = {
                        Text(text = "Text to search")
                    },
                    trailingIcon = {
                        IconToggleButton(checked = true, onCheckedChange = {}) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search icon"
                            )
                        }
                    },
                    onValueChange = { text ->
                        searchText = text
                        mainViewModel.searchCountry(text)
                    })
                options.forEach { selectionOption ->
                    DropdownMenuItem(onClick = {
                        selectedOptionText = selectionOption
                        mainViewModel.addValues(index, selectionOption)
                        mainViewModel.addError(index, false)
                        expanded = false
                    }) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }

    @Composable
    private fun BuildSlider(index: Int) {
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError
        var stepsValue by remember { mutableStateOf(.1f) }

        Slider(value = stepsValue, steps = 20, onValueChange = {
            stepsValue = it
            mainViewModel.addValues(index, it.toString())
            mainViewModel.addError(index, false)
        })

        if (showError) {
            Toast.makeText(this, "Select $index Value", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BuildRangeSlider(index: Int) {
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError
        var rangStepsValue by remember { mutableStateOf(0.2f..0.6f) }

        RangeSlider(value = rangStepsValue, onValueChange = {
            rangStepsValue = it
            mainViewModel.addValues(index, it.toString())
            mainViewModel.addError(index, false)
        })
        if (showError) {
            Toast.makeText(this, "Select $index Value", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    private fun BuildCheckBox(index: Int) {
        val options = mainViewModel.countryList.collectAsState().value
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError
        val selectedOptions by remember { mutableStateOf(arrayListOf<String>()) }

        Divider()
        options.forEach { selectionOption ->
            var isChecked by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(checked = isChecked, onCheckedChange = {
                    isChecked = it
                    if (it) {
                        if (selectedOptions.contains(selectionOption).not()) {
                            selectedOptions.add(selectionOption)
                        }
                    } else {
                        if (selectedOptions.contains(selectionOption)) {
                            selectedOptions.remove(selectionOption)
                        }
                    }
                    mainViewModel.addValues(index, selectedOptions.joinToString(","))
                    if (selectedOptions.isEmpty()) mainViewModel.addError(index, true)
                    else mainViewModel.addError(index, false)
                })
                Text(selectionOption)
            }
        }
        Divider()

        if (showError) {
            Toast.makeText(this, "Select $index Value", Toast.LENGTH_SHORT).show()
        }

    }

    @Composable
    private fun BuildRadioGroup(index: Int) {
        val options = mainViewModel.countryList.collectAsState().value
        val showError =
            mainViewModel.viewPropertiesList.collectAsState().value.getValue(index).isError
        var selectedOptionText by remember { mutableStateOf("") }

        Divider()
        options.forEach { selectionOption ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(selected = selectionOption == selectedOptionText, onClick = {
                        selectedOptionText = selectionOption
                        mainViewModel.addValues(index, selectionOption)
                        mainViewModel.addError(index, false)
                    }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(selected = selectionOption == selectedOptionText, onClick = {
                    selectedOptionText = selectionOption
                    mainViewModel.addValues(index, selectionOption)
                    mainViewModel.addError(index, false)
                })
                Text(selectionOption)
            }
        }
        Divider()

        if (showError) {
            Toast.makeText(this, "Select $index Value", Toast.LENGTH_SHORT).show()
        }

    }
}