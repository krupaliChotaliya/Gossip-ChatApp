package com.android.chatsapp.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager {

    private var SharedPrefName:String="chatsapp"
    private lateinit var sharedPreferences:SharedPreferences
    private var context:Context
    private lateinit var editor:SharedPreferences.Editor

    constructor(context: Context) {
        this.context = context
    }

    fun saveUser(userID:String){
        sharedPreferences=context.getSharedPreferences(SharedPrefName,Context.MODE_PRIVATE)
        editor=sharedPreferences.edit()
        editor.putString("userId",userID)
        editor.apply()
    }

    fun isLoggedIn(): String? {
        sharedPreferences=context.getSharedPreferences(SharedPrefName,Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId",null)
    }

    fun logout(): Boolean {
        sharedPreferences=context.getSharedPreferences(SharedPrefName,Context.MODE_PRIVATE)
        editor=sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }
}