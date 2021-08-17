package com.winphyoethu.ultimaxmusic.util.sharedpreference

interface PreferenceHelper {

    fun putString(key: String, value: String)

    fun getString(key: String, defValue: String): String?

    fun putInt(key: String, value: Int)

    fun getInt(key: String, defValue: Int): Int

    fun putBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun remove(key: String)

}