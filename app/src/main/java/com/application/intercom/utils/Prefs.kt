package com.application.intercom.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.BuildConfig

import com.google.gson.Gson

class Prefs(val context: Context) {
    companion object {
        const val PREFERENCE_NAME = BuildConfig.APPLICATION_ID
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    fun put(key: String, long: Long) {
        editor.putLong(key, long)
        editor.commit()
    }

    fun put(key: String, int: Int) {
        editor.putInt(key, int)
        editor.commit()
    }

    fun put(key: String, string: String) {
        editor.putString(key, string)
        editor.commit()
    }

    fun put(key: String, boolean: Boolean) {
        editor.putBoolean(key, boolean)
        editor.commit()
    }

    fun getLong(key: String): String {
        return pref.getLong(key, 0).toString()
    }

    fun getInt(key: String): String {
        return pref.getInt(key, 0).toString()
    }

    fun getString(key: String, toString: String): String {
        return pref.getString(key, "")!!
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun setBoolean(key: String, boolean: Boolean) {
        editor.putBoolean(key, boolean)
        editor.commit()
    }

    fun setSaveDeviceID(context: Context?): String {
        val shred = context?.getSharedPreferences("projectId", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("projectId", "") ?: ""
    }

    fun getSaveDeviceID(context: Context?, sort: String?) {
        val shred = context?.getSharedPreferences("projectId", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("projectId", sort)?.apply()
    }

    fun getdeviceToken(context: Context?, deviceID: String?) {
        val shred = context?.getSharedPreferences("deviceID", AppCompatActivity.MODE_PRIVATE)
        shred?.edit()?.putString("deviceID", deviceID)?.apply()
    }

    fun setdeviceToken(context: Context?): String {
        val shred = context?.getSharedPreferences("deviceID", AppCompatActivity.MODE_PRIVATE)
        return shred?.getString("deviceID", "") ?: ""
    }

    fun clearData() {
        editor.clear()
        editor.apply()
    }
}
