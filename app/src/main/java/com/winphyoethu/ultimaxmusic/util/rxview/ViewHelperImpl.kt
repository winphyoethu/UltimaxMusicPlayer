package com.winphyoethu.ultimaxmusic.util.rxview

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewHelperImpl @Inject constructor(): ViewHelper{

    override fun bindEditText(editText: EditText): PublishSubject<String> {

        val editTextPublisher: PublishSubject<String> = PublishSubject.create()

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextPublisher.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return editTextPublisher

    }

    override fun bindButton(button: Button): PublishSubject<Unit> {

        val buttonPublisher: PublishSubject<Unit> = PublishSubject.create()

        button.setOnClickListener {
            buttonPublisher.onNext(Unit)
        }

        return buttonPublisher

    }

}