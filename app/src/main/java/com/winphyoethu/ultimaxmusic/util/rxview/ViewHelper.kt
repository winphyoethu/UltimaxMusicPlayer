package com.winphyoethu.ultimaxmusic.util.rxview

import android.widget.Button
import android.widget.EditText
import io.reactivex.subjects.PublishSubject

interface ViewHelper {

    fun bindEditText(editText: EditText): PublishSubject<String>

    fun bindButton(button: Button): PublishSubject<Unit>

}