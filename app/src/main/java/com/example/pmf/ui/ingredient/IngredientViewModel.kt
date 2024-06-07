package com.example.pmf.ui.ingredient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class  IngredientViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Ingted Fragment"
    }
    val text: LiveData<String> = _text
}