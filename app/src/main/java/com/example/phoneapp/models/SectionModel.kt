package com.example.phoneapp.models

data class SectionModel(
    val letter: Char,
    val contacts: List<Contact>,
    var expanded: Boolean = true
)