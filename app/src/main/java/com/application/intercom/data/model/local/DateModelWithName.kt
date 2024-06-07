package com.application.intercom.data.model.local

data class DateModelWithName(
    var name: String,
    var id: String,
    var amount: String,
    var dueDate: String,
    var isChecked: Boolean = false
)
