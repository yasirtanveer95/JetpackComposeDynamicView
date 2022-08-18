package com.example.dymanicviews

data class ViewProperties(
    var isRequired: Boolean = false,
    var isError: Boolean = false,
    var answerText: String = ""
)