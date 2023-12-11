package com.android.chatsapp.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.android.chatsapp.BR

class UserViewModel : ViewModel() {
    
//    val status= MutableLiveData<String>()
//    val statusVal: LiveData<String>
//        get() = status
//
//    fun updateItems(statusvalue:String){
//        status.value=statusvalue
//    }

    val status = ObservableField<String>()
    init {
        status.set("John")
    }

    fun updateValues(value:String) {
        Log.i("viewmodel",value)
        status.set(value)
    }
}