package id.ac.polbeng.ardianto.onlineservice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserServiceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ini adalah halaman service Fragment"
    }
    val text: LiveData<String> = _text
}