package com.example.phoneapp.models

sealed class ContactItem {
    data class Section(val letter: Char) : ContactItem()
    data class Entry(val contact: Contact) : ContactItem()
}