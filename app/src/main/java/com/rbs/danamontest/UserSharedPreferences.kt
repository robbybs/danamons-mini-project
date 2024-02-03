package com.rbs.danamontest

import android.content.Context
import android.content.SharedPreferences

class UserSharedPreferences(context: Context) {
    companion object {
        private const val PREF_NAME = "user_pref"
        private const val IS_LOGIN = "isLogin"
    }

    private var preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setLogin(value: Boolean) {
        editor.putBoolean(IS_LOGIN, value)
        editor.apply()
    }

    fun isLogin(): Boolean = preferences.getBoolean(IS_LOGIN, false)

    fun setRemoveSession() {
        editor.clear()
        editor.apply()
    }
}