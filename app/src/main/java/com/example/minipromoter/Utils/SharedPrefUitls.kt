package com.example.minipromoter.Utils

import android.app.Activity
import android.content.SharedPreferences
import com.example.minipromoter.App


/* a utils class to save and get data from shared preferences
* */
object SharedPrefUtils {
    private var mSharedPref: SharedPreferences? = null

    init {
        if (mSharedPref == null)
            mSharedPref = App.getInstance()
                .getSharedPreferences(App.getInstance().packageName, Activity.MODE_PRIVATE)
    }

    fun readString(key: String, defValue: String): String? {
        return if (mSharedPref != null) mSharedPref!!.getString(key, defValue) else defValue
    }

    fun writeString(key: String, value: String) {
        if (mSharedPref != null) {
            val prefsEditor = mSharedPref!!.edit()
            prefsEditor.putString(key, value)
            prefsEditor.apply()
        }
    }

    fun readBoolean(key: String, defValue: Boolean): Boolean {
        return if (mSharedPref != null) mSharedPref!!.getBoolean(key, defValue) else defValue
    }

    fun writeBoolean(key: String, value: Boolean) {
        if (mSharedPref != null) {
            val prefsEditor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }
    }

    fun readInteger(key: String, defValue: Int): Int? {
        return if (mSharedPref != null) mSharedPref!!.getInt(key, defValue) else defValue
    }

    fun writeInteger(key: String, value: Int?) {
        if (mSharedPref != null) {
            val prefsEditor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value!!).apply()
        }
    }

    fun removeAll() {
        if (mSharedPref != null) {
            val prefsEditor = mSharedPref!!.edit()
            prefsEditor.clear().apply()
        }
    }

    fun remove(key: String) {
        if (mSharedPref != null) {
            val prefsEditor = mSharedPref!!.edit()
            prefsEditor.remove(key).apply()
        }
    }
}
