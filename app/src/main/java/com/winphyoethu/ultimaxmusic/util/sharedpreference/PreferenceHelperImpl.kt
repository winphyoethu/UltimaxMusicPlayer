package com.winphyoethu.ultimaxmusic.util.sharedpreference

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.winphyoethu.ultimaxmusic.util.Constants
import javax.inject.Inject

class PreferenceHelperImpl @Inject constructor(app: Application) : PreferenceHelper {

    private val sp: SharedPreferences =
        app.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sp.edit()

    override fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    override fun getString(key: String, defValue: String): String? {
        return sp.getString(key, defValue)
    }

    override fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    override fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    override fun remove(key: String) {
        editor.remove(key)
        editor.apply()
    }

}