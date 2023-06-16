package com.dicoding.capthree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThemeFactory(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> ThemeViewModel(pref) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
