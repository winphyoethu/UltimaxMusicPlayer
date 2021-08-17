package com.winphyoethu.ultimaxmusic.util

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.makeToast(text: String, duration: Int) {
    Toast.makeText(this, text, duration).show()
}

fun Cursor.getStringFromCursor(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getLongFromCursor(columnName: String): Long {
    return getLong(getColumnIndex(columnName))
}